/**
 *
 */
package com.prady.sample.tx.handler;

import java.io.IOException;

import javax.servlet.ServletException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.function.ServerRequest;
import org.springframework.web.servlet.function.ServerResponse;

import com.prady.sample.tx.dto.ProductDTO;
import com.prady.sample.tx.service.ProductService;

/**
 * @author Prady
 *
 */
@Component
public class ProductHandler {

    @Autowired
    private ProductService productService;

    /**
     * @param request This param may be use for further computation
     * @return
     */
    public ServerResponse getProducts(ServerRequest request) {
        return ServerResponse.ok().body(productService.getProducts());
    }

    /**
     * @param request
     * @return
     */
    public ServerResponse getProduct(ServerRequest request) {
        return ServerResponse.ok().body(productService.getProduct(Long.parseLong(request.pathVariable("id"))));
    }

    /**
     * @param request
     * @return
     * @throws IOException
     * @throws ServletException
     */
    public ServerResponse createProduct(ServerRequest request) throws ServletException, IOException {
        return ServerResponse.ok().body(productService.create(request.body(ProductDTO.class)));
    }

    /**
     * @param request
     * @return
     * @throws IOException
     * @throws ServletException
     * @throws NumberFormatException
     */
    public ServerResponse updateProduct(ServerRequest request) throws ServletException, IOException {
        return ServerResponse.ok().body(productService.update(Long.parseLong(request.pathVariable("id")), request.body(ProductDTO.class)));
    }

    /**
     * @param request
     * @return
     */
    public ServerResponse deleteProduct(ServerRequest request) {
        productService.delete(Long.parseLong(request.pathVariable("id")));
        return ServerResponse.ok().build();
    }

}
