
package dao;

import db.DBConnection;
import model.CartItem;
import model.Order;
import model.OrderItem;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class OrderDAO {

    // 1. Place order and save order items
    public int placeOrder(BigDecimal subtotal, BigDecimal discount,
                          BigDecimal finalAmount,
                          ArrayList<CartItem> items) {

        String orderQuery =
                "INSERT INTO orders (subtotal, discount, final_amount) " +
                        "VALUES (?, ?, ?)";

        String itemQuery =
                "INSERT INTO order_items " +
                        "(order_id, product_id, quantity, unit_price, item_total) " +
                        "VALUES (?, ?, ?, ?, ?)";

        Connection con = DBConnection.getConnection();

        if (con == null) {
            return -1;
        }

        try {
            con.setAutoCommit(false);

            int orderId;

            try (PreparedStatement ps = con.prepareStatement(
                    orderQuery, Statement.RETURN_GENERATED_KEYS)) {

                ps.setBigDecimal(1, subtotal);
                ps.setBigDecimal(2, discount);
                ps.setBigDecimal(3, finalAmount);

                ps.executeUpdate();

                try (ResultSet rs = ps.getGeneratedKeys()) {

                    if (rs.next()) {
                        orderId = rs.getInt(1);
                    } else {
                        throw new SQLException("Order ID not generated!");
                    }
                }
            }

            try (PreparedStatement ps = con.prepareStatement(itemQuery)) {

                for (CartItem item : items) {

                    BigDecimal price = item.getProduct().getPrice();

                    BigDecimal total = item.getTotalPrice();

                    ps.setInt(1, orderId);
                    ps.setInt(2, item.getProduct().getProductId());
                    ps.setInt(3, item.getQuantity());
                    ps.setBigDecimal(4, price);
                    ps.setBigDecimal(5, total);

                    ps.addBatch();
                }

                ps.executeBatch();
            }

            con.commit();

            return orderId;

        } catch (SQLException e) {

            try {
                con.rollback();
            } catch (SQLException rollbackError) {
                rollbackError.printStackTrace();
            }

            System.out.println("Error placing order!");
            e.printStackTrace();

        } finally {

            try {
                con.setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }

            try {
                con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return -1;
    }

    // 2. Get all previous orders
    public ArrayList<Order> getOrderHistory() {

        ArrayList<Order> orders = new ArrayList<>();

        String query =
                "SELECT order_id, subtotal, discount, final_amount, order_date " +
                        "FROM orders ORDER BY order_date DESC";

        try (Connection con = DBConnection.getConnection()) {

            if (con == null) {
                return orders;
            }

            try (PreparedStatement ps = con.prepareStatement(query);
                 ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {

                    Order order = new Order(
                            rs.getInt("order_id"),
                            rs.getBigDecimal("subtotal"),
                            rs.getBigDecimal("discount"),
                            rs.getBigDecimal("final_amount"),
                            rs.getTimestamp("order_date")
                    );

                    orders.add(order);
                }
            }

        } catch (SQLException e) {
            System.out.println("Error fetching order history!");
            e.printStackTrace();
        }

        return orders;
    }

    // 3. Get products of a particular order
    public ArrayList<OrderItem> getOrderItems(int orderId) {

        ArrayList<OrderItem> items = new ArrayList<>();

        String query =
                "SELECT oi.product_id, p.product_name, " +
                        "oi.quantity, oi.unit_price, oi.item_total " +
                        "FROM order_items oi " +
                        "JOIN products p ON oi.product_id = p.product_id " +
                        "WHERE oi.order_id = ?";

        try (Connection con = DBConnection.getConnection()) {

            if (con == null) {
                return items;
            }

            try (PreparedStatement ps = con.prepareStatement(query)) {

                ps.setInt(1, orderId);

                try (ResultSet rs = ps.executeQuery()) {

                    while (rs.next()) {

                        OrderItem item = new OrderItem(
                                rs.getInt("product_id"),
                                rs.getString("product_name"),
                                rs.getInt("quantity"),
                                rs.getBigDecimal("unit_price"),
                                rs.getBigDecimal("item_total")
                        );

                        items.add(item);
                    }
                }
            }

        } catch (SQLException e) {
            System.out.println("Error fetching order items!");
            e.printStackTrace();
        }

        return items;
    }
}
