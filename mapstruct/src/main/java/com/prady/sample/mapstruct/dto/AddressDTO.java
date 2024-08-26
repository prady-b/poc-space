package com.prady.sample.mapstruct.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author Pradeep Balakrishnan
 */

@Data
@AllArgsConstructor
public class AddressDTO {
    private String addressLine1;
    private String addressLine2;
    private String city;
    private String state;
    private String zipCode;

    private String addressLines1;
    private String addressLines2;
    private String addressLines3;
}
