/**
 *
 */
package com.prady.sample.tx.helper;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.IntStream;

import org.apache.commons.lang3.RandomUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import com.prady.sample.tx.dto.CustomerDTO;
import com.prady.sample.tx.dto.OrderDTO;
import com.prady.sample.tx.dto.OrderDetailDTO;
import com.prady.sample.tx.dto.ProductDTO;

import reactor.core.publisher.Mono;

/**
 * @author Prady
 *
 */
@Component
public class DataStoreHelper {

    private List<ProductDTO> savedProducts = new ArrayList<>();
    private List<CustomerDTO> savedCustomers = new ArrayList<>();
    private List<OrderDTO> savedOrders = new ArrayList<>();

    @Value("${product.resource.path:/products}")
    protected String productResourcePath;
    @Value("${customer.resource.path:/customers}")
    protected String customerResourcePath;
    @Value("${order.resource.path:/orders}")
    protected String resourcePath;

    private Random rand = new Random();

    public void createInitialData(int port) {
        savedProducts.addAll(createProducts(port, productResourcePath, 5));
        savedCustomers.addAll(createCustomers(port, customerResourcePath, 5));

        WebClient webClient = WebClient.create("http://localhost:" + port);
        IntStream.range(0, 5).forEach(i -> {
            OrderDTO order = populateOrderDTO(i);
            //  @formatter:off
            OrderDTO savedOrder = webClient.post()
                    .uri(resourcePath)
                    .body(Mono.just(order), OrderDTO.class)
                    .retrieve()
                    .bodyToMono(OrderDTO.class)
                    .block();

            savedOrders.add(savedOrder);
            // @formatter:on
        });
    }

    public List<ProductDTO> createProducts(int port, String resourcePath, int noOfItems) {
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

    public ProductDTO populateProductDTO(int i) {
        ProductDTO product = new ProductDTO();
        product.setProductCode("Code " + i + ":" + RandomUtils.nextLong(0, 1000));
        product.setProductName("Product Name " + RandomUtils.nextLong(0, 1000));
        product.setUnitPrice(new BigDecimal(1000 * (i + 1)));
        product.setUnitsInStock(100 * (i + 1));
        return product;
    }

    public List<CustomerDTO> createCustomers(int port, String resourcePath, int noOfItems) {
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

    public CustomerDTO populateCustomerDTO(int i) {
        CustomerDTO customer = new CustomerDTO();
        customer.setFirstName("First Name " + i + ":" + RandomUtils.nextLong(0, 1000));
        customer.setLastName("Last Name " + RandomUtils.nextLong(0, 1000));
        customer.setTitle("Title " + RandomUtils.nextLong(0, 1000));
        return customer;
    }

    public OrderDTO populateOrderDTO(int i) {
        Optional<CustomerDTO> customerOp = savedCustomers.stream().findAny();
        Optional<ProductDTO> productOp = savedProducts.stream().findAny();
        if (productOp.isPresent() && customerOp.isPresent()) {
            OrderDTO order = new OrderDTO();
            order.setCustomerId(customerOp.get().getId());
            order.setOrderDate(new Date());
            order.setShippedDate(new Date(LocalDate.now().plusDays(10).toEpochDay()));
            order.setStatus("ORDERED");
            OrderDetailDTO detail = new OrderDetailDTO();
            detail.setProductId(productOp.get().getId());
            detail.setQuantity(2);
            detail.setUnitPrice(productOp.get().getUnitPrice());
            order.setDetails(new HashSet<>());
            order.getDetails().add(detail);
            return order;
        }
        return null;
    }

    /**
     * @return
     */
    public ProductDTO getAnyProduct() {
        return savedProducts.get(rand.nextInt(savedProducts.size()));
    }

    /**
     * @param product
     */
    public void removeProduct(ProductDTO product) {
        savedProducts.remove(product);
    }

    /**
     * @return
     */
    public CustomerDTO getAnyCustomer() {
        return savedCustomers.get(rand.nextInt(savedCustomers.size()));
    }

    /**
     * @param customer
     */
    public void removeCustomer(CustomerDTO customer) {
        savedCustomers.remove(customer);
    }

    /**
     * @return
     */
    public OrderDTO getAnyOrder() {
        return savedOrders.get(rand.nextInt(savedOrders.size()));
    }

    /**
     * @param order
     */
    public void removeOrder(OrderDTO order) {
        savedOrders.remove(order);
    }
}
