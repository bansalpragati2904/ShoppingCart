
package model;

import java.math.BigDecimal;

public class OrderItem {

    private int productId;
    private String productName;
    private int quantity;
    private BigDecimal unitPrice;
    private BigDecimal itemTotal;

    public OrderItem(int productId, String productName,
                     int quantity, BigDecimal unitPrice,
                     BigDecimal itemTotal) {

        this.productId = productId;
        this.productName = productName;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.itemTotal = itemTotal;
    }

    public int getProductId() {
        return productId;
    }

    public String getProductName() {
        return productName;
    }

    public int getQuantity() {
        return quantity;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public BigDecimal getItemTotal() {
        return itemTotal;
    }
}
