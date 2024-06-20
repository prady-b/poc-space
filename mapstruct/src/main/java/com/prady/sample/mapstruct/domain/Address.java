package com.prady.sample.mapstruct.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author Pradeep Balakrishnan
 */

@Data
@AllArgsConstructor
public class Address {
    private String addressLine1;
    private String addressLine2;
    private String city;
    private String state;
    private String zip;
}
