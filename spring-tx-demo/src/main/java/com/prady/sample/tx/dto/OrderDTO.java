/**
 *
 */

package com.prady.sample.tx.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.validation.constraints.NotNull;

/**
 * @author Prady
 *
 */
public class OrderDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long orderId;
    private Integer version;
    @NotNull(message = "Customer Id cannot be null")
    private Long customerId;
    private Date orderDate;
    private Date shippedDate;
    private String status;
    private Set<OrderDetailDTO> details = new HashSet<>();

    /**
     * @return the orderId
     */
    public Long getOrderId() {
        return orderId;
    }

    /**
     * @param orderId the orderId to set
     */
    public void setOrderId(Long id) {
        this.orderId = id;
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
     * @return the customerId
     */
    public Long getCustomerId() {
        return customerId;
    }

    /**
     * @param customerId the customerId to set
     */
    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    /**
     * @return the orderDate
     */
    public Date getOrderDate() {
        return orderDate;
    }

    /**
     * @param orderDate the orderDate to set
     */
    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    /**
     * @return the shippedDate
     */
    public Date getShippedDate() {
        return shippedDate;
    }

    /**
     * @param shippedDate the shippedDate to set
     */
    public void setShippedDate(Date shippedDate) {
        this.shippedDate = shippedDate;
    }

    /**
     * @return the status
     */
    public String getStatus() {
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * @return the details
     */
    public Set<OrderDetailDTO> getDetails() {
        return details;
    }

    /**
     * @param details the details to set
     */
    public void setDetails(Set<OrderDetailDTO> details) {
        this.details = details;
    }
}
