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

import com.prady.sample.tx.dto.CustomerDTO;
import com.prady.sample.tx.service.CustomerService;

/**
 * @author Prady
 *
 */
@Component
public class CustomerHandler {

    @Autowired
    private CustomerService customerService;

    /**
     * @param request This param may be use for further computation
     * @return
     */
    public ServerResponse getCustomers(ServerRequest request) {
        return ServerResponse.ok().body(customerService.getCustomers());
    }

    /**
     * @param request
     * @return
     */
    public ServerResponse getCustomer(ServerRequest request) {
        return ServerResponse.ok().body(customerService.getCustomer(Long.parseLong(request.pathVariable("id"))));
    }

    /**
     * @param request
     * @return
     * @throws IOException
     * @throws ServletException
     */
    public ServerResponse createCustomer(ServerRequest request) throws ServletException, IOException {
        return ServerResponse.ok().body(customerService.create(request.body(CustomerDTO.class)));
    }

    /**
     * @param request
     * @return
     * @throws IOException
     * @throws ServletException
     * @throws NumberFormatException
     */
    public ServerResponse updateCustomer(ServerRequest request) throws ServletException, IOException {
        return ServerResponse.ok()
                .body(customerService.update(Long.parseLong(request.pathVariable("id")), request.body(CustomerDTO.class)));
    }

    /**
     * @param request
     * @return
     */
    public ServerResponse deleteCustomer(ServerRequest request) {
        customerService.delete(Long.parseLong(request.pathVariable("id")));
        return ServerResponse.ok().build();
    }

}
