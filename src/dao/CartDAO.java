
package dao;

import db.DBConnection;
import model.CartItem;
import model.Product;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class CartDAO {

    // 1. Add product to cart
    public boolean addToCart(int productId, int quantity) {

        if (quantity <= 0) {
            System.out.println("Quantity must be greater than zero!");
            return false;
        }

        String checkQuery =
                "SELECT quantity FROM cart WHERE product_id = ?";

        String insertQuery =
                "INSERT INTO cart (product_id, quantity) VALUES (?, ?)";

        String updateQuery =
                "UPDATE cart SET quantity = quantity + ? WHERE product_id = ?";

        try (Connection con = DBConnection.getConnection()) {

            if (con == null) {
                return false;
            }

            try (PreparedStatement check = con.prepareStatement(checkQuery)) {

                check.setInt(1, productId);

                try (ResultSet rs = check.executeQuery()) {

                    if (rs.next()) {

                        try (PreparedStatement update =
                                     con.prepareStatement(updateQuery)) {

                            update.setInt(1, quantity);
                            update.setInt(2, productId);

                            return update.executeUpdate() > 0;
                        }

                    } else {

                        try (PreparedStatement insert =
                                     con.prepareStatement(insertQuery)) {

                            insert.setInt(1, productId);
                            insert.setInt(2, quantity);

                            return insert.executeUpdate() > 0;
                        }
                    }
                }
            }

        } catch (SQLException e) {
            System.out.println("Error adding product to cart!");
            e.printStackTrace();
        }

        return false;
    }

    // 2. View all cart items
    public ArrayList<CartItem> getCartItems() {

        ArrayList<CartItem> items = new ArrayList<>();

        String query =
                "SELECT p.product_id, p.product_name, p.price, c.quantity " +
                        "FROM cart c JOIN products p " +
                        "ON c.product_id = p.product_id";

        try (Connection con = DBConnection.getConnection()) {

            if (con == null) {
                return items;
            }

            try (PreparedStatement ps = con.prepareStatement(query);
                 ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {

                    Product product = new Product(
                            rs.getInt("product_id"),
                            rs.getString("product_name"),
                            rs.getBigDecimal("price")
                    );

                    CartItem item = new CartItem(
                            product,
                            rs.getInt("quantity")
                    );

                    items.add(item);
                }
            }

        } catch (SQLException e) {
            System.out.println("Error fetching cart!");
            e.printStackTrace();
        }

        return items;
    }

    // 3. Update product quantity
    public boolean updateQuantity(int productId, int quantity) {

        if (quantity <= 0) {
            System.out.println("Quantity must be greater than zero!");
            return false;
        }

        String query =
                "UPDATE cart SET quantity = ? WHERE product_id = ?";

        try (Connection con = DBConnection.getConnection()) {

            if (con == null) {
                return false;
            }

            try (PreparedStatement ps = con.prepareStatement(query)) {

                ps.setInt(1, quantity);
                ps.setInt(2, productId);

                return ps.executeUpdate() > 0;
            }

        } catch (SQLException e) {
            System.out.println("Error updating quantity!");
            e.printStackTrace();
        }

        return false;
    }

    // 4. Remove product from cart
    public boolean removeFromCart(int productId) {

        String query =
                "DELETE FROM cart WHERE product_id = ?";

        try (Connection con = DBConnection.getConnection()) {

            if (con == null) {
                return false;
            }

            try (PreparedStatement ps = con.prepareStatement(query)) {

                ps.setInt(1, productId);

                return ps.executeUpdate() > 0;
            }

        } catch (SQLException e) {
            System.out.println("Error removing product from cart!");
            e.printStackTrace();
        }

        return false;
    }

    // 5. Clear entire cart
    public boolean clearCart() {

        String query = "DELETE FROM cart";

        try (Connection con = DBConnection.getConnection()) {

            if (con == null) {
                return false;
            }

            try (PreparedStatement ps = con.prepareStatement(query)) {

                ps.executeUpdate();
                return true;
            }

        } catch (SQLException e) {
            System.out.println("Error clearing cart!");
            e.printStackTrace();
        }

        return false;
    }
}
