package com.prady.sample.mapstruct.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author Pradeep Balakrishnan
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeDTO {

    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private List<AddressDTO> address;
}
