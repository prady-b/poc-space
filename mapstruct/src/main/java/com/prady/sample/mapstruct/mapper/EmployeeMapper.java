package com.prady.sample.mapstruct.mapper;

import com.prady.sample.mapstruct.domain.Address;
import com.prady.sample.mapstruct.domain.Employee;
import com.prady.sample.mapstruct.dto.AddressDTO;
import com.prady.sample.mapstruct.dto.EmployeeDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * @author Pradeep Balakrishnan
 */

@Mapper
public interface EmployeeMapper {

    @Mapping(source = "emailId", target = "email")
    EmployeeDTO transformToEmployeeDTO(Employee employee);

    @Mapping(source = "zip", target = "zipCode")
    AddressDTO transformToAddressDTO(Address address);
}
