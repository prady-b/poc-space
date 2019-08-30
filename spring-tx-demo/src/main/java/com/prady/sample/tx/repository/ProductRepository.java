/**
 *
 */
package com.prady.sample.tx.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.prady.sample.tx.domain.Product;

/**
 * @author Prady
 *
 */
@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    /**
     * @param productCode
     * @param productName
     */
    Optional<Product> findByProductCodeAndProductName(String productCode, String productName);

}
