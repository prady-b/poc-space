/**
 *
 */
package com.prady.sample.tx.service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.transaction.Transactional;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.prady.sample.tx.domain.Order;
import com.prady.sample.tx.domain.OrderDetail;
import com.prady.sample.tx.dto.OrderDTO;
import com.prady.sample.tx.dto.OrderDetailDTO;
import com.prady.sample.tx.dto.ProductDTO;
import com.prady.sample.tx.exception.InsufficientResourcesException;
import com.prady.sample.tx.exception.ItemNotFoundException;
import com.prady.sample.tx.mapper.OrderMapper;
import com.prady.sample.tx.repository.OrderRepository;

/**
 * @author Prady
 *
 */
@Service
@Transactional
public class OrderServiceImpl implements OrderService {

    private static final Logger log = LoggerFactory.getLogger(OrderServiceImpl.class);
    public static final String ORDER = "Order";

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private ProductService productService;

    @Override
    public List<OrderDTO> getOrders() {
        List<Order> orders = orderRepository.findAll();
        return orderMapper.toOrderDTOs(orders);
    }

    @Override
    public OrderDTO getOrder(Long id) {
        Optional<Order> order = orderRepository.findById(id);
        if (!order.isPresent()) {
            throw new ItemNotFoundException(id, ORDER);
        }
        return orderMapper.toOrderDTO(order.get());
    }

    @Override
    public OrderDTO create(OrderDTO orderDTO) {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<OrderDTO>> validationErrors = validator.validate(orderDTO);
        if (!CollectionUtils.isEmpty(validationErrors)) {
            throw new ConstraintViolationException(validationErrors);
        }
        for (OrderDetailDTO detail : orderDTO.getDetails()) {
            ProductDTO productDTO = productService.getProduct(detail.getProductId());
            if (productDTO.getUnitsInStock() < detail.getQuantity()) {
                throw new InsufficientResourcesException(detail.getProductId(), detail.getQuantity(), "PRODUCT");
            }
        }
        Order order = orderMapper.toOrder(orderDTO);
        for (OrderDetail detail : order.getDetails()) {
            detail.setOrder(order);
        }
        OrderDTO savedOrder = orderMapper.toOrderDTO(orderRepository.save(order));

        for (OrderDetailDTO detail : orderDTO.getDetails()) {
            ProductDTO productDTO = productService.getProduct(detail.getProductId());
            productDTO.setUnitsInStock(productDTO.getUnitsInStock() - detail.getQuantity());
            productService.update(productDTO.getProductId(), productDTO);
        }

        return savedOrder;
    }

    @Override
    public OrderDTO update(Long id, OrderDTO orderDTO) {
        Optional<Order> order = orderRepository.findById(id);
        if (!order.isPresent()) {
            throw new ItemNotFoundException(id, ORDER);
        }
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<OrderDTO>> validationErrors = validator.validate(orderDTO);
        if (!CollectionUtils.isEmpty(validationErrors)) {
            log.error("Validation Errors {} ", validationErrors);
            throw new ConstraintViolationException(validationErrors);
        }
        Order orderEntity = order.get();
        orderMapper.toOrder(orderDTO, orderEntity);
        return orderMapper.toOrderDTO(orderRepository.save(orderEntity));
    }

    @Override
    public void delete(Long id) {
        Optional<Order> order = orderRepository.findById(id);
        if (!order.isPresent()) {
            throw new ItemNotFoundException(id, ORDER);
        }
        orderRepository.delete(order.get());
    }

}
