import db.DBConnection;
import java.sql.Connection;
import java.sql.SQLException;

public class TestConnection {

    public static void main(String[] args) {

        System.out.println("======================================");
        System.out.println("       SHOPPING CART SYSTEM");
        System.out.println("          DATABASE TEST");
        System.out.println("======================================");

        try (Connection con = DBConnection.getConnection()) {

            if (con != null && !con.isClosed()) {

                System.out.println("SUCCESS!");
                System.out.println("Database connected successfully!");
                System.out.println("Database: shopping_cart_db");

            } else {

                System.out.println("Database connection failed!");
            }

        } catch (SQLException e) {

            System.out.println("Error: " + e.getMessage());
        }

        System.out.println("======================================");
    }
}
