/**
 *
 */
package com.prady.sample.tx.domain;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Version;

import org.springframework.format.annotation.NumberFormat;

/**
 * @author Prady
 *
 */
@Entity
public class OrderDetail implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderDetailId;
    @Version
    private Integer version;
    private Long productId;
    @ManyToOne(fetch = FetchType.LAZY)
    private Order order;
    @NumberFormat
    private BigDecimal unitPrice;
    @NumberFormat
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
        orderDetailId = id;
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
     * @return the orders
     */
    public Order getOrder() {
        return order;
    }

    /**
     * @param orders the orders to set
     */
    public void setOrder(Order order) {
        this.order = order;
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
