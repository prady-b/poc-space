/**
 *
 */
package com.prady.sample.tx;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.client.WebClient;

import com.prady.sample.tx.dto.CustomerDTO;
import com.prady.sample.tx.dto.OrderDTO;
import com.prady.sample.tx.dto.OrderDetailDTO;
import com.prady.sample.tx.dto.ProductDTO;
import com.prady.sample.tx.helper.Helper;
import com.prady.sample.tx.service.ProductService;

import reactor.core.publisher.Mono;

/**
 * @author Prady
 *
 */
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient(timeout = "100000")
@TestInstance(Lifecycle.PER_CLASS)
public class OrderServiceTests {

    private static final Logger log = LoggerFactory.getLogger(OrderServiceTests.class);
    private static final String EX_CODE_PATH = "$.code";

    private WebClient webClient;

    @LocalServerPort
    private int port;

    @Autowired
    private WebTestClient webTestClient;

    @Value("${product.resource.path:/products}")
    private String productResourcePath;
    @Value("${customer.resource.path:/customers}")
    private String customerResourcePath;
    @Value("${order.resource.path:/orders}")
    private String resourcePath;

    private List<ProductDTO> savedProducts = new ArrayList<>();
    private List<CustomerDTO> savedCustomers = new ArrayList<>();
    private List<OrderDTO> savedOrders = new ArrayList<>();

    @Autowired
    private ProductService productService;

    @BeforeAll
    public void createOrders() throws InterruptedException {
        savedProducts.addAll(Helper.createProducts(port, productResourcePath, 5));
        savedCustomers.addAll(Helper.createCustomers(port, customerResourcePath, 5));

        webClient = WebClient.create("http://localhost:" + port);
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
        log.info("Created Test Orders");
    }

