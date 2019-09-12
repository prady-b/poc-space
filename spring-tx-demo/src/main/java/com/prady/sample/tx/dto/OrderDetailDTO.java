/**
 *
 */

package com.prady.sample.tx.dto;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author Prady
 *
 */
public class OrderDetailDTO implements Serializable {

    private static final long serialVersionUID = 1L;
    private Long orderDetailId;
    private Integer version;
    private Long productId;
    private BigDecimal unitPrice;
    private Integer quantity;

    /**
     * @return the orderDetailId
     */
    public Long getOrderDetailId() {
        return orderDetailId;
    }

    /**
     * @param orderDetailId the orderDetailId to set
     */
    public void setOrderDetailId(Long id) {
        this.orderDetailId = id;
    }

    /**
     * @return the version
     */
    public Integer getVersion() {
        return version;
    }

    /**
     * @return the productId
     */
    public Long getProductId() {
        return productId;
    }

    /**
     * @param productId the productId to set
     */
    public void setProductId(Long productId) {
        this.productId = productId;
    }

    /**
     * @param version the version to set
     */
    public void setVersion(Integer version) {
        this.version = version;
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

    /**
     * @return the quantity
     */
    public Integer getQuantity() {
        return quantity;
    }

    /**
     * @param quantity the quantity to set
     */
    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

}
