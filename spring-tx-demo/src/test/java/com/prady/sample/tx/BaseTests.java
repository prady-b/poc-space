/**
 *
 */

package com.prady.sample.tx;

import java.lang.reflect.Method;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ExtensionContext.Namespace;
import org.junit.jupiter.api.extension.ExtensionContext.Store;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.env.Environment;
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
@ExtendWith(BaseTests.class)
public class BaseTests implements BeforeEachCallback, AfterEachCallback {

    protected final Logger log = LoggerFactory.getLogger(getClass());

    protected WebClient webClient;

    @LocalServerPort
    protected int port;

    @Autowired
    protected WebTestClient webTestClient;

    @Autowired
    protected Environment environment;

    @Value("${product.resource.path:/products}")
    protected String productResourcePath;
    @Value("${customer.resource.path:/customers}")
    protected String customerResourcePath;
    @Value("${order.resource.path:/orders}")
    protected String orderResourcePath;

    private static Boolean initialized = Boolean.FALSE;
    @Autowired
    protected DataStoreHelper dataStoreHelper;

    @BeforeAll
    public void setupData() {
        if (!BaseTests.initialized && isInitialDataRequired()) {
            dataStoreHelper.createInitialData(port);
            BaseTests.initialized = Boolean.TRUE;
        }
    }

    protected Boolean isInitialDataRequired() {
        return Boolean.TRUE;
    }

    private Store getStore(ExtensionContext context) {
        return context.getStore(Namespace.create(getClass(), context.getRequiredTestMethod()));
    }

    @Override
    public void afterEach(ExtensionContext context) throws Exception {
        getStore(context).put("THREAD_NAME", Thread.currentThread().getName());
        Thread.currentThread().setName((String) getStore(context).get("THREAD_NAME"));
    }

    @Override
    public void beforeEach(ExtensionContext context) throws Exception {
        Method testMethod = context.getRequiredTestMethod();
        getStore(context).put("THREAD_NAME", Thread.currentThread().getName());
        Thread.currentThread().setName(testMethod.getName());
    }

}
