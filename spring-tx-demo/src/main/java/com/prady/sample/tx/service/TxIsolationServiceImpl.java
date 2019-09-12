/**
 *
 */
package com.prady.sample.tx.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import com.prady.sample.tx.domain.Product;
import com.prady.sample.tx.repository.ProductRepository;

/**
 * @author Prady
 *
 */
@Service
public class TxIsolationServiceImpl implements TxIsolationService {

    private static final Logger log = LoggerFactory.getLogger(TxIsolationServiceImpl.class);
    private static final String SUCCESS_MESSAGE = "Saved Successfully";

    @Autowired
    private ProductRepository productRepository;

    @Override
    @Transactional(isolation = Isolation.READ_UNCOMMITTED)
    public String selectReadUncomittedHibernate(Long productId) {
        log.info("In selectReadUncomittedHibernate");
        Optional<Product> productOps = productRepository.findById(productId);
        if (productOps.isPresent()) {
            Product product = productOps.get();
            log.info("Before update Product Name {}; Waiting for update ", product.getProductName());
            sleep(3);
            productOps = productRepository.findById(productId);
            product = productOps.orElse(new Product());
            log.info("After update Product Name {} ", product.getProductName());
            return product.getProductName();
        }
        return null;
    }

    @Override
    @Transactional(isolation = Isolation.READ_UNCOMMITTED)
    public void saveReadUncomittedHibernate(Long productId, String productName) {
        log.info("In saveReadUncomittedHibernate");
        Optional<Product> productOps = productRepository.findById(productId);
        if (productOps.isPresent()) {
            Product product = productOps.get();
            product.setProductName(productName);
            productRepository.saveAndFlush(product);
            sleep(3);
        }
        log.info(SUCCESS_MESSAGE);
    }

    @Override
    @Transactional(isolation = Isolation.READ_UNCOMMITTED)
    public String selectReadUncomittedJDBC(Long productId) {
        log.info("In selectReadUncomittedJDBC");
        String currProductName = productRepository.getProductNameUsingJDBC(productId);
        log.info("Before update Product Name: {}; Waiting for update", currProductName);
        sleep(3);
        String newProductName = productRepository.getProductNameUsingJDBC(productId);
        log.info("After update Product Name: {} ", newProductName);
        return newProductName;
    }

    @Override
    @Transactional(isolation = Isolation.READ_UNCOMMITTED)
    public void saveReadUncomittedJDBC(Long productId, String productName) {
        log.info("In saveReadUncomittedJDBC");
        productRepository.updateProductNameUsingJDBC(productId, productName);
        sleep(3);
        log.info(SUCCESS_MESSAGE);
    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public String selectReadComittedJDBC(Long productId) {
        log.info("In selectReadComittedJDBC");
        String currProductName = productRepository.getProductNameUsingJDBC(productId);
        log.info("Before update Product Name: {}; Waiting for update ", currProductName);
        sleep(1);
        String newProductName = productRepository.getProductNameUsingJDBC(productId);
        log.info("After update without Commit; Product Name: {} ", newProductName);
        sleep(1);
        newProductName = productRepository.getProductNameUsingJDBC(productId);
        log.info("After update with Commit; Product Name: {} ", newProductName);
        return newProductName;
    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public void saveReadComittedJDBC(Long productId, String productName) {
        log.info("In saveReadComittedJDBC");
        productRepository.updateProductNameUsingJDBC(productId, productName);
        sleep(1);
        log.info(SUCCESS_MESSAGE);
    }

    @Override
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public String selectRepeatableReadJDBC(Long productId) {
        log.info("In selectRepeatableReadJDBC");
        String currProductName = productRepository.getProductNameUsingJDBC(productId);
        log.info("Before update Product Name: {}; Waiting for update ", currProductName);
        sleep(1);
        String newProductName = productRepository.getProductNameUsingJDBC(productId);
        log.info("After update without Commit; Product Name: {} ", newProductName);
        sleep(1);
        newProductName = productRepository.getProductNameUsingJDBC(productId);
        log.info("After update with Commit; Product Name: {} ", newProductName);
        return newProductName;
    }

    @Override
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public void saveRepeatableReadJDBC(Long productId, String productName) {
        sleep(1);
        log.info("In saveRepeatableReadJDBC");
        productRepository.updateProductNameUsingJDBC(productId, productName);
        sleep(1);
        log.info(SUCCESS_MESSAGE);
    }

    @Override
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public int selectRepeatableReadMultipleJDBC(int unitInStock) {
        log.info("In selectRepeatableReadMultipleJDBC");
        int currCount = productRepository.getProductCountUsingJDBC(unitInStock);
        log.info("Before insert Product count: {}; Waiting for insert ", currCount);
        sleep(2);
        int newCount = productRepository.getProductCountUsingJDBC(unitInStock);
        log.info("After insert without Commit; Product count: {} ", newCount);
        sleep(1);
        newCount = productRepository.getProductCountUsingJDBC(unitInStock);
        log.info("After insert with Commit; Product count: {} ", newCount);
        return newCount;
    }

    @Override
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public void saveRepeatableReadMultipleJDBC(int unitInStock) {
        sleep(1);
        log.info("In saveRepeatableReadMultipleJDBC");
        productRepository.insert("PRODUCT_6", "Product Name 6", unitInStock, BigDecimal.valueOf(100));
        sleep(2);
        log.info(SUCCESS_MESSAGE);
    }

    @Override
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public int selectRepeatableReadMultipleLockJDBC(int unitInStock) {
        log.info("In selectRepeatableReadMultipleLockJDBC");
        List<Product> products = productRepository.getProductUsingJDBC(unitInStock);
        log.info("Before insert Product count: {}; Waiting for insert ", products.size());
        sleep(2);
        products = productRepository.getProductUsingJDBC(unitInStock);
        log.info("After insert without Commit; Product count: {} ", products.size());
        sleep(1);
        products = productRepository.getProductUsingJDBC(unitInStock);
        log.info("After insert with Commit; Product count: {} ", products.size());
        return products.size();
    }

    @Override
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public void saveRepeatableReadMultipleLockJDBC(int unitInStock) {
        log.info("In saveRepeatableReadMultipleLockJDBC");
        List<Product> products = productRepository.getProductUsingJDBC(unitInStock);
        productRepository.insert("PRODUCT_6", "Product Name 6", unitInStock, BigDecimal.valueOf(100));
        productRepository.updateProductNameUsingJDBC(products.get(1).getProductId(), "Updated (saveRepeatableReadMultipleLockJDBC)");
        sleep(2);
        log.info(SUCCESS_MESSAGE);
    }

    private void sleep(long seconds) {
        try {
            TimeUnit.SECONDS.sleep(seconds);
        } catch (Exception e) {
            log.error("Error while sleep ", e);
            // Suppressing
        }
    }

}
