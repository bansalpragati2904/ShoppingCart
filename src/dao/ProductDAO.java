
package dao;

import db.DBConnection;
import model.Product;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ProductDAO {

    // 1. Display all products
    public ArrayList<Product> getAllProducts() {

        ArrayList<Product> products = new ArrayList<>();

        String query =
                "SELECT product_id, product_name, price FROM products";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                Product product = new Product(
                        rs.getInt("product_id"),
                        rs.getString("product_name"),
                        rs.getBigDecimal("price")
                );

                products.add(product);
            }

        } catch (SQLException e) {
            System.out.println("Error fetching products!");
            e.printStackTrace();
        }

        return products;
    }

    // 2. Search product by ID
    public Product getProductById(int productId) {

        String query =
                "SELECT * FROM products WHERE product_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {

            ps.setInt(1, productId);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {

                    return new Product(
                            rs.getInt("product_id"),
                            rs.getString("product_name"),
                            rs.getBigDecimal("price")
                    );
                }
            }

        } catch (SQLException e) {
            System.out.println("Error searching product!");
            e.printStackTrace();
        }

        return null;
    }

    // 3. Update product price
    public boolean updateProductPrice(int productId, BigDecimal newPrice) {

        String query =
                "UPDATE products SET price = ? WHERE product_id = ?";

        if (newPrice == null || newPrice.compareTo(BigDecimal.ZERO) <= 0) {
            System.out.println("Price must be greater than zero!");
            return false;
        }

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {

            ps.setBigDecimal(1, newPrice);
            ps.setInt(2, productId);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error updating product price!");
            e.printStackTrace();
        }

        return false;
    }
}
