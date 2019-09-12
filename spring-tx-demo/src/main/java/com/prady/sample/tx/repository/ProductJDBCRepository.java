/**
 *
 */
package com.prady.sample.tx.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

/**
 * @author Prady
 *
 */
@Repository
public class ProductJDBCRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public String getProductName(Long productId) {
        return jdbcTemplate.queryForObject("SELECT product_name FROM product WHERE product_id = ?", String.class, productId);
    }

    public void updateProductName(Long productId, String productName) {
        jdbcTemplate.update("UPDATE product SET product_name = ? WHERE product_id = ?", productName, productId);
    }
}
