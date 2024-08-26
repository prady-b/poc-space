package com.prady.sample.mapstruct;

import com.prady.sample.mapstruct.domain.Address;
import com.prady.sample.mapstruct.domain.Employee;
import com.prady.sample.mapstruct.dto.EmployeeDTO;
import com.prady.sample.mapstruct.mapper.EmployeeMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.util.List;

class MapstructApplicationTests {

    @Test
    void testMap() {
        Employee employee = new Employee("Pradeep", "B", "test.com", "1234567890", List.of(new Address("123", "test", "test", "test", "test", List.of("test1", "test2"))));

        EmployeeDTO employeeDTO = Mappers.getMapper(EmployeeMapper.class).transformToEmployeeDTO(employee);
        Assertions.assertEquals(employeeDTO.getEmail(), employee.getEmailId());
        Assertions.assertEquals(employeeDTO.getFirstName(), employee.getFirstName());
        Assertions.assertEquals(employeeDTO.getLastName(), employee.getLastName());
        Assertions.assertEquals(employeeDTO.getPhone(), employee.getPhone());
        Assertions.assertEquals(employeeDTO.getAddress().get(0).getAddressLine1(), employee.getAddress().get(0).getAddressLine1());
        Assertions.assertEquals(employeeDTO.getAddress().get(0).getAddressLine2(), employee.getAddress().get(0).getAddressLine2());
        Assertions.assertEquals(employeeDTO.getAddress().get(0).getCity(), employee.getAddress().get(0).getCity());
        Assertions.assertEquals(employeeDTO.getAddress().get(0).getState(), employee.getAddress().get(0).getState());
        Assertions.assertEquals(employeeDTO.getAddress().get(0).getZipCode(), employee.getAddress().get(0).getZip());
        Assertions.assertEquals(employeeDTO.getAddress().get(0).getAddressLines1(), employee.getAddress().get(0).getAddressLines().get(0));
        Assertions.assertEquals(employeeDTO.getAddress().get(0).getAddressLines1(), employee.getAddress().get(0).getAddressLines().get(0));
        Assertions.assertNull(employeeDTO.getAddress().get(0).getAddressLines3());
    }

}
