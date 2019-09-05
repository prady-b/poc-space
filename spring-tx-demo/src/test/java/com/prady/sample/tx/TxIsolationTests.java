/**
 *
 */

package com.prady.sample.tx;

import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.ObjectOptimisticLockingFailureException;

import com.prady.sample.tx.dto.CustomerDTO;
import com.prady.sample.tx.service.CustomerService;
import com.prady.sample.tx.service.TxIsolationService;

/**
 * @author Prady
 */
public class TxIsolationTests extends BaseTests {

    @Autowired
    private TxIsolationService txIsolationService;
    @Autowired
    private CustomerService customerService;

    @Override
    protected Boolean isInitialDataRequired() {
        return Boolean.FALSE;
    }

    @Test
    void entityDirtyCheckWithoutSaveNewTx() {
        List<Long> customerIds = dataStoreHelper.createCustomers(port, 1);
        txIsolationService.entityDirtyCheck(customerIds.get(0), Boolean.FALSE, Boolean.TRUE);
        CustomerDTO customer = customerService.getCustomer(customerIds.get(0));
        Assertions.assertEquals("Updated First Name", customer.getFirstName());
        Assertions.assertEquals("Updated Last Name", customer.getLastName());
    }

    @Test
    void entityDirtyCheckWithSaveNewTx() {
        List<Long> customerIds = dataStoreHelper.createCustomers(port, 1);
        Exception exception = Assertions.assertThrows(ObjectOptimisticLockingFailureException.class, () -> {
            txIsolationService.entityDirtyCheck(customerIds.get(0), Boolean.TRUE, Boolean.TRUE);
        });
        Assertions.assertTrue(exception.getMessage()
                .contains("Object of class [com.prady.sample.tx.domain.Customer] with identifier [" + customerIds.get(0) + "]"));
    }

    @Test
    void entityDirtyCheckWithoutSaveDefault() {
        List<Long> customerIds = dataStoreHelper.createCustomers(port, 1);
        txIsolationService.entityDirtyCheck(customerIds.get(0), Boolean.FALSE, Boolean.FALSE);
        CustomerDTO customer = customerService.getCustomer(customerIds.get(0));
        Assertions.assertEquals("Updated First Name", customer.getFirstName());
        Assertions.assertEquals("Updated Last Name", customer.getLastName());
    }

    @Test
    void entityDirtyCheckWithSaveDefault() {
        List<Long> customerIds = dataStoreHelper.createCustomers(port, 1);
        txIsolationService.entityDirtyCheck(customerIds.get(0), Boolean.TRUE, Boolean.FALSE);
        CustomerDTO customer = customerService.getCustomer(customerIds.get(0));
        Assertions.assertEquals("Updated First Name", customer.getFirstName());
        Assertions.assertEquals("Updated Last Name", customer.getLastName());
    }
}
