/**
 *
 */
package com.prady.sample.tx.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import com.prady.sample.tx.domain.Order;
import com.prady.sample.tx.dto.OrderDTO;

/**
 * @author Prady
 *
 */
@Mapper(componentModel = "spring", uses = { OrderDetailMapper.class })
public interface OrderMapper {

    OrderDTO toOrderDTO(Order order);

    List<OrderDTO> toOrderDTOs(List<Order> orders);

    Order toOrder(OrderDTO orderDTO);

    void toOrder(OrderDTO orderDTO, @MappingTarget Order order);
}
