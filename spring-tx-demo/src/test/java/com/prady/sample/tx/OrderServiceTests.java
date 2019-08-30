/**
 *
 */
package com.prady.sample.tx;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.prady.sample.tx.dto.OrderDTO;
import com.prady.sample.tx.dto.OrderDetailDTO;
import com.prady.sample.tx.dto.ProductDTO;
import com.prady.sample.tx.helper.DataStoreHelper;
import com.prady.sample.tx.service.ProductService;

import reactor.core.publisher.Mono;

/**
 * @author Prady
 *
 */
public class OrderServiceTests extends BaseTests {

    private static final String EX_CODE_PATH = "$.code";

    @Autowired
    private DataStoreHelper storeHelper;

    @Autowired
    private ProductService productService;

    @Test
    public void testGetOrders() throws InterruptedException {
        //  @formatter:off
        webTestClient.get()
        .uri(orderResourcePath)
        .exchange()
        .expectStatus()
        .isOk()
        .expectBody()
        .jsonPath("$").isArray();
        //  @formatter:on
    }

    @Test
    public void testGetOrder() {
        OrderDTO order = storeHelper.getAnyOrder();
        // @formatter:off
        webTestClient.get()
        .uri(orderResourcePath + "/" + order.getId())
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
        OrderDTO order = storeHelper.populateOrderDTO(10001);
        int productUnitsinStock = 0;
        for (OrderDetailDTO detail : order.getDetails()) {
            ProductDTO productDTO = productService.getProduct(detail.getProductId());
            productUnitsinStock = productDTO.getUnitsInStock();
        }
        int excepted = productUnitsinStock - order.getDetails().iterator().next().getQuantity();
        // @formatter:off
        webTestClient.post()
        .uri(orderResourcePath)
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
        OrderDTO order = storeHelper.populateOrderDTO(10001);
        for (OrderDetailDTO detail : order.getDetails()) {
            ProductDTO productDTO = productService.getProduct(detail.getProductId());
            productDTO.setUnitsInStock(5);
            productService.update(productDTO.getId(), productDTO);
            detail.setQuantity(10);
        }
        // @formatter:off
        webTestClient.post()
        .uri(orderResourcePath)
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
        OrderDTO order = storeHelper.populateOrderDTO(10001);
        order.setCustomerId(null);
        //  @formatter:off
        webTestClient.post()
        .uri(orderResourcePath)
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
        OrderDTO order = storeHelper.getAnyOrder();
        order.setStatus("SHIPPED");
        //  @formatter:off
        webTestClient.put()
        .uri(orderResourcePath + "/" + order.getId())
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
        OrderDTO order = storeHelper.getAnyOrder();
        order.setCustomerId(null);
        //  @formatter:off
        webTestClient.put()
        .uri(orderResourcePath + "/" + order.getId())
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
        OrderDTO order = storeHelper.getAnyOrder();
        //  @formatter:off
        webTestClient.put()
        .uri(orderResourcePath + "/1111111")
        .body(Mono.just(order), OrderDTO.class)
        .exchange()
        .expectStatus()
        .isNotFound();
        // @formatter:on
    }

    @Test
    public void testDeleteOrder() {
        OrderDTO order = storeHelper.getAnyOrder();
        //  @formatter:off
        webTestClient.delete()
        .uri(orderResourcePath + "/" + order.getId())
        .exchange()
        .expectStatus()
        .isOk();
        storeHelper.removeOrder(order);
        //  @formatter:on
    }

    @Test
    public void testDeleteOrderNotFound() {
        //  @formatter:off
        webTestClient.delete()
        .uri(orderResourcePath + "/1111111111")
        .exchange()
        .expectStatus()
        .isNotFound();
        //  @formatter:on
    }
}
