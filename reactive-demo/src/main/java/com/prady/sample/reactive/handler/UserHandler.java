/**
 *
 */

package com.prady.sample.reactive.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.prady.sample.reactive.dto.UserAccountDTO;
import com.prady.sample.reactive.service.UserService;

import reactor.core.publisher.Mono;

/**
 * @author Prady
 */
@Component
public class UserHandler {

    @Autowired
    private UserService userService;

    /**
     * @param request
     * @return
     */
    public Mono<ServerResponse> getUsers(ServerRequest request) {
        return ServerResponse.ok().body(userService.getUsers(), UserAccountDTO.class);
    }

    /**
     * @param request
     * @return
     */
    public Mono<ServerResponse> getUser(ServerRequest request) {
        return ServerResponse.ok().body(userService.getUser(request.pathVariable("id")), UserAccountDTO.class);
    }

    /**
     * @param request
     * @return
     */
    public Mono<ServerResponse> createUser(ServerRequest request) {
        return ServerResponse.ok().body(userService.create(request.bodyToMono(UserAccountDTO.class)), UserAccountDTO.class);
    }

    /**
     * @param request
     * @return
     */
    public Mono<ServerResponse> updateUser(ServerRequest request) {
        return ServerResponse.ok().body(userService.update(request.pathVariable("id"), request.bodyToMono(UserAccountDTO.class)),
                UserAccountDTO.class);
    }

    /**
     * @param request
     * @return
     */
    public Mono<ServerResponse> deleteUser(ServerRequest request) {
        return ServerResponse.ok().body(userService.delete(request.pathVariable("id")), Void.class);
    }

}
