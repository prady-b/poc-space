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

import com.prady.sample.tx.dto.OrderDTO;
import com.prady.sample.tx.service.OrderService;

/**
 * @author Prady
 *
 */
@Component
public class OrderHandler {

    @Autowired
    private OrderService orderService;

    /**
     * @param request This param may be use for further computation
     * @return
     */
    public ServerResponse getOrders(ServerRequest request) {
        return ServerResponse.ok().body(orderService.getOrders());
    }

    /**
     * @param request
     * @return
     */
    public ServerResponse getOrder(ServerRequest request) {
        return ServerResponse.ok().body(orderService.getOrder(Long.parseLong(request.pathVariable("id"))));
    }

    /**
     * @param request
     * @return
     * @throws IOException
     * @throws ServletException
     */
    public ServerResponse createOrder(ServerRequest request) throws ServletException, IOException {
        return ServerResponse.ok().body(orderService.create(request.body(OrderDTO.class)));
    }

    /**
     * @param request
     * @return
     * @throws IOException
     * @throws ServletException
     * @throws NumberFormatException
     */
    public ServerResponse updateOrder(ServerRequest request) throws ServletException, IOException {
        return ServerResponse.ok().body(orderService.update(Long.parseLong(request.pathVariable("id")), request.body(OrderDTO.class)));
    }

    /**
     * @param request
     * @return
     */
    public ServerResponse deleteOrder(ServerRequest request) {
        orderService.delete(Long.parseLong(request.pathVariable("id")));
        return ServerResponse.ok().build();
    }

}
