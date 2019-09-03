/**
 *
 */
package com.prady.sample.tx.service;

import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;

import org.springframework.stereotype.Service;

import com.prady.sample.tx.dto.ProductDTO;

/**
 * @author Prady
 *
 */
@Service
public class RequiredNewProductTxServiceImpl extends AbstractProductTxServiceImpl {

    @Override
    @Transactional(value = TxType.REQUIRES_NEW)
    public ProductDTO save(ProductDTO productDTO) {
        return super.save(productDTO);
    }
}
