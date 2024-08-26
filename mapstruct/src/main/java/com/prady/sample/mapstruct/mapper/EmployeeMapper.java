package com.prady.sample.mapstruct.mapper;

import com.prady.sample.mapstruct.domain.Address;
import com.prady.sample.mapstruct.domain.Employee;
import com.prady.sample.mapstruct.dto.AddressDTO;
import com.prady.sample.mapstruct.dto.EmployeeDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

/**
 * @author Pradeep Balakrishnan
 */

@Mapper
public interface EmployeeMapper {

    @Mapping(source = "emailId", target = "email")
    EmployeeDTO transformToEmployeeDTO(Employee employee);

    @Mapping(source = "zip", target = "zipCode")
    //@Mapping(expression = "java(address.getAddressLines().get(0))", target = "addressLines1")
    //@Mapping(expression = "java(address.getAddressLines().get(1))", target = "addressLines2")
    //@Mapping(expression = "java(address.getAddressLines().size() > 2 ? address.getAddressLines().get(2) : null)", target = "addressLines3")

    @Mapping(source = "addressLines", target = "addressLines1", qualifiedByName = "addressLines1")
    @Mapping(source = "addressLines", target = "addressLines2", qualifiedByName = "addressLines2")
    @Mapping(source = "addressLines", target = "addressLines3", qualifiedByName = "addressLines3")
    AddressDTO transformToAddressDTO(Address address);

    @Named("addressLines1")
    default String mapAddressLines1(List<String> addressLines) {
        return addressLines != null && !addressLines.isEmpty() ? addressLines.get(0) : null;
    }

    @Named("addressLines2")
    default String mapAddressLines2(List<String> addressLines) {
        return addressLines != null && addressLines.size() > 1 ? addressLines.get(1) : null;
    }

    @Named("addressLines3")
    default String mapAddressLines3(List<String> addressLines) {
        return addressLines != null && addressLines.size() > 2 ? addressLines.get(2) : null;
    }


}