    private OrderDTO populateOrderDTO(int i, Optional<ProductDTO> productOp) {
        Optional<CustomerDTO> customerOp = savedCustomers.stream().findAny();
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

    private OrderDTO populateOrderDTO(int i) {
        return populateOrderDTO(i, savedProducts.stream().findAny());
    }

    @Test
    public void testGetOrders() throws InterruptedException {
        //  @formatter:off
        webTestClient.get()
        .uri(resourcePath)
        .exchange()
        .expectStatus()
        .isOk()
        .expectBody()
        .jsonPath("$").isArray();
        //  @formatter:on
    }

    @Test
    public void testGetOrder() {
        Optional<OrderDTO> orderOp = savedOrders.stream().findAny();
        OrderDTO order = orderOp.isPresent() ? orderOp.get() : new OrderDTO();
        // @formatter:off
        webTestClient.get()
        .uri(resourcePath + "/" + order.getId())
        .exchange()
        .expectStatus()
        .isOk()
        .expectBody()
        .jsonPath("$.id").isNotEmpty()
        .jsonPath("$.status").isEqualTo(order.getStatus());
        //  @formatter:on
    }

    @Test
    public void testCreateOrder() {
        OrderDTO order = populateOrderDTO(10001);
        int productUnitsinStock = 0;
        for (OrderDetailDTO detail : order.getDetails()) {
            ProductDTO productDTO = productService.getProduct(detail.getProductId());
            productUnitsinStock = productDTO.getUnitsInStock();
        }
        int excepted = productUnitsinStock - order.getDetails().iterator().next().getQuantity();
        // @formatter:off
        webTestClient.post()
        .uri(resourcePath)
        .body(Mono.just(order), OrderDTO.class)
        .exchange()
        .expectStatus()
        .isOk()
        .expectBody()
        .jsonPath("$.id").isNotEmpty()
        .jsonPath("$.status").isEqualTo(order.getStatus());
        // @formatter:on

        for (OrderDetailDTO detail : order.getDetails()) {
            ProductDTO productDTO = productService.getProduct(detail.getProductId());
            Assertions.assertEquals(excepted, productDTO.getUnitsInStock());
        }
    }

    @Test
    public void testCreateOrderWithInsufficientResourcesException() {
        OrderDTO order = populateOrderDTO(10001);
        for (OrderDetailDTO detail : order.getDetails()) {
            ProductDTO productDTO = productService.getProduct(detail.getProductId());
            productDTO.setUnitsInStock(5);
            productService.update(productDTO.getId(), productDTO);
            detail.setQuantity(10);
        }
        // @formatter:off
        webTestClient.post()
        .uri(resourcePath)
        .body(Mono.just(order), OrderDTO.class)
        .exchange()
        .expectStatus()
        .isBadRequest()
        .expectBody()
        .jsonPath(EX_CODE_PATH).isEqualTo("INSUFFICIENT_RESOURCES");
        // @formatter:on

    }

    @Test
    public void testCreateOrderWithError() {
        OrderDTO order = populateOrderDTO(10001);
        order.setCustomerId(null);
        //  @formatter:off
        webTestClient.post()
        .uri(resourcePath)
        .body(Mono.just(order), OrderDTO.class)
        .exchange()
        .expectStatus()
        .isBadRequest()
        .expectBody()
        .jsonPath(EX_CODE_PATH).isEqualTo("VALIDATION")
        .jsonPath("$.validationViolations[0].fieldName").isEqualTo("customerId");
        // @formatter:on
    }

    @Test
    public void testUpdateOrder() {
        Optional<OrderDTO> orderOp = savedOrders.stream().findAny();
        OrderDTO order = orderOp.isPresent() ? orderOp.get() : new OrderDTO();
        order.setStatus("SHIPPED");
        //  @formatter:off
        webTestClient.put()
        .uri(resourcePath + "/" + order.getId())
        .body(Mono.just(order), OrderDTO.class)
        .exchange()
        .expectStatus()
        .isOk()
        .expectBody()
        .jsonPath("$.id").isNotEmpty()
        .jsonPath("$.status").isEqualTo("SHIPPED");
        // @formatter:on
    }

    @Test
    public void testUpdateOrderWithValidationError() {
        Optional<OrderDTO> orderOp = savedOrders.stream().findAny();
        OrderDTO order = orderOp.isPresent() ? orderOp.get() : new OrderDTO();
        order.setCustomerId(null);
        //  @formatter:off
        webTestClient.put()
        .uri(resourcePath + "/" + order.getId())
        .body(Mono.just(order), OrderDTO.class)
        .exchange()
        .expectStatus()
        .isBadRequest()
        .expectBody()
        .jsonPath(EX_CODE_PATH).isEqualTo("VALIDATION")
        .jsonPath("$.validationViolations[0].fieldName").isEqualTo("customerId");
        // @formatter:on
    }

    @Test
    public void testUpdateOrderNotFound() {
        Optional<OrderDTO> orderOp = savedOrders.stream().findAny();
        OrderDTO order = orderOp.isPresent() ? orderOp.get() : new OrderDTO();
        //  @formatter:off
        webTestClient.put()
        .uri(resourcePath + "/1111111")
        .body(Mono.just(order), OrderDTO.class)
        .exchange()
        .expectStatus()
        .isNotFound();
        // @formatter:on
    }

    @Test
    public void testDeleteOrder() {
        Optional<OrderDTO> orderOp = savedOrders.stream().findAny();
        OrderDTO order = orderOp.isPresent() ? orderOp.get() : new OrderDTO();
        //  @formatter:off
        webTestClient.delete()
        .uri(resourcePath + "/" + order.getId())
        .exchange()
        .expectStatus()
        .isOk();
        savedOrders.remove(order);
        //  @formatter:on
    }

    @Test
    public void testDeleteOrderNotFound() {
        //  @formatter:off
        webTestClient.delete()
        .uri(resourcePath + "/1111111111")
        .exchange()
        .expectStatus()
        .isNotFound();
        //  @formatter:on
    }
}
