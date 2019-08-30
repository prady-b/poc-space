/**
 *
 */
package com.prady.sample.tx.service;

import java.util.List;

import com.prady.sample.tx.dto.OrderDTO;

/**
 * @author Prady
 */
public interface OrderService {

    /**
     * @return
     */
    List<OrderDTO> getOrders();

    /**
     * @param id
     * @return
     */
    OrderDTO getOrder(Long id);

    /**
     * @param orderDTO
     * @return
     */
    OrderDTO create(OrderDTO orderDTO);

    /**
     * @param id
     * @param orderDTO
     * @return
     */
    OrderDTO update(Long id, OrderDTO orderDTO);

    /**
     * @param id
     */
    void delete(Long id);

}
