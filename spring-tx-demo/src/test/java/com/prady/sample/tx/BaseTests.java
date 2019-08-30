/**
 *
 */

package com.prady.sample.tx;

import org.junit.jupiter.api.BeforeAll;
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

import com.prady.sample.tx.helper.DataStoreHelper;

/**
 * @author Prady
 *
 */
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient(timeout = "100000")
@TestInstance(Lifecycle.PER_CLASS)
public class BaseTests {

    protected final Logger log = LoggerFactory.getLogger(getClass());

    protected WebClient webClient;

    @LocalServerPort
    protected int port;

    @Autowired
    protected WebTestClient webTestClient;

    @Value("${product.resource.path:/products}")
    protected String productResourcePath;
    @Value("${customer.resource.path:/customers}")
    protected String customerResourcePath;
    @Value("${order.resource.path:/orders}")
    protected String orderResourcePath;

    private static Boolean initialized = Boolean.FALSE;
    @Autowired
    private DataStoreHelper dataStoreHelper;

    @BeforeAll
    public void setupData() {
        if (!BaseTests.initialized) {
            dataStoreHelper.createInitialData(port);
            BaseTests.initialized = Boolean.TRUE;
        }
    }

}
