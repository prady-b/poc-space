/**
 *
 */
package com.prady.sample.tx.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import com.prady.sample.tx.domain.Customer;
import com.prady.sample.tx.dto.CustomerDTO;

/**
 * @author Prady
 *
 */
@Mapper(componentModel = "spring")
public interface CustomerMapper {

    CustomerDTO toCustomerDTO(Customer customer);

    List<CustomerDTO> toCustomerDTOs(List<Customer> customer);

    Customer toCustomer(CustomerDTO customerDTO);

    void toCustomer(CustomerDTO customerDTO, @MappingTarget Customer customer);
}
