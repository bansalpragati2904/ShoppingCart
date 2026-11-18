
import db.DBConnection;
import service.ShoppingCart;
import service.Billing;
import dao.ProductDAO;
import model.OrderItem;
import dao.OrderDAO;

import java.util.ArrayList;
import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        ShoppingCart shoppingCart = new ShoppingCart();
        Billing billing = new Billing();
        ProductDAO productDAO = new ProductDAO();

        // Check database connection
        try (Connection con = DBConnection.getConnection()) {

            if (con == null || con.isClosed()) {
                System.out.println("Database connection failed!");
                return;
            }

            System.out.println("\n========================================");
            System.out.println("       SHOPPING CART MANAGEMENT");
            System.out.println("========================================");
            System.out.println("Database connected successfully!");

        } catch (Exception e) {
            System.out.println("Database connection error!");
            e.printStackTrace();
            return;
        }

        int choice;

        do {

            System.out.println("\n============== MAIN MENU ==============");
            System.out.println("1. View All Products");
            System.out.println("2. Search Product");
            System.out.println("3. Add Product to Cart");
            System.out.println("4. View Cart");
            System.out.println("5. Update Cart Quantity");
            System.out.println("6. Remove Product from Cart");
            System.out.println("7. Generate Bill / Place Order");
            System.out.println("8. View Order History");
            System.out.println("9. Update Product Price");
            System.out.println("0. Exit");
            System.out.println("=======================================");

            System.out.print("Enter your choice: ");

            while (!sc.hasNextInt()) {
                System.out.println("Please enter a valid number!");
                sc.next();
                System.out.print("Enter your choice: ");
            }

            choice = sc.nextInt();

            switch (choice) {

                case 1:
                    shoppingCart.displayProducts();
                    break;

                case 2:
                    System.out.print("Enter Product ID: ");
                    int searchId = readInteger(sc);
                    shoppingCart.searchProduct(searchId);
                    break;

                case 3:

                    String more = "yes";

                    while (more.equalsIgnoreCase("yes")) {

                        System.out.print("\nEnter Product ID: ");
                        int addId = readInteger(sc);

                        System.out.print("Enter Quantity: ");
                        int addQuantity = readInteger(sc);

                        shoppingCart.addProduct(addId, addQuantity);

                        System.out.print("\nDo you want to add another product? (yes/no): ");

                        sc.nextLine();
                        more = sc.nextLine().trim();

                        while (more.isEmpty()
                                || (!more.equalsIgnoreCase("yes")
                                && !more.equalsIgnoreCase("no"))) {

                            System.out.print("Please enter yes or no: ");
                            more = sc.nextLine().trim();
                        }
                    }

                    System.out.println("\nReturning to Main Menu...");
                    break;

                case 4:
                    shoppingCart.displayCart();
                    break;

                case 5:
                    System.out.print("Enter Product ID: ");
                    int updateId = readInteger(sc);

                    System.out.print("Enter New Quantity: ");
                    int newQuantity = readInteger(sc);

                    shoppingCart.updateProductQuantity(
                            updateId, newQuantity
                    );
                    break;

                case 6:
                    System.out.print("Enter Product ID: ");
                    int removeId = readInteger(sc);

                    shoppingCart.removeProduct(removeId);
                    break;

                case 7:
                    billing.generateBill();
                    break;

                case 8:

                    OrderDAO orderDAO = new OrderDAO();

                    var orders = orderDAO.getOrderHistory();

                    if (orders.isEmpty()) {
                        System.out.println("No previous orders found!");
                    } else {

                        for (var order : orders) {

                            System.out.println("\n==================================");
                            System.out.println("Order ID: " + order.getOrderId());
                            System.out.println("Subtotal: ₹" + order.getSubtotal());
                            System.out.println("Discount: ₹" + order.getDiscount());
                            System.out.println("Final Amount: ₹" + order.getFinalAmount());
                            System.out.println("Order Date: " + order.getOrderDate());

                            System.out.println("\nPurchased Products:");

                            ArrayList<OrderItem> items =
                                    orderDAO.getOrderItems(order.getOrderId());

                            for (OrderItem item : items) {

                                System.out.println("----------------------------------");
                                System.out.println("Product ID: " + item.getProductId());
                                System.out.println("Product Name: " + item.getProductName());
                                System.out.println("Quantity: " + item.getQuantity());
                                System.out.println("Unit Price: ₹" + item.getUnitPrice());
                                System.out.println("Item Total: ₹" + item.getItemTotal());
                            }

                            System.out.println("==================================");
                        }
                    }

                    break;

                case 9:

                    System.out.print("Enter Product ID: ");
                    int priceId = readInteger(sc);

                    System.out.print("Enter New Price: ");

                    while (!sc.hasNextBigDecimal()) {
                        System.out.println("Enter a valid price!");
                        sc.next();
                        System.out.print("Enter New Price: ");
                    }

                    BigDecimal newPrice = sc.nextBigDecimal();

                    if (productDAO.updateProductPrice(priceId, newPrice)) {
                        System.out.println("Price updated successfully!");
                    } else {
                        System.out.println("Price update failed!");
                    }

                    break;

                case 0:
                    System.out.println("\nThank you for using our system!");
                    System.out.println("Exiting...");
                    break;

                default:
                    System.out.println("Invalid choice! Please try again.");
            }

        } while (choice != 0);

        sc.close();
    }

    // Read an integer safely
    public static int readInteger(Scanner sc) {

        while (!sc.hasNextInt()) {
            System.out.println("Please enter a valid integer!");
            sc.next();
        }

        return sc.nextInt();
    }
}
