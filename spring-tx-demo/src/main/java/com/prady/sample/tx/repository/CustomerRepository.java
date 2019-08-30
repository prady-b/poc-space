/**
 *
 */
package com.prady.sample.tx.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.prady.sample.tx.domain.Customer;

/**
 * @author Prady
 *
 */
@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    /**
     * @param firstName
     * @param lastName
     * @return
     */
    Optional<Customer> findByFirstNameAndLastName(String firstName, String lastName);

}
