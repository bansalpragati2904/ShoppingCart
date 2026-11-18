
package service;

import dao.CartDAO;
import dao.OrderDAO;
import model.CartItem;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;

public class Billing {

    private CartDAO cartDAO;
    private OrderDAO orderDAO;

    public Billing() {
        cartDAO = new CartDAO();
        orderDAO = new OrderDAO();
    }

    // Generate bill and place order
    public void generateBill() {

        ArrayList<CartItem> items = cartDAO.getCartItems();

        if (items.isEmpty()) {
            System.out.println("\nYour cart is empty!");
            return;
        }

        BigDecimal subtotal = BigDecimal.ZERO;

        // Calculate subtotal
        for (CartItem item : items) {

            BigDecimal total = item.getTotalPrice();

            subtotal = subtotal.add(total);
        }

        // Calculate discount
        BigDecimal discount;

        if (subtotal.compareTo(new BigDecimal("50000")) >= 0) {
            discount = subtotal.multiply(new BigDecimal("0.10"));
        } else if (subtotal.compareTo(new BigDecimal("20000")) >= 0) {
            discount = subtotal.multiply(new BigDecimal("0.05"));
        } else {
            discount = BigDecimal.ZERO;
        }

        discount = discount.setScale(2, RoundingMode.HALF_UP);

        // Calculate final amount
        BigDecimal finalAmount = subtotal.subtract(discount);

        System.out.println("\n==========================================");
        System.out.println("             SHOPPING CART BILL");
        System.out.println("==========================================");

        System.out.printf("%-20s %-8s %-8s %-10s%n",
                "PRODUCT", "PRICE", "QTY", "TOTAL");

        System.out.println("------------------------------------------");

        for (CartItem item : items) {

            BigDecimal price = item.getProduct().getPrice();
            BigDecimal total = item.getTotalPrice();

            System.out.printf("%-20s ₹%-7s %-8d ₹%-9s%n",
                    item.getProduct().getProductName(),
                    price.setScale(2, RoundingMode.HALF_UP),
                    item.getQuantity(),
                    total.setScale(2, RoundingMode.HALF_UP));
        }

        System.out.println("------------------------------------------");

        System.out.printf("Subtotal       : ₹%s%n",
                subtotal.setScale(2, RoundingMode.HALF_UP));

        System.out.printf("Discount       : ₹%s%n", discount);

        System.out.printf("Final Amount   : ₹%s%n",
                finalAmount.setScale(2, RoundingMode.HALF_UP));

        System.out.println("==========================================");

        // Save order in database
        int orderId = orderDAO.placeOrder(
                subtotal, discount, finalAmount, items
        );

        if (orderId != -1) {

            boolean cleared = cartDAO.clearCart();

            System.out.println("\nOrder placed successfully!");
            System.out.println("Order ID: " + orderId);

            if (cleared) {
                System.out.println("Cart cleared successfully!");
            } else {
                System.out.println("Warning: Could not clear the cart.");
            }

            System.out.println("\nThank you for shopping with us!");

        } else {

            System.out.println("\nOrder could not be placed.");
            System.out.println("Please try again.");
        }
    }
}
