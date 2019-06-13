/**
 *
 */

package com.prady.sample.reactive.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

import com.prady.sample.reactive.domain.UserAccount;

import reactor.core.publisher.Mono;

/**
 * @author Prady
 */
@Repository
public interface UserAccountRepository extends ReactiveMongoRepository<UserAccount, String> {

    /**
     * @param userLoginName
     * @return
     */
    Mono<UserAccount> findByUserLoginName(String userLoginName);

}
