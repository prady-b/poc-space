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

import com.prady.sample.tx.domain.Product;
import com.prady.sample.tx.dto.ProductDTO;
import com.prady.sample.tx.exception.ItemAlreadyExistsException;
import com.prady.sample.tx.exception.ItemNotFoundException;
import com.prady.sample.tx.mapper.ProductMapper;
import com.prady.sample.tx.repository.ProductRepository;

/**
 * @author Prady
 *
 */
@Service
@Transactional
public class ProductServiceImpl implements ProductService {

    private static final Logger log = LoggerFactory.getLogger(ProductServiceImpl.class);
    public static final String PRODUCT = "Product";

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductMapper productMapper;

    @Override
    public List<ProductDTO> getProducts() {
        List<Product> products = productRepository.findAll();
        return productMapper.toProductDTOs(products);
    }

    @Override
    public ProductDTO getProduct(Long id) {
        Optional<Product> product = productRepository.findById(id);
        if (!product.isPresent()) {
            throw new ItemNotFoundException(id, PRODUCT);
        }
        return productMapper.toProductDTO(product.get());
    }

    @Override
    public ProductDTO create(ProductDTO productDTO) {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<ProductDTO>> validationErrors = validator.validate(productDTO);
        if (!CollectionUtils.isEmpty(validationErrors)) {
            throw new ConstraintViolationException(validationErrors);
        }
        Optional<Product> existingProduct = productRepository.findByProductCodeAndProductName(productDTO.getProductCode(),
                productDTO.getProductName());
        if (existingProduct.isPresent()) {
            throw new ItemAlreadyExistsException(existingProduct.get().getProductId(), PRODUCT);
        }
        log.info("Creating Product {} ", productDTO.getProductName());
        Product product = productMapper.toProduct(productDTO);
        return productMapper.toProductDTO(productRepository.save(product));
    }

    @Override
    public ProductDTO update(Long id, ProductDTO productDTO) {
        Optional<Product> product = productRepository.findById(id);
        if (!product.isPresent()) {
            throw new ItemNotFoundException(id, PRODUCT);
        }
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<ProductDTO>> validationErrors = validator.validate(productDTO);
        if (!CollectionUtils.isEmpty(validationErrors)) {
            log.error("Validation Errors {} ", validationErrors);
            throw new ConstraintViolationException(validationErrors);
        }
        Product productEntity = product.get();
        productMapper.toProduct(productDTO, productEntity);
        return productMapper.toProductDTO(productRepository.save(productEntity));
    }

    @Override
    public void delete(Long id) {
        Optional<Product> product = productRepository.findById(id);
        if (!product.isPresent()) {
            throw new ItemNotFoundException(id, PRODUCT);
        }
        productRepository.delete(product.get());
    }

}
