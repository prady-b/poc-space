/**
 *
 */
package com.prady.sample.tx;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.prady.sample.tx.dto.ProductDTO;
import com.prady.sample.tx.helper.Helper;

import reactor.core.publisher.Mono;

/**
 * @author Prady
 *
 */
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestInstance(Lifecycle.PER_CLASS)
public class ProductServiceTests {

    private static final Logger log = LoggerFactory.getLogger(ProductServiceTests.class);
    private static final String EX_CODE_PATH = "$.code";

    @LocalServerPort
    private int port;

    @Autowired
    private WebTestClient webTestClient;

    @Value("${product.resource.path:/products}")
    private String resourcePath;

    private List<ProductDTO> savedProducts = new ArrayList<>();

    @BeforeAll
    public void createProducts() throws InterruptedException {
        savedProducts.addAll(Helper.createProducts(port, resourcePath, 5));
        log.info("Created Test Products");
    }

    @Test
    public void testGetProducts() throws InterruptedException {
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
    public void testGetProduct() {
        Optional<ProductDTO> productOp = savedProducts.stream().findAny();
        ProductDTO product = productOp.isPresent() ? productOp.get() : new ProductDTO();
        //  @formatter:off
        webTestClient.get()
        .uri(resourcePath + "/" + product.getId())
        .exchange()
        .expectStatus()
        .isOk()
        .expectBody()
        .jsonPath("$.id").isNotEmpty()
        .jsonPath("$.productName").isEqualTo(product.getProductName());
        //  @formatter:on
    }

    @Test
    public void testCreateProduct() {
        ProductDTO product = Helper.populateProductDTO(10001);
        //  @formatter:off
        webTestClient.post()
        .uri(resourcePath)
        .body(Mono.just(product), ProductDTO.class)
        .exchange()
        .expectStatus()
        .isOk()
        .expectBody()
        .jsonPath("$.id").isNotEmpty()
        .jsonPath("$.productName").isEqualTo(product.getProductName());
        // @formatter:on
    }

    @Test
    public void testCreateproductWithError() {
        ProductDTO product = Helper.populateProductDTO(10001);
        product.setProductName("Test");
        //  @formatter:off
        webTestClient.post()
        .uri(resourcePath)
        .body(Mono.just(product), ProductDTO.class)
        .exchange()
        .expectStatus()
        .isBadRequest()
        .expectBody()
        .jsonPath(EX_CODE_PATH).isEqualTo("VALIDATION")
        .jsonPath("$.validationViolations[0].fieldName").isEqualTo("productName");
        // @formatter:on
    }

    @Test
    public void testCreateproductWithAlreadyExistsError() {
        ProductDTO product = Helper.populateProductDTO(10001);
        Optional<ProductDTO> productOp = savedProducts.stream().findAny();
        ProductDTO existingProduct = productOp.isPresent() ? productOp.get() : new ProductDTO();
        product.setProductName(existingProduct.getProductName());
        product.setProductCode(existingProduct.getProductCode());
        //  @formatter:off
        webTestClient.post()
        .uri(resourcePath)
        .body(Mono.just(product), ProductDTO.class)
        .exchange()
        .expectStatus()
        .isBadRequest()
        .expectBody()
        .jsonPath(EX_CODE_PATH).isEqualTo("ALREADY_EXISTS");
        // @formatter:on
    }

    @Test
    public void testUpdateproduct() {
        Optional<ProductDTO> productOp = savedProducts.stream().findAny();
        ProductDTO product = productOp.isPresent() ? productOp.get() : new ProductDTO();
        product.setUnitsInStock(5);
        //  @formatter:off
        webTestClient.put()
        .uri(resourcePath + "/" + product.getId())
        .body(Mono.just(product), ProductDTO.class)
        .exchange()
        .expectStatus()
        .isOk()
        .expectBody()
        .jsonPath("$.id").isNotEmpty()
        .jsonPath("$.unitsInStock").isEqualTo("5");
        // @formatter:on
    }

    @Test
    public void testUpdateProductWithValidationError() {
        Optional<ProductDTO> productOp = savedProducts.stream().findAny();
        ProductDTO product = productOp.isPresent() ? productOp.get() : new ProductDTO();
        product.setProductName(null);
        //  @formatter:off
        webTestClient.put()
        .uri(resourcePath + "/" + product.getId())
        .body(Mono.just(product), ProductDTO.class)
        .exchange()
        .expectStatus()
        .isBadRequest()
        .expectBody()
        .jsonPath(EX_CODE_PATH).isEqualTo("VALIDATION")
        .jsonPath("$.validationViolations[0].fieldName").isEqualTo("productName");
        // @formatter:on
    }

    @Test
    public void testUpdateProductNotFound() {
        Optional<ProductDTO> productOp = savedProducts.stream().findAny();
        ProductDTO product = productOp.isPresent() ? productOp.get() : new ProductDTO();
        //  @formatter:off
        webTestClient.put()
        .uri(resourcePath + "/1111111")
        .body(Mono.just(product), ProductDTO.class)
        .exchange()
        .expectStatus()
        .isNotFound();
        // @formatter:on
    }

    @Test
    public void testDeleteProduct() {
        Optional<ProductDTO> productOp = savedProducts.stream().findAny();
        ProductDTO product = productOp.isPresent() ? productOp.get() : new ProductDTO();
        //  @formatter:off
        webTestClient.delete()
        .uri(resourcePath + "/" + product.getId())
        .exchange()
        .expectStatus()
        .isOk();
        savedProducts.remove(product);
        //  @formatter:on
    }

    @Test
    public void testDeleteProductNotFound() {
        //  @formatter:off
        webTestClient.delete()
        .uri(resourcePath + "/1111111111")
        .exchange()
        .expectStatus()
        .isNotFound();
        //  @formatter:on
    }
}
