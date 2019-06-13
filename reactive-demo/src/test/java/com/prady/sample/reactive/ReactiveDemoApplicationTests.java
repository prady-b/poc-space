
package com.prady.sample.reactive;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

import org.apache.commons.lang3.RandomUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.reactivestreams.Subscription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.client.WebClient;

import com.prady.sample.reactive.dto.UserAccountDTO;

import reactor.core.publisher.BaseSubscriber;
import reactor.core.publisher.Mono;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestInstance(Lifecycle.PER_CLASS)
class ReactiveDemoApplicationTests {

    private static final Logger log = LoggerFactory.getLogger(ReactiveDemoApplicationTests.class);
    private static final String EX_CODE_PATH = "$.code";

    private WebClient webClient;

    @LocalServerPort
    private int port;

    @Value("${user.resource.path:/users}")
    private String resourcePath;

    @Autowired
    private WebTestClient webTestClient;

    private List<UserAccountDTO> savedUsers = new ArrayList<>();



    @BeforeAll
    private void createUsers() throws InterruptedException {
        webClient = WebClient.create("http://localhost:" + port);

        IntStream.range(0, 5).forEach(i -> {
            UserAccountDTO user = populateUserAccountDTO(i);
            //  @formatter:off
            webClient.post()
            .uri(resourcePath)
            .body(Mono.just(user), UserAccountDTO.class)
            .retrieve()
            .bodyToMono(UserAccountDTO.class)
            .subscribe(u -> savedUsers.add(u));
            // @formatter:on
        });
        TimeUnit.SECONDS.sleep(1);
    }

    @Test
    public void testCreateUsers() {
        UserAccountDTO user = populateUserAccountDTO(10001);
        //  @formatter:off
        webTestClient.post()
        .uri(resourcePath)
        .body(Mono.just(user), UserAccountDTO.class)
        .exchange()
        .expectStatus()
        .isOk()
        .expectBody()
        .jsonPath("$.id").isNotEmpty()
        .jsonPath("$.userLoginName").isEqualTo(user.getUserLoginName());
        // @formatter:on
    }

    @Test
    public void testCreateUsersWithError() {
        UserAccountDTO user = populateUserAccountDTO(10001);
        user.setUserLoginName(null);
        //  @formatter:off
        webTestClient.post()
        .uri(resourcePath)
        .body(Mono.just(user), UserAccountDTO.class)
        .exchange()
        .expectStatus()
        .isBadRequest()
        .expectBody()
        .jsonPath(EX_CODE_PATH).isEqualTo("VALIDATION")
        .jsonPath("$.validationViolations[0].fieldName").isEqualTo("userLoginName");
        // @formatter:on
    }

    @Test
    public void testCreateUsersWithAlreadyExistsError() {
        UserAccountDTO user = populateUserAccountDTO(10001);
        Optional<UserAccountDTO> userOp = savedUsers.stream().findAny();
        UserAccountDTO existingUser = userOp.isPresent() ? userOp.get() : new UserAccountDTO();
        user.setUserLoginName(existingUser.getUserLoginName());
        //  @formatter:off
        webTestClient.post()
        .uri(resourcePath)
        .body(Mono.just(user), UserAccountDTO.class)
        .exchange()
        .expectStatus()
        .isBadRequest()
        .expectBody()
        .jsonPath(EX_CODE_PATH).isEqualTo("ALREADY_EXISTS");
        // @formatter:on
    }

    @Test
    public void testUpdateUsers() {
        Optional<UserAccountDTO> userOp = savedUsers.stream().findAny();
        UserAccountDTO user = userOp.isPresent() ? userOp.get() : new UserAccountDTO();
        user.setLastName("Updated");
        user.setPassword("NewPassword");
        //  @formatter:off
        webTestClient.put()
        .uri(resourcePath + "/" + user.getId())
        .body(Mono.just(user), UserAccountDTO.class)
        .exchange()
        .expectStatus()
        .isOk()
        .expectBody()
        .jsonPath("$.id").isNotEmpty()
        .jsonPath("$.lastName").isEqualTo("Updated");
        // @formatter:on
    }

