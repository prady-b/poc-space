/**
 *
 */
package com.prady.sample.tx.service;

/**
 * @author Prady
 *
 */
public interface TxIsolationService {

    void entityDirtyCheck(Long customerId, Boolean isSave, Boolean isNewTx);
}
