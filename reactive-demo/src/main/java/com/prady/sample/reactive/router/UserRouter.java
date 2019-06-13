/**
 *
 */

package com.prady.sample.reactive.router;

import static org.springframework.web.reactive.function.server.RouterFunctions.route;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.prady.sample.reactive.handler.UserHandler;

/**
 * @author Prady
 */
@Configuration
public class UserRouter {

    private static final String URL_ID = "/{id}";

    @Bean
    public RouterFunction<ServerResponse> userRoutes(UserHandler userHandler) {
        // @formatter:off
        return route()
                .path("/users", builder -> builder
                        .GET("", userHandler::getUsers)
                        .GET(URL_ID, userHandler::getUser)
                        .POST("", userHandler::createUser)
                        .PUT(URL_ID, userHandler::updateUser)
                        .DELETE(URL_ID, userHandler::deleteUser))
                .build();
        // @formatter:on
    }
}
