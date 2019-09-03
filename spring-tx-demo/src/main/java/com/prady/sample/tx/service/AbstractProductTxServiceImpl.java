/**
 *
 */
package com.prady.sample.tx.service;

import org.springframework.beans.factory.annotation.Autowired;

import com.prady.sample.tx.domain.Product;
import com.prady.sample.tx.dto.ProductDTO;
import com.prady.sample.tx.exception.InsufficientResourcesException;
import com.prady.sample.tx.mapper.ProductMapper;
import com.prady.sample.tx.repository.ProductRepository;

/**
 * @author Prady
 *
 */
public abstract class AbstractProductTxServiceImpl implements ProductTxService {

    @Autowired
    protected ProductRepository productRepository;
    @Autowired
    protected ProductMapper productMapper;

    @Override
    public ProductDTO save(ProductDTO productDTO) {
        if (productDTO.getUnitsInStock() < 0) {
            throw new InsufficientResourcesException(productDTO.getId(), Math.abs(productDTO.getUnitsInStock()), "PRODUCT");
        }
        Product product = productMapper.toProduct(productDTO);
        return productMapper.toProductDTO(productRepository.save(product));
    }

}
