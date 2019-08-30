/**
 *
 */
package com.prady.sample.tx.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import com.prady.sample.tx.domain.Product;
import com.prady.sample.tx.dto.ProductDTO;

/**
 * @author Prady
 *
 */
@Mapper(componentModel = "spring")
public interface ProductMapper {

    ProductDTO toProductDTO(Product product);

    List<ProductDTO> toProductDTOs(List<Product> product);

    Product toProduct(ProductDTO productDTO);

    void toProduct(ProductDTO productDTO, @MappingTarget Product product);
}
