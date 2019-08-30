/**
 *
 */
package com.prady.sample.tx.helper;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import org.apache.commons.lang3.RandomUtils;
import org.springframework.web.reactive.function.client.WebClient;

import com.prady.sample.tx.dto.CustomerDTO;
import com.prady.sample.tx.dto.ProductDTO;

import reactor.core.publisher.Mono;

/**
 * @author Prady
 *
 */
public class Helper {

    public static List<ProductDTO> createProducts(int port, String resourcePath, int noOfItems) {
        List<ProductDTO> savedProducts = new ArrayList<>();
        WebClient webClient = WebClient.create("http://localhost:" + port);
        IntStream.range(0, noOfItems).forEach(i -> {
            ProductDTO product = populateProductDTO(i);
            //  @formatter:off
            ProductDTO savedProduct = webClient.post()
                    .uri(resourcePath)
                    .body(Mono.just(product), ProductDTO.class)
                    .retrieve()
                    .bodyToMono(ProductDTO.class)
                    .block();
            savedProducts.add(savedProduct);
            // @formatter:on
        });
        return savedProducts;
    }

    public static ProductDTO populateProductDTO(int i) {
        ProductDTO product = new ProductDTO();
        product.setProductCode("Code " + i + ":" + RandomUtils.nextLong(0, 1000));
        product.setProductName("Product Name " + RandomUtils.nextLong(0, 1000));
        product.setUnitPrice(new BigDecimal(1000 * (i + 1)));
        product.setUnitsInStock(100 * (i + 1));
        return product;
    }

    public static List<CustomerDTO> createCustomers(int port, String resourcePath, int noOfItems) {
        List<CustomerDTO> savedCustomers = new ArrayList<>();
        WebClient webClient = WebClient.create("http://localhost:" + port);
        IntStream.range(0, noOfItems).forEach(i -> {
            CustomerDTO customer = populateCustomerDTO(i);
            //  @formatter:off
            CustomerDTO savedCustomer = webClient.post()
                    .uri(resourcePath)
                    .body(Mono.just(customer), CustomerDTO.class)
                    .retrieve()
                    .bodyToMono(CustomerDTO.class)
                    .block();
            savedCustomers.add(savedCustomer);
            // @formatter:on
        });
        return savedCustomers;
    }

    public static CustomerDTO populateCustomerDTO(int i) {
        CustomerDTO customer = new CustomerDTO();
        customer.setFirstName("First Name " + i + ":" + RandomUtils.nextLong(0, 1000));
        customer.setLastName("Last Name " + RandomUtils.nextLong(0, 1000));
        customer.setTitle("Title " + RandomUtils.nextLong(0, 1000));
        return customer;
    }
}
