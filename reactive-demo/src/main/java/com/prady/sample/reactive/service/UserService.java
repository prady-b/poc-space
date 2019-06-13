/**
 *
 */

package com.prady.sample.reactive.service;

import com.prady.sample.reactive.dto.UserAccountDTO;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * @author Prady
 */
public interface UserService {

    /**
     * @return
     */
    Flux<UserAccountDTO> getUsers();

    /**
     * @param userAccountDTO
     * @return
     */
    Mono<UserAccountDTO> create(Mono<UserAccountDTO> userAccountDTO);

    /**
     * @param id
     * @return
     */
    Mono<UserAccountDTO> getUser(String id);

    /**
     * @param id
     * @param body
     * @return
     */
    Mono<UserAccountDTO> update(String id, Mono<UserAccountDTO> body);

    /**
     * @param id
     * @return
     */
    Mono<Void> delete(String id);

}
