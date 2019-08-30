/**
 *
 */
package com.prady.sample.tx;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.prady.sample.tx.dto.CustomerDTO;
import com.prady.sample.tx.helper.DataStoreHelper;

import reactor.core.publisher.Mono;

/**
 * @author Prady
 *
 */
public class CustomerServiceTests extends BaseTests {

    private static final String EX_CODE_PATH = "$.code";

    @Autowired
    private DataStoreHelper storeHelper;

    @Test
    public void testGetCustomers() throws InterruptedException {
        //  @formatter:off
        webTestClient.get()
        .uri(customerResourcePath)
        .exchange()
        .expectStatus()
        .isOk()
        .expectBody()
        .jsonPath("$").isArray();
        //  @formatter:on
    }

    @Test
    public void testGetCustomer() {
        CustomerDTO customer = storeHelper.getAnyCustomer();
        //  @formatter:off
        webTestClient.get()
        .uri(customerResourcePath + "/" + customer.getId())
        .exchange()
        .expectStatus()
        .isOk()
        .expectBody()
        .jsonPath("$.id").isNotEmpty()
        .jsonPath("$.firstName").isEqualTo(customer.getFirstName());
        //  @formatter:on
    }

    @Test
    public void testCreateCustomer() {
        CustomerDTO customer = storeHelper.populateCustomerDTO(10001);
        //  @formatter:off
        webTestClient.post()
        .uri(customerResourcePath)
        .body(Mono.just(customer), CustomerDTO.class)
        .exchange()
        .expectStatus()
        .isOk()
        .expectBody()
        .jsonPath("$.id").isNotEmpty()
        .jsonPath("$.firstName").isEqualTo(customer.getFirstName());
        // @formatter:on
    }

    @Test
    public void testCreatecustomerWithError() {
        CustomerDTO customer = storeHelper.populateCustomerDTO(10001);
        customer.setFirstName("Test");
        //  @formatter:off
        webTestClient.post()
        .uri(customerResourcePath)
        .body(Mono.just(customer), CustomerDTO.class)
        .exchange()
        .expectStatus()
        .isBadRequest()
        .expectBody()
        .jsonPath(EX_CODE_PATH).isEqualTo("VALIDATION")
        .jsonPath("$.validationViolations[0].fieldName").isEqualTo("firstName");
        // @formatter:on
    }

    @Test
    public void testCreatecustomerWithAlreadyExistsError() {
        CustomerDTO customer = storeHelper.populateCustomerDTO(10001);
        CustomerDTO existingCustomer = storeHelper.getAnyCustomer();
        customer.setFirstName(existingCustomer.getFirstName());
        customer.setLastName(existingCustomer.getLastName());
        //  @formatter:off
        webTestClient.post()
        .uri(customerResourcePath)
        .body(Mono.just(customer), CustomerDTO.class)
        .exchange()
        .expectStatus()
        .isBadRequest()
        .expectBody()
        .jsonPath(EX_CODE_PATH).isEqualTo("ALREADY_EXISTS");
        // @formatter:on
    }

    @Test
    public void testUpdateCustomer() {
        CustomerDTO customer = storeHelper.getAnyCustomer();
        customer.setTitle("New Title");
        //  @formatter:off
        webTestClient.put()
        .uri(customerResourcePath + "/" + customer.getId())
        .body(Mono.just(customer), CustomerDTO.class)
        .exchange()
        .expectStatus()
        .isOk()
        .expectBody()
        .jsonPath("$.id").isNotEmpty()
        .jsonPath("$.title").isEqualTo("New Title");
        // @formatter:on
    }

    @Test
    public void testUpdateCustomerWithValidationError() {
        CustomerDTO customer = storeHelper.getAnyCustomer();
        String oldFirstName = customer.getFirstName();
        customer.setFirstName(null);
        //  @formatter:off
        webTestClient.put()
        .uri(customerResourcePath + "/" + customer.getId())
        .body(Mono.just(customer), CustomerDTO.class)
        .exchange()
        .expectStatus()
        .isBadRequest()
        .expectBody()
        .jsonPath(EX_CODE_PATH).isEqualTo("VALIDATION")
        .jsonPath("$.validationViolations[0].fieldName").isEqualTo("firstName");
        // @formatter:on
        customer.setFirstName(oldFirstName);
    }

    @Test
    public void testUpdateCustomerNotFound() {
        CustomerDTO customer = storeHelper.getAnyCustomer();
        //  @formatter:off
        webTestClient.put()
        .uri(customerResourcePath + "/1111111")
        .body(Mono.just(customer), CustomerDTO.class)
        .exchange()
        .expectStatus()
        .isNotFound();
        // @formatter:on
    }

    @Test
    public void testDeleteCustomer() {
        CustomerDTO customer = storeHelper.getAnyCustomer();
        //  @formatter:off
        webTestClient.delete()
        .uri(customerResourcePath + "/" + customer.getId())
        .exchange()
        .expectStatus()
        .isOk();
        storeHelper.removeCustomer(customer);
        //  @formatter:on
    }

    @Test
    public void testDeleteCustomerNotFound() {
        //  @formatter:off
        webTestClient.delete()
        .uri(customerResourcePath + "/1111111111")
        .exchange()
        .expectStatus()
        .isNotFound();
        //  @formatter:on
    }
}
