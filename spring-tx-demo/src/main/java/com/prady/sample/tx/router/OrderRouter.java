/**
 *
 */

package com.prady.sample.tx.router;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.RouterFunctions;
import org.springframework.web.servlet.function.ServerResponse;

import com.prady.sample.tx.handler.OrderHandler;

/**
 * @author Prady
 *
 */
@Configuration
public class OrderRouter {

    private static final String URL_ID = "/{id}";
    @Bean
    public RouterFunction<ServerResponse> orderRoutes(OrderHandler orderHandler) {
        // @formatter:off
        return RouterFunctions.route()
                .path("/orders", builder -> builder
                        .GET("", orderHandler::getOrders)
                        .GET(URL_ID, orderHandler::getOrder)
                        .POST("", orderHandler::createOrder)
                        .PUT(URL_ID, orderHandler::updateOrder)
                        .DELETE(URL_ID, orderHandler::deleteOrder))
                .build();
        // @formatter:on
    }
}
