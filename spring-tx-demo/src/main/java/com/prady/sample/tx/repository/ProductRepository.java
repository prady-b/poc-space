/**
 *
 */
package com.prady.sample.tx.repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
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

    @Query(value = "SELECT product_name FROM product WHERE product_id = ?1", nativeQuery = true)
    String getProductNameUsingJDBC(Long productId);

    @Modifying
    @Query(value = "UPDATE product SET product_name = ?2 WHERE product_id = ?1", nativeQuery = true)
    void updateProductNameUsingJDBC(Long productId, String productName);

    @Query(value = "SELECT count(*) FROM product WHERE units_in_stock = ?1", nativeQuery = true)
    int getProductCountUsingJDBC(int unitInStock);

    @Modifying
    @Query(value = "INSERT INTO product (product_code, product_name, units_in_stock, unit_price) VALUES (?1, ?2, ?3, ?4)", nativeQuery = true)
    void insert(String productCode, String productName, int unitInStock, BigDecimal unitPrce);

    @Query(value = "SELECT product_id, product_code, product_name, units_in_stock, unit_price, version FROM product WHERE units_in_stock = ?1", nativeQuery = true)
    List<Product> getProductUsingJDBC(int unitInStock);

    @Modifying
    @Query(value = "UPDATE product SET product_name = ?3, units_in_stock = ?2 + 1 WHERE product_id = ?1", nativeQuery = true)
    void updateProductNameUsingJDBC(Long productId, int unitInStock, String productName);

    @Query(value = "SELECT product_id, product_code, product_name, units_in_stock, unit_price, version FROM product WHERE units_in_stock BETWEEN ?1 and ?2", nativeQuery = true)
    List<Product> getProductUsingJDBC(int startUnitInStock, int endUnitInStack);

}
