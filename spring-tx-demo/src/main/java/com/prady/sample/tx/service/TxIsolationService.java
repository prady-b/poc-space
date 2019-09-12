/**
 *
 */
package com.prady.sample.tx.service;

/**
 * @author Prady
 *
 */
public interface TxIsolationService {

    String selectReadUncomittedHibernate(Long productId);

    void saveReadUncomittedHibernate(Long productId, String productName);

    String selectReadUncomittedJDBC(Long productId);

    void saveReadUncomittedJDBC(Long productId, String productName);

    String selectReadComittedJDBC(Long productId);

    void saveReadComittedJDBC(Long productId, String productName);

    String selectRepeatableReadJDBC(Long productId);

    void saveRepeatableReadJDBC(Long productId, String productName);

    int selectRepeatableReadMultipleJDBC(int unitInStock);

    void saveRepeatableReadMultipleJDBC(int unitInStock);

    int selectRepeatableReadMultipleLockJDBC(int unitInStock);

    void saveRepeatableReadMultipleLockJDBC(Long productId, int unitInStock);

}
