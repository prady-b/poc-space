/**
 *
 */
package com.prady.sample.tx;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.CustomizableThreadFactory;

import com.prady.sample.tx.domain.Product;
import com.prady.sample.tx.repository.ProductRepository;
import com.prady.sample.tx.service.TxIsolationService;

/**
 * @author Prady
 *
 */
public class TxIsolationTests extends BaseTests {

    @Autowired
    private TxIsolationService txIsolationService;
    @Autowired
    private ProductRepository productRepository;

    @Override
    protected Boolean isInitialDataRequired() {
        return Boolean.FALSE;
    }

    @Test
    public void testReadUncomittedHibernate() throws Exception {
        String productName = "Updated Name";
        List<Long> productIds = dataStoreHelper.createProducts(port, 1);
        Long productId = productIds.get(0);
        Optional<Product> savedProductOp = productRepository.findById(productId);
        ExecutorService executorService = Executors.newFixedThreadPool(2, new CustomizableThreadFactory("testReadUncomittedHibernate"));
        Future<String> productNameFuture = executorService
                .submit(() -> txIsolationService.selectReadUncomittedHibernate(productId));
        TimeUnit.MICROSECONDS.sleep(500);
        executorService.submit(() -> txIsolationService.saveReadUncomittedHibernate(productId, productName));
        Assertions.assertEquals(savedProductOp.get().getProductName(), productNameFuture.get());
    }

    @Test
    public void testReadUncomittedJDBC() throws Exception {
        String productName = "Updated Name";
        List<Long> productIds = dataStoreHelper.createProducts(port, 1);
        Long productId = productIds.get(0);
        ExecutorService executorService = Executors.newFixedThreadPool(2, new CustomizableThreadFactory("testReadUncomittedJDBC"));
        Future<String> productNameFuture = executorService.submit(() -> txIsolationService.selectReadUncomittedJDBC(productId));
        executorService.submit(() -> txIsolationService.saveReadUncomittedJDBC(productId, productName));
        Assertions.assertEquals(productName, productNameFuture.get());
    }

    @Test
    public void testReadComittedJDBC() throws Exception {
        String productName = "Updated Name(ReadComitted)";
        List<Long> productIds = dataStoreHelper.createProducts(port, 1);
        Long productId = productIds.get(0);
        ExecutorService executorService = Executors.newFixedThreadPool(2, new CustomizableThreadFactory("testReadComittedJDBC"));
        Future<String> productNameFuture = executorService.submit(() -> txIsolationService.selectReadComittedJDBC(productId));
        executorService.submit(() -> txIsolationService.saveReadComittedJDBC(productId, productName));
        Assertions.assertEquals(productName, productNameFuture.get());
    }

    @Test
    public void testRepeatableReadJDBC() throws Exception {
        String productName = "Updated Name(RepeatableReadJDBC)";
        List<Long> productIds = dataStoreHelper.createProducts(port, 1);
        Optional<Product> savedProductOp = productRepository.findById(productIds.get(0));
        Long productId = productIds.get(0);
        ExecutorService executorService = Executors.newFixedThreadPool(2, new CustomizableThreadFactory("testRepeatableReadJDBC"));
        Future<String> productNameFuture = executorService.submit(() -> txIsolationService.selectRepeatableReadJDBC(productId));
        executorService.submit(() -> txIsolationService.saveRepeatableReadJDBC(productId, productName));
        if (environment.getActiveProfiles().length > 0 && StringUtils.equals(environment.getActiveProfiles()[0], "mysql")) {
            Assertions.assertEquals(savedProductOp.get().getProductName(), productNameFuture.get());
        } else {
            Assertions.assertEquals(productName, productNameFuture.get()); // H2 returns the updated Value
        }
        savedProductOp = productRepository.findById(productIds.get(0));
        Assertions.assertEquals(productName, savedProductOp.get().getProductName());
    }

    @Test
    public void testRepeatableReadMultipleJDBC() throws Exception {
        int unitInStock = 3001;
        productRepository.save(new Product("PRODUCT_1", "Product Name 1", unitInStock, BigDecimal.valueOf(100)));
        productRepository.save(new Product("PRODUCT_2", "Product Name 2", unitInStock, BigDecimal.valueOf(100)));
        productRepository.save(new Product("PRODUCT_3", "Product Name 3", unitInStock, BigDecimal.valueOf(100)));
        productRepository.save(new Product("PRODUCT_4", "Product Name 4", unitInStock, BigDecimal.valueOf(100)));
        productRepository.save(new Product("PRODUCT_5", "Product Name 5", unitInStock, BigDecimal.valueOf(100)));

        ExecutorService executorService = Executors.newFixedThreadPool(2, new CustomizableThreadFactory("testRepeatableReadMultipleJDBC"));
        Future<Integer> productNameFuture = executorService.submit(() -> txIsolationService.selectRepeatableReadMultipleJDBC(unitInStock));
        TimeUnit.MICROSECONDS.sleep(500);
        executorService.submit(() -> txIsolationService.saveRepeatableReadMultipleJDBC(unitInStock));

        // MySQL: Suppose that you are running in the default REPEATABLE READ isolation level. When you issue a consistent read (that is, an
        // ordinary SELECT statement), InnoDB gives your transaction a timepoint according to which your query sees the database. If another
        // transaction deletes a row and commits after your timepoint was assigned, you do not see the row as having been deleted. Inserts
        // and updates are treated similarly
        // https://dev.mysql.com/doc/refman/8.0/en/innodb-consistent-read.html
        if (environment.getActiveProfiles().length > 0 && StringUtils.equals(environment.getActiveProfiles()[0], "mysql")) {
            Assertions.assertEquals(5, productNameFuture.get());
        } else {
            Assertions.assertEquals(6, productNameFuture.get());
        }

        Assertions.assertEquals(6, productRepository.getProductCountUsingJDBC(unitInStock));
    }

    @Test
    public void testRepeatableReadMultipleLockJDBC() throws Exception {
        int unitInStock = 6001;
        productRepository.save(new Product("PRODUCT_1", "Product Name 1", unitInStock, BigDecimal.valueOf(100)));
        productRepository.save(new Product("PRODUCT_2", "Product Name 2", unitInStock, BigDecimal.valueOf(100)));
        productRepository.save(new Product("PRODUCT_3", "Product Name 3", unitInStock, BigDecimal.valueOf(100)));
        productRepository.save(new Product("PRODUCT_4", "Product Name 4", unitInStock, BigDecimal.valueOf(100)));
        productRepository.save(new Product("PRODUCT_5", "Product Name 5", unitInStock, BigDecimal.valueOf(100)));

        ExecutorService executorService = Executors.newFixedThreadPool(2,
                new CustomizableThreadFactory("testRepeatableReadMultipleLockJDBC"));
        Future<Integer> productNameFuture = executorService
                .submit(() -> txIsolationService.selectRepeatableReadMultipleLockJDBC(unitInStock));
        TimeUnit.MILLISECONDS.sleep(500);
        executorService.submit(() -> txIsolationService.saveRepeatableReadMultipleLockJDBC(unitInStock));

        executorService.awaitTermination(3000, TimeUnit.MILLISECONDS);
        if (environment.getActiveProfiles().length > 0 && StringUtils.equals(environment.getActiveProfiles()[0], "mysql")) {
            Assertions.assertEquals(5, productNameFuture.get());
        } else {
            Assertions.assertEquals(6, productNameFuture.get());
        }
        Assertions.assertEquals(6, productRepository.getProductCountUsingJDBC(unitInStock));
    }
}
