/**
 *
 */
package com.prady.sample.tx.service;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.prady.sample.tx.domain.Order;
import com.prady.sample.tx.dto.OrderDTO;
import com.prady.sample.tx.dto.OrderDetailDTO;
import com.prady.sample.tx.dto.ProductDTO;
import com.prady.sample.tx.exception.InsufficientResourcesException;
import com.prady.sample.tx.mapper.OrderMapper;
import com.prady.sample.tx.repository.OrderRepository;

/**
 * @author Prady
 *
 */
@Service
public class OrderTxServiceImpl implements OrderTxService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductTxService productTxService;

    @Override
    @Transactional
    public OrderDTO save(OrderDTO orderDTO) {
        Order order = orderMapper.toOrder(orderDTO);
        for (OrderDetailDTO detail : orderDTO.getDetails()) {
            ProductDTO productDTO = productService.getProduct(detail.getProductId());
            productDTO.setUnitsInStock(productDTO.getUnitsInStock() - detail.getQuantity());
            productTxService.save(productDTO);
        }
        return orderMapper.toOrderDTO(orderRepository.save(order));
    }

    @Override
    @Transactional
    public OrderDTO saveWithSuppressingException(OrderDTO orderDTO) {
        Order order = orderMapper.toOrder(orderDTO);
        OrderDTO savedOrder = orderMapper.toOrderDTO(orderRepository.save(order));
        try {
            for (OrderDetailDTO detail : orderDTO.getDetails()) {
                ProductDTO productDTO = productService.getProduct(detail.getProductId());
                productDTO.setUnitsInStock(productDTO.getUnitsInStock() - detail.getQuantity());
                productTxService.save(productDTO);
            }
        } catch (InsufficientResourcesException e) {
            // Supressing
        }
        return savedOrder;
    }

    /**
     * @param productTxService the productTxService to set
     */
    @Override
    public void setProductTxService(ProductTxService productTxService) {
        this.productTxService = productTxService;
    }
}
