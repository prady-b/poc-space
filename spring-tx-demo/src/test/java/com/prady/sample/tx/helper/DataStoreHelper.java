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
import java.util.stream.IntStream;

import org.apache.commons.lang3.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import com.prady.sample.tx.dto.CustomerDTO;
import com.prady.sample.tx.dto.OrderDTO;
import com.prady.sample.tx.dto.OrderDetailDTO;
import com.prady.sample.tx.dto.ProductDTO;
import com.prady.sample.tx.service.CustomerService;
import com.prady.sample.tx.service.OrderService;
import com.prady.sample.tx.service.ProductService;

import reactor.core.publisher.Mono;

/**
 * @author Prady
 *
 */
@Component
public class DataStoreHelper {

    private List<Long> savedProducts = new ArrayList<>();
    private List<Long> savedCustomers = new ArrayList<>();
    private List<Long> savedOrders = new ArrayList<>();

    @Value("${product.resource.path:/products}")
    protected String productResourcePath;
    @Value("${customer.resource.path:/customers}")
    protected String customerResourcePath;
    @Value("${order.resource.path:/orders}")
    protected String orderResourcePath;

    @Autowired
    private ProductService productService;
    @Autowired
    private CustomerService customerService;
    @Autowired
    private OrderService orderService;

    private final int noOfItems = 20;

    public void createInitialData(int port) {
        savedProducts.addAll(createProducts(port, noOfItems));
        savedCustomers.addAll(createCustomers(port, noOfItems));
        savedOrders.addAll(createOrders(port, noOfItems));
    }

    public List<Long> createProducts(int port, int noOfItems) {
        List<Long> savedProducts = new ArrayList<>();
        WebClient webClient = WebClient.create("http://localhost:" + port);
        IntStream.range(0, noOfItems).forEach(i -> {
            ProductDTO product = populateProductDTO(i);
            //  @formatter:off
            ProductDTO savedProduct = webClient.post()
                    .uri(productResourcePath)
                    .body(Mono.just(product), ProductDTO.class)
                    .retrieve()
                    .bodyToMono(ProductDTO.class)
                    .block();
            savedProducts.add(savedProduct.getId());
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

    public List<Long> createCustomers(int port, int noOfItems) {
        List<Long> savedCustomers = new ArrayList<>();
        WebClient webClient = WebClient.create("http://localhost:" + port);
        IntStream.range(0, noOfItems).forEach(i -> {
            CustomerDTO customer = populateCustomerDTO(i);
            //  @formatter:off
            CustomerDTO savedCustomer = webClient.post()
                    .uri(customerResourcePath)
                    .body(Mono.just(customer), CustomerDTO.class)
                    .retrieve()
                    .bodyToMono(CustomerDTO.class)
                    .block();
            savedCustomers.add(savedCustomer.getId());
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

    public List<Long> createOrders(int port, int noOfItems) {
        List<Long> savedOrders = new ArrayList<>();
        WebClient webClient = WebClient.create("http://localhost:" + port);
        IntStream.range(0, noOfItems).forEach(i -> {
            OrderDTO order = populateOrderDTO(i);
            //  @formatter:off
            OrderDTO savedOrder = webClient.post()
                    .uri(orderResourcePath)
                    .body(Mono.just(order), OrderDTO.class)
                    .retrieve()
                    .bodyToMono(OrderDTO.class)
                    .block();

            savedOrders.add(savedOrder.getId());
            // @formatter:on
        });
        return savedOrders;
    }

    public Long createOrder(int port) {
        return createOrders(port, 1).get(0);
    }

    public OrderDTO populateOrderDTO(int i) {
        CustomerDTO customer = getAnyCustomer();
        ProductDTO product = getAnyProduct();
        OrderDTO order = new OrderDTO();
        order.setCustomerId(customer.getId());
        order.setOrderDate(new Date());
        order.setShippedDate(new Date(LocalDate.now().plusDays(10).toEpochDay()));
        order.setStatus("ORDERED");
        OrderDetailDTO detail = new OrderDetailDTO();
        detail.setProductId(product.getId());
        detail.setQuantity(2);
        detail.setUnitPrice(product.getUnitPrice());
        order.setDetails(new HashSet<>());
        order.getDetails().add(detail);
        return order;
    }

    /**
     * @return
     */
    public ProductDTO getAnyProduct() {
        return productService.getProduct(savedProducts.get(RandomUtils.nextInt(0, savedProducts.size())));
    }

    /**
     * @param product
     */
    public void removeProduct(ProductDTO product) {
        savedProducts.remove(product.getId());
    }

    /**
     * @return
     */
    public CustomerDTO getAnyCustomer() {
        return customerService.getCustomer(savedCustomers.get(RandomUtils.nextInt(0, savedCustomers.size())));
    }

    /**
     * @param customer
     */
    public void removeCustomer(CustomerDTO customer) {
        savedCustomers.remove(customer.getId());
    }

    /**
     * @return
     */
    public OrderDTO getAnyOrder() {
        Long orderId = savedOrders.get(RandomUtils.nextInt(0, savedOrders.size()));
        savedOrders.remove(orderId);
        return orderService.getOrder(orderId);
    }

    /**
     * @param order
     */
    public void removeOrder(OrderDTO order) {
        savedOrders.remove(order.getId());
    }

}
