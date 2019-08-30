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

import com.prady.sample.tx.dto.CustomerDTO;
import com.prady.sample.tx.helper.Helper;

import reactor.core.publisher.Mono;

/**
 * @author Prady
 *
 */
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestInstance(Lifecycle.PER_CLASS)
public class CustomerServiceTests {

    private static final Logger log = LoggerFactory.getLogger(CustomerServiceTests.class);
    private static final String EX_CODE_PATH = "$.code";

    @LocalServerPort
    private int port;

    @Autowired
    private WebTestClient webTestClient;

    @Value("${customer.resource.path:/customers}")
    private String resourcePath;

    private List<CustomerDTO> savedCustomers = new ArrayList<>();

    @BeforeAll
    public void createCustomers() throws InterruptedException {
        savedCustomers.addAll(Helper.createCustomers(port, resourcePath, 5));
        log.info("Created Test Customers");
    }

    @Test
    public void testGetCustomers() throws InterruptedException {
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
    public void testGetCustomer() {
        Optional<CustomerDTO> customerOp = savedCustomers.stream().findAny();
        CustomerDTO customer = customerOp.isPresent() ? customerOp.get() : new CustomerDTO();
        //  @formatter:off
        webTestClient.get()
        .uri(resourcePath + "/" + customer.getId())
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
        CustomerDTO customer = Helper.populateCustomerDTO(10001);
        //  @formatter:off
        webTestClient.post()
        .uri(resourcePath)
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
        CustomerDTO customer = Helper.populateCustomerDTO(10001);
        customer.setFirstName("Test");
        //  @formatter:off
        webTestClient.post()
        .uri(resourcePath)
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
        CustomerDTO customer = Helper.populateCustomerDTO(10001);
        Optional<CustomerDTO> customerOp = savedCustomers.stream().filter(c -> c.getFirstName() != null).findAny();
        CustomerDTO existingCustomer = customerOp.isPresent() ? customerOp.get() : new CustomerDTO();
        customer.setFirstName(existingCustomer.getFirstName());
        customer.setLastName(existingCustomer.getLastName());
        //  @formatter:off
        webTestClient.post()
        .uri(resourcePath)
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
        Optional<CustomerDTO> customerOp = savedCustomers.stream().filter(customer -> customer.getFirstName() != null).findAny();
        CustomerDTO customer = customerOp.isPresent() ? customerOp.get() : new CustomerDTO();
        customer.setTitle("New Title");
        //  @formatter:off
        webTestClient.put()
        .uri(resourcePath + "/" + customer.getId())
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
        Optional<CustomerDTO> customerOp = savedCustomers.stream().filter(customer -> customer.getFirstName() != null).findAny();
        CustomerDTO customer = customerOp.isPresent() ? customerOp.get() : new CustomerDTO();
        customer.setFirstName(null);
        //  @formatter:off
        webTestClient.put()
        .uri(resourcePath + "/" + customer.getId())
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
    public void testUpdateCustomerNotFound() {
        Optional<CustomerDTO> customerOp = savedCustomers.stream().findAny();
        CustomerDTO customer = customerOp.isPresent() ? customerOp.get() : new CustomerDTO();
        //  @formatter:off
        webTestClient.put()
        .uri(resourcePath + "/1111111")
        .body(Mono.just(customer), CustomerDTO.class)
        .exchange()
        .expectStatus()
        .isNotFound();
        // @formatter:on
    }

    @Test
    public void testDeleteCustomer() {
        Optional<CustomerDTO> customerOp = savedCustomers.stream().findAny();
        CustomerDTO customer = customerOp.isPresent() ? customerOp.get() : new CustomerDTO();
        //  @formatter:off
        webTestClient.delete()
        .uri(resourcePath + "/" + customer.getId())
        .exchange()
        .expectStatus()
        .isOk();
        savedCustomers.remove(customer);
        //  @formatter:on
    }

    @Test
    public void testDeleteCustomerNotFound() {
        //  @formatter:off
        webTestClient.delete()
        .uri(resourcePath + "/1111111111")
        .exchange()
        .expectStatus()
        .isNotFound();
        //  @formatter:on
    }
}
