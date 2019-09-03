/**
 *
 */

package com.prady.sample.tx;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.transaction.UnexpectedRollbackException;

import com.prady.sample.tx.dto.OrderDTO;
import com.prady.sample.tx.dto.OrderDetailDTO;
import com.prady.sample.tx.dto.ProductDTO;
import com.prady.sample.tx.exception.InsufficientResourcesException;
import com.prady.sample.tx.helper.DataStoreHelper;
import com.prady.sample.tx.service.OrderTxService;
import com.prady.sample.tx.service.ProductService;
import com.prady.sample.tx.service.ProductTxService;

/**
 * @author Prady
 */
public class TransactionTests extends BaseTests {

    @Autowired
    private OrderTxService orderTxService;
    @Autowired
    private ProductService productService;
    @Autowired
    private DataStoreHelper storeHelper;

    @Autowired
    private ProductTxService defaultProductTxService;

    @Autowired
    @Qualifier("requiredNewProductTxServiceImpl")
    private ProductTxService requriedNewProductTxService;

    @Test
    public void testDefaultTx() {
        OrderDTO orderDTO = storeHelper.getAnyOrder();
        int productUnitsinStock = 0;
        for (OrderDetailDTO detail : orderDTO.getDetails()) {
            ProductDTO productDTO = productService.getProduct(detail.getProductId());
            productUnitsinStock = productDTO.getUnitsInStock();
        }
        int excepted = productUnitsinStock - orderDTO.getDetails().iterator().next().getQuantity();
        orderTxService.setProductTxService(defaultProductTxService);
        OrderDTO savedOrderDTO = orderTxService.save(orderDTO);

        for (OrderDetailDTO detail : savedOrderDTO.getDetails()) {
            ProductDTO productDTO = productService.getProduct(detail.getProductId());
            Assertions.assertEquals(excepted, productDTO.getUnitsInStock());
        }
    }

    @Test
    public void testDefaultTxWithException() {
        OrderDTO orderDTO = storeHelper.getAnyOrder();
        for (OrderDetailDTO detail : orderDTO.getDetails()) {
            ProductDTO productDTO = productService.getProduct(detail.getProductId());
            detail.setQuantity(productDTO.getUnitsInStock() + 10);
        }

        Assertions.assertThrows(InsufficientResourcesException.class, () -> {
            orderTxService.setProductTxService(defaultProductTxService);
            orderTxService.save(orderDTO);
        });
    }

    @Test
    public void testDefaultTxWithSuppressingException() {
        OrderDTO orderDTO = storeHelper.getAnyOrder();
        for (OrderDetailDTO detail : orderDTO.getDetails()) {
            detail.setQuantity(5000);
        }
        Exception exception = Assertions.assertThrows(UnexpectedRollbackException.class, () -> {
            orderTxService.setProductTxService(defaultProductTxService);
            orderTxService.saveWithSuppressingException(orderDTO);
        });
        Assertions.assertTrue(
                exception.getMessage().contains("Transaction silently rolled back because it has been marked as rollback-only"));
    }

    @Test
    public void testRequiredTxWithSuppressingException() {
        OrderDTO orderDTO = storeHelper.getAnyOrder();
        String status = "ORDERED" + System.currentTimeMillis();
        orderDTO.setStatus(status);
        orderTxService.setProductTxService(requriedNewProductTxService);

        OrderDTO savedOrderDTO = orderTxService.saveWithSuppressingException(orderDTO);
        Assertions.assertEquals(status, savedOrderDTO.getStatus());

        orderTxService.setProductTxService(defaultProductTxService);
    }
}
