/**
 *
 */

package com.prady.sample.tx.router;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.RouterFunctions;
import org.springframework.web.servlet.function.ServerResponse;

import com.prady.sample.tx.handler.ProductHandler;

/**
 * @author Prady
 *
 */
@Configuration
public class ProductRouter {

    private static final String URL_ID = "/{id}";
    @Bean
    public RouterFunction<ServerResponse> productRoutes(ProductHandler productHandler) {
        // @formatter:off
        return RouterFunctions.route()
                .path("/products", builder -> builder
                        .GET("", productHandler::getProducts)
                        .GET(URL_ID, productHandler::getProduct)
                        .POST("", productHandler::createProduct)
                        .PUT(URL_ID, productHandler::updateProduct)
                        .DELETE(URL_ID, productHandler::deleteProduct))
                .build();
        // @formatter:on
    }
}
