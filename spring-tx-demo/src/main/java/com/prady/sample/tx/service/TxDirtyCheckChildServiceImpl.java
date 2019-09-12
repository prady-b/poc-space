/**
 *
 */
package com.prady.sample.tx.service;

import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.prady.sample.tx.domain.Customer;
import com.prady.sample.tx.repository.CustomerRepository;

/**
 * @author Prady
 *
 */
@Service
public class TxDirtyCheckChildServiceImpl implements TxDirtyCheckChildService {

    @Autowired
    private CustomerRepository customerRepository;
    private static final String LAST_NAME = "Updated First Name";

    @Override
    @Transactional(value = TxType.REQUIRES_NEW)
    public void entityDirtyCheckWithoutSaveNewTx(Customer customer) {
        customer.setFirstName(LAST_NAME);
    }

    @Override
    @Transactional(value = TxType.REQUIRES_NEW)
    public void entityDirtyCheckWithSaveNewTx(Customer customer) {
        customer.setFirstName(LAST_NAME);
        customerRepository.save(customer);
    }

    @Override
    @Transactional
    public void entityDirtyCheckWithoutSaveDefault(Customer customer) {
        customer.setFirstName(LAST_NAME);
    }

    @Override
    @Transactional
    public void entityDirtyCheckWithSaveDefault(Customer customer) {
        customer.setFirstName(LAST_NAME);
        customerRepository.save(customer);
    }

}
