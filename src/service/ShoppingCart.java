
package service;

import dao.CartDAO;
import dao.ProductDAO;
import model.CartItem;
import model.Product;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;

public class ShoppingCart {

    private ProductDAO productDAO;
    private CartDAO cartDAO;

    public ShoppingCart() {
        productDAO = new ProductDAO();
        cartDAO = new CartDAO();
    }

    // 1. Display all available products
    public void displayProducts() {

        ArrayList<Product> products = productDAO.getAllProducts();

        if (products.isEmpty()) {
            System.out.println("No products available!");
            return;
        }

        Collections.sort(products, new Comparator<Product>() {
            @Override
            public int compare(Product p1, Product p2) {
                return Integer.compare(p1.getProductId(), p2.getProductId());
            }
        });

        System.out.println("\n========== AVAILABLE PRODUCTS ==========");
        System.out.printf("%-10s %-20s %-15s%n",
                "ID", "PRODUCT NAME", "PRICE");

        System.out.println("----------------------------------------");

        Iterator<Product> itr = products.iterator();

        while (itr.hasNext()) {

            Product product = itr.next();

            System.out.printf("%-10d %-20s ₹%-14s%n",
                    product.getProductId(),
                    product.getProductName(),
                    product.getPrice());
        }

        System.out.println("========================================");
    }

    // 2. Search product
    public void searchProduct(int productId) {

        Product product = productDAO.getProductById(productId);

        if (product == null) {
            System.out.println("Product not found!");
        } else {
            System.out.println("\nProduct Found!");
            System.out.println("ID: " + product.getProductId());
            System.out.println("Name: " + product.getProductName());
            System.out.println("Price: ₹" + product.getPrice());
        }
    }

    // 3. Add product to cart
    public void addProduct(int productId, int quantity) {

        Product product = productDAO.getProductById(productId);

        if (product == null) {
            System.out.println("Invalid product ID!");
            return;
        }

        if (quantity <= 0) {
            System.out.println("Quantity must be greater than zero!");
            return;
        }

        boolean added = cartDAO.addToCart(productId, quantity);

        if (added) {
            System.out.println("Product added to cart successfully!");
        } else {
            System.out.println("Unable to add product to cart!");
        }
    }

    // 4. Display cart
    public void displayCart() {

        ArrayList<CartItem> items = cartDAO.getCartItems();

        if (items.isEmpty()) {
            System.out.println("\nYour cart is empty!");
            return;
        }

        BigDecimal subtotal = BigDecimal.ZERO;

        System.out.println("\n=============== YOUR CART ===============");
        System.out.printf("%-20s %-10s %-10s %-12s%n",
                "PRODUCT", "PRICE", "QUANTITY", "TOTAL");

        System.out.println("-----------------------------------------");

        for (CartItem item : items) {

            BigDecimal total = item.getTotalPrice();
            subtotal = subtotal.add(total);

            System.out.printf("%-20s ₹%-9s %-10d ₹%-11s%n",
                    item.getProduct().getProductName(),
                    item.getProduct().getPrice(),
                    item.getQuantity(),
                    total);
        }

        System.out.println("-----------------------------------------");
        System.out.println("Subtotal: ₹" + subtotal);
        System.out.println("=========================================");
    }

    // 5. Update quantity
    public void updateProductQuantity(int productId, int quantity) {

        if (quantity <= 0) {
            System.out.println("Quantity must be greater than zero!");
            return;
        }

        boolean updated = cartDAO.updateQuantity(productId, quantity);

        if (updated) {
            System.out.println("Quantity updated successfully!");
        } else {
            System.out.println("Product not found in cart!");
        }
    }

    // 6. Remove product
    public void removeProduct(int productId) {

        boolean removed = cartDAO.removeFromCart(productId);

        if (removed) {
            System.out.println("Product removed successfully!");
        } else {
            System.out.println("Product not found in cart!");
        }
    }
}
