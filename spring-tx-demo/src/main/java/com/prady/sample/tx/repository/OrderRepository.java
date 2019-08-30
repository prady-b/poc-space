/**
 *
 */
package com.prady.sample.tx.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.prady.sample.tx.domain.Order;

/**
 * @author Prady
 *
 */
@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

}
