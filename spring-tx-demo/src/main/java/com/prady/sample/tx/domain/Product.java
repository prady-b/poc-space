/**
 *
 */
package com.prady.sample.tx.domain;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.format.annotation.NumberFormat;

/**
 * @author Prady
 *
 */
@Entity
public class Product implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productId;
    @Version
    private Integer version;
    @NotNull
    @Size(min = 5)
    private String productCode;
    @NotNull
    @Size(min = 10)
    private String productName;
    @NumberFormat
    private Integer unitsInStock;
    @NumberFormat
    private BigDecimal unitPrice;

    /**
     *
     */
    public Product() {
        // Default
    }

    /**
     * @param productCode
     * @param productName
     * @param unitsInStock
     * @param unitPrice
     */
    public Product(@NotNull @Size(min = 5) String productCode, @NotNull @Size(min = 10) String productName, Integer unitsInStock,
            BigDecimal unitPrice) {
        super();
        this.productCode = productCode;
        this.productName = productName;
        this.unitsInStock = unitsInStock;
        this.unitPrice = unitPrice;
    }

    /**
     * @return the id
     */
    public Long getProductId() {
        return productId;
    }

    /**
     * @param id the id to set
     */
    public void setProductId(Long id) {
        productId = id;
    }

    /**
     * @return the version
     */
    public Integer getVersion() {
        return version;
    }

    /**
     * @param version the version to set
     */
    public void setVersion(Integer version) {
        this.version = version;
    }

    /**
     * @return the productCode
     */
    public String getProductCode() {
        return productCode;
    }

    /**
     * @param productCode the productCode to set
     */
    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    /**
     * @return the productName
     */
    public String getProductName() {
        return productName;
    }

    /**
     * @param productName the productName to set
     */
    public void setProductName(String productName) {
        this.productName = productName;
    }

    /**
     * @return the unitsInStock
     */
    public Integer getUnitsInStock() {
        return unitsInStock;
    }

    /**
     * @param unitsInStock the unitsInStock to set
     */
    public void setUnitsInStock(Integer unitsInStock) {
        this.unitsInStock = unitsInStock;
    }

    /**
     * @return the unitPrice
     */
    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    /**
     * @param unitPrice the unitPrice to set
     */
    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }
}
