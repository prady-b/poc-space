/**
 *
 */
package com.prady.sample.tx.service;

import com.prady.sample.tx.dto.OrderDTO;

/**
 * @author Prady
 *
 */
public interface OrderTxService {

    OrderDTO save(OrderDTO orderDTO);

    OrderDTO saveWithSuppressingException(OrderDTO orderDTO);

    void setProductTxService(ProductTxService productTxService);
}
