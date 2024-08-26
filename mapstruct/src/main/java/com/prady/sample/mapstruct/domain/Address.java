package com.prady.sample.mapstruct.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

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

    private List<String> addressLines;
}
