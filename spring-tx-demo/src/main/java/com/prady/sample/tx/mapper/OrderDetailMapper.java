/**
 *
 */
package com.prady.sample.tx.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.prady.sample.tx.domain.OrderDetail;
import com.prady.sample.tx.dto.OrderDetailDTO;

/**
 * @author Prady
 *
 */
@Mapper(componentModel = "spring")
public interface OrderDetailMapper {

    OrderDetailDTO toOrderDetailDTO(OrderDetail orderDetail);

    @Mapping(target = "order", ignore = true)
    OrderDetail toOrderDetail(OrderDetailDTO orderDetailDTO);

}
