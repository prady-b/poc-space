/**
 *
 */
package com.prady.sample.tx.service;

import com.prady.sample.tx.domain.Customer;

/**
 * @author Prady
 *
 */
public interface TxDirtyCheckChildService {

    void entityDirtyCheckWithoutSaveNewTx(Customer customer);

    void entityDirtyCheckWithSaveNewTx(Customer customer);

    void entityDirtyCheckWithoutSaveDefault(Customer customer);

    void entityDirtyCheckWithSaveDefault(Customer customer);

}
