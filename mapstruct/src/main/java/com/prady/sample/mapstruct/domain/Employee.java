package com.prady.sample.mapstruct.domain;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;

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