    @Test
    public void testUpdateUsersWithValidationError() {
        Optional<UserAccountDTO> userOp = savedUsers.stream().findAny();
        UserAccountDTO user = userOp.isPresent() ? userOp.get() : new UserAccountDTO();
        user.setLastName(null);
        //  @formatter:off
        webTestClient.put()
        .uri(resourcePath + "/" + user.getId())
        .body(Mono.just(user), UserAccountDTO.class)
        .exchange()
        .expectStatus()
        .isBadRequest()
        .expectBody()
        .jsonPath(EX_CODE_PATH).isEqualTo("VALIDATION")
        .jsonPath("$.validationViolations[0].fieldName").isEqualTo("lastName");
        // @formatter:on
    }

    @Test
    public void testUpdateUsersNotFound() {
        Optional<UserAccountDTO> userOp = savedUsers.stream().findAny();
        UserAccountDTO user = userOp.isPresent() ? userOp.get() : new UserAccountDTO();
        //  @formatter:off
        webTestClient.put()
        .uri(resourcePath + "/1111111")
        .body(Mono.just(user), UserAccountDTO.class)
        .exchange()
        .expectStatus()
        .isNotFound();
        // @formatter:on
    }


    @Test
    public void testGetAllUsers() throws InterruptedException {
        //  @formatter:off
        webClient.get()
        .uri(resourcePath)
        .retrieve()
        .bodyToFlux(UserAccountDTO.class)
        .subscribe(new BaseSubscriber<UserAccountDTO>() {
            @Override
            protected void hookOnSubscribe(Subscription subscription) {
                subscription.request(3);
            }

            @Override
            protected void hookOnNext(UserAccountDTO user) {
                log.debug("User Id {}; Name : {}", user.getId(), user.getFirstName());
                request(1);
            }
        });
        TimeUnit.SECONDS.sleep(2);
        //  @formatter:on
    }

    @Test
    public void testGetUser() {
        Optional<UserAccountDTO> userOp = savedUsers.stream().findAny();
        UserAccountDTO user = userOp.isPresent() ? userOp.get() : new UserAccountDTO();
        //  @formatter:off
        webTestClient.get()
        .uri(resourcePath + "/" + user.getId())
        .exchange()
        .expectStatus()
        .isOk()
        .expectBody()
        .jsonPath("$.id").isNotEmpty()
        .jsonPath("$.firstName").isEqualTo(user.getFirstName());
        //  @formatter:on
    }

    @Test
    public void testDeleteUser() {
        Optional<UserAccountDTO> userOp = savedUsers.stream().findAny();
        UserAccountDTO user = userOp.isPresent() ? userOp.get() : new UserAccountDTO();
        //  @formatter:off
        webTestClient.delete()
        .uri(resourcePath + "/" + user.getId())
        .exchange()
        .expectStatus()
        .isOk();
        savedUsers.remove(user);
        //  @formatter:on
    }

    @Test
    public void testDeleteUserNotFound() {
        //  @formatter:off
        webTestClient.delete()
        .uri(resourcePath + "/1111111111")
        .exchange()
        .expectStatus()
        .isNotFound();
        //  @formatter:on
    }

    private UserAccountDTO populateUserAccountDTO(int i) {
        UserAccountDTO user = new UserAccountDTO();
        user.setUserLoginName("Login Name " + i + ":" + RandomUtils.nextLong(0, 1000));
        user.setFirstName("First Name " + RandomUtils.nextLong(0, 1000));
        user.setLastName("Last Name " + RandomUtils.nextLong(0, 1000));
        user.setPassword("Password " + RandomUtils.nextLong(0, 1000));

        return user;
    }

}
