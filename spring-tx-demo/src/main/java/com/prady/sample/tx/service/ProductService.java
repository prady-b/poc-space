/**
 *
 */
package com.prady.sample.tx.service;

import java.util.List;

import com.prady.sample.tx.dto.ProductDTO;

/**
 * @author Prady
 */
public interface ProductService {

    /**
     * @return
     */
    List<ProductDTO> getProducts();

    /**
     * @param id
     * @return
     */
    ProductDTO getProduct(Long id);

    /**
     * @param productDTO
     * @return
     */
    ProductDTO create(ProductDTO productDTO);

    /**
     * @param id
     * @param productDTO
     * @return
     */
    ProductDTO update(Long id, ProductDTO productDTO);

    /**
     * @param id
     */
    void delete(Long id);

}
