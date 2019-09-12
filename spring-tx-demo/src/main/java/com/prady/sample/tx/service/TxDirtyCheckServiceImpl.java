/**
 *
 */
package com.prady.sample.tx.service;

import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.prady.sample.tx.domain.Customer;
import com.prady.sample.tx.repository.CustomerRepository;

/**
 * @author Prady
 *
 */
@Service
public class TxDirtyCheckServiceImpl implements TxDirtyCheckService {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private TxDirtyCheckChildService childService;

    @Override
    @Transactional
    public void entityDirtyCheck(Long customerId, Boolean isSave, Boolean isNewTx) {
        Optional<Customer> customerOp = customerRepository.findById(customerId);
        if (customerOp.isPresent()) {
            Customer customer = customerOp.get();
            customer.setLastName("Updated Last Name");
            if (Boolean.TRUE.equals(isSave)) {
                if (Boolean.TRUE.equals(isNewTx)) {
                    childService.entityDirtyCheckWithSaveNewTx(customer);
                } else {
                    childService.entityDirtyCheckWithSaveDefault(customer);
                }
            } else {
                if (Boolean.TRUE.equals(isNewTx)) {
                    childService.entityDirtyCheckWithoutSaveNewTx(customer);
                } else {
                    childService.entityDirtyCheckWithoutSaveDefault(customer);
                }
            }
        }
    }
}
