/**
 *
 */
package com.prady.sample.tx.service;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;

import com.prady.sample.tx.domain.Product;
import com.prady.sample.tx.dto.ProductDTO;
import com.prady.sample.tx.mapper.ProductMapper;
import com.prady.sample.tx.repository.ProductRepository;

/**
 * @author Prady
 *
 */
@Primary
public class DefaultProductTxServiceImpl implements ProductTxService {

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ProductMapper productMapper;

    @Override
    @Transactional
    public ProductDTO save(ProductDTO productDTO) {
        Product product = productMapper.toProduct(productDTO);
        return productMapper.toProductDTO(productRepository.save(product));
    }

}
