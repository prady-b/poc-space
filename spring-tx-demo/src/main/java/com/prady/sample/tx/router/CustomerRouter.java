/**
 *
 */

package com.prady.sample.tx.router;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.RouterFunctions;
import org.springframework.web.servlet.function.ServerResponse;

import com.prady.sample.tx.handler.CustomerHandler;

/**
 * @author Prady
 *
 */
@Configuration
public class CustomerRouter {

    private static final String URL_ID = "/{id}";
    @Bean
    public RouterFunction<ServerResponse> customerRoutes(CustomerHandler customerHandler) {
        // @formatter:off
        return RouterFunctions.route()
                .path("/customers", builder -> builder
                        .GET("", customerHandler::getCustomers)
                        .GET(URL_ID, customerHandler::getCustomer)
                        .POST("", customerHandler::createCustomer)
                        .PUT(URL_ID, customerHandler::updateCustomer)
                        .DELETE(URL_ID, customerHandler::deleteCustomer))
                .build();
        // @formatter:on
    }
}
