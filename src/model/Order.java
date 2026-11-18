
package model;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class Order {

    private int orderId;
    private BigDecimal subtotal;
    private BigDecimal discount;
    private BigDecimal finalAmount;
    private Timestamp orderDate;

    public Order(int orderId, BigDecimal subtotal,
                 BigDecimal discount, BigDecimal finalAmount,
                 Timestamp orderDate) {

        this.orderId = orderId;
        this.subtotal = subtotal;
        this.discount = discount;
        this.finalAmount = finalAmount;
        this.orderDate = orderDate;
    }

    public int getOrderId() {
        return orderId;
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public BigDecimal getDiscount() {
        return discount;
    }

    public BigDecimal getFinalAmount() {
        return finalAmount;
    }

    public Timestamp getOrderDate() {
        return orderDate;
    }
}
