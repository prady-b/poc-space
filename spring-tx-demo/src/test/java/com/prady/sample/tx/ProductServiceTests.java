/**
 *
 */
package com.prady.sample.tx;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.prady.sample.tx.dto.ProductDTO;
import com.prady.sample.tx.helper.DataStoreHelper;

import reactor.core.publisher.Mono;

/**
 * @author Prady
 *
 */
public class ProductServiceTests extends BaseTests {

    private static final String EX_CODE_PATH = "$.code";

    @Autowired
    private DataStoreHelper storeHelper;

    @Test
    public void testGetProducts() throws InterruptedException {
        //  @formatter:off
        webTestClient.get()
        .uri(productResourcePath)
        .exchange()
        .expectStatus()
        .isOk()
        .expectBody()
        .jsonPath("$").isArray();
        //  @formatter:on
    }

    @Test
    public void testGetProduct() {
        ProductDTO product = storeHelper.getAnyProduct();
        //  @formatter:off
        webTestClient.get()
        .uri(productResourcePath + "/" + product.getId())
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
        ProductDTO product = storeHelper.populateProductDTO(10001);
        //  @formatter:off
        webTestClient.post()
        .uri(productResourcePath)
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
        ProductDTO product = storeHelper.populateProductDTO(10001);
        product.setProductName("Test");
        //  @formatter:off
        webTestClient.post()
        .uri(productResourcePath)
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
        ProductDTO product = storeHelper.populateProductDTO(10001);
        ProductDTO existingProduct = storeHelper.getAnyProduct();
        product.setProductName(existingProduct.getProductName());
        product.setProductCode(existingProduct.getProductCode());
        //  @formatter:off
        webTestClient.post()
        .uri(productResourcePath)
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
        ProductDTO product = storeHelper.getAnyProduct();
        product.setUnitsInStock(5);
        //  @formatter:off
        webTestClient.put()
        .uri(productResourcePath + "/" + product.getId())
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
        ProductDTO product = storeHelper.getAnyProduct();
        product.setProductName(null);
        //  @formatter:off
        webTestClient.put()
        .uri(productResourcePath + "/" + product.getId())
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
        ProductDTO product = storeHelper.getAnyProduct();
        //  @formatter:off
        webTestClient.put()
        .uri(productResourcePath + "/1111111")
        .body(Mono.just(product), ProductDTO.class)
        .exchange()
        .expectStatus()
        .isNotFound();
        // @formatter:on
    }

    @Test
    public void testDeleteProduct() {
        ProductDTO product = storeHelper.getAnyProduct();
        //  @formatter:off
        webTestClient.delete()
        .uri(productResourcePath + "/" + product.getId())
        .exchange()
        .expectStatus()
        .isOk();
        storeHelper.removeProduct(product);
        //  @formatter:on
    }

    @Test
    public void testDeleteProductNotFound() {
        //  @formatter:off
        webTestClient.delete()
        .uri(productResourcePath + "/1111111111")
        .exchange()
        .expectStatus()
        .isNotFound();
        //  @formatter:on
    }
}
