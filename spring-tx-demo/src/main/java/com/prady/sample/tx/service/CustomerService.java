/**
 *
 */
package com.prady.sample.tx.service;

import java.util.List;

import com.prady.sample.tx.dto.CustomerDTO;

/**
 * @author Prady
 */
public interface CustomerService {

    /**
     * @return
     */
    List<CustomerDTO> getCustomers();

    /**
     * @param id
     * @return
     */
    CustomerDTO getCustomer(Long id);

    /**
     * @param customerDTO
     * @return
     */
    CustomerDTO create(CustomerDTO customerDTO);

    /**
     * @param id
     * @param customerDTO
     * @return
     */
    CustomerDTO update(Long id, CustomerDTO customerDTO);

    /**
     * @param id
     */
    void delete(Long id);

}
