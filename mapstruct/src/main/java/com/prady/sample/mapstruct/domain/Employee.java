package com.prady.sample.mapstruct.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

/**
 * @author Pradeep Balakrishnan
 */

@Data
@AllArgsConstructor
public class Employee {

    private String firstName;
    private String lastName;
    private String emailId;
    private String phone;
    private List<Address> address;
}
