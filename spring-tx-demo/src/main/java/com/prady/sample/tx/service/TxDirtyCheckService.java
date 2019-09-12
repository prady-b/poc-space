/**
 *
 */
package com.prady.sample.tx.service;

/**
 * @author Prady
 *
 */
public interface TxDirtyCheckService {

    void entityDirtyCheck(Long customerId, Boolean isSave, Boolean isNewTx);
}
