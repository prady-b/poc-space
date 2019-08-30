/**
 *
 */
package com.prady.sample.tx.service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.transaction.Transactional;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.prady.sample.tx.domain.Customer;
import com.prady.sample.tx.dto.CustomerDTO;
import com.prady.sample.tx.exception.ItemAlreadyExistsException;
import com.prady.sample.tx.exception.ItemNotFoundException;
import com.prady.sample.tx.mapper.CustomerMapper;
import com.prady.sample.tx.repository.CustomerRepository;

/**
 * @author Prady
 *
 */
@Service
@Transactional
public class CustomerServiceImpl implements CustomerService {

    private static final Logger log = LoggerFactory.getLogger(CustomerServiceImpl.class);
    public static final String CUSTOMER = "Customer";

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private CustomerMapper customerMapper;

    @Override
    public List<CustomerDTO> getCustomers() {
        List<Customer> customers = customerRepository.findAll();
        return customerMapper.toCustomerDTOs(customers);
    }

    @Override
    public CustomerDTO getCustomer(Long id) {
        Optional<Customer> customer = customerRepository.findById(id);
        if (!customer.isPresent()) {
            throw new ItemNotFoundException(id, CUSTOMER);
        }
        return customerMapper.toCustomerDTO(customer.get());
    }

    @Override
    public CustomerDTO create(CustomerDTO customerDTO) {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<CustomerDTO>> validationErrors = validator.validate(customerDTO);
        if (!CollectionUtils.isEmpty(validationErrors)) {
            throw new ConstraintViolationException(validationErrors);
        }
        Optional<Customer> existingCustomer = customerRepository.findByFirstNameAndLastName(customerDTO.getFirstName(),
                customerDTO.getLastName());
        if (existingCustomer.isPresent()) {
            throw new ItemAlreadyExistsException(existingCustomer.get().getId(), CUSTOMER);
        }
        log.info("Creating Customer {} ", customerDTO.getFirstName());
        Customer customer = customerMapper.toCustomer(customerDTO);
        return customerMapper.toCustomerDTO(customerRepository.save(customer));
    }

    @Override
    public CustomerDTO update(Long id, CustomerDTO customerDTO) {
        Optional<Customer> customer = customerRepository.findById(id);
        if (!customer.isPresent()) {
            throw new ItemNotFoundException(id, CUSTOMER);
        }
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<CustomerDTO>> validationErrors = validator.validate(customerDTO);
        if (!CollectionUtils.isEmpty(validationErrors)) {
            log.error("Validation Errors {} ", validationErrors);
            throw new ConstraintViolationException(validationErrors);
        }
        Customer customerEntity = customer.get();
        customerMapper.toCustomer(customerDTO, customerEntity);
        return customerMapper.toCustomerDTO(customerRepository.save(customerEntity));
    }

    @Override
    public void delete(Long id) {
        Optional<Customer> customer = customerRepository.findById(id);
        if (!customer.isPresent()) {
            throw new ItemNotFoundException(id, CUSTOMER);
        }
        customerRepository.delete(customer.get());
    }

}
