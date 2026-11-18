CREATE DATABASE IF NOT EXISTS shopping_cart_db;

USE shopping_cart_db;

CREATE TABLE IF NOT EXISTS products (
                                        product_id INT PRIMARY KEY AUTO_INCREMENT,
                                        product_name VARCHAR(100) NOT NULL,
    price DECIMAL(10,2) NOT NULL,
    CHECK (price > 0)
    );

CREATE TABLE IF NOT EXISTS cart (
                                    cart_id INT PRIMARY KEY AUTO_INCREMENT,
                                    product_id INT NOT NULL,
                                    quantity INT NOT NULL,
                                    FOREIGN KEY (product_id) REFERENCES products(product_id),
    CHECK (quantity > 0)
    );

CREATE TABLE IF NOT EXISTS orders (
                                      order_id INT PRIMARY KEY AUTO_INCREMENT,
                                      subtotal DECIMAL(10,2) NOT NULL,
    discount DECIMAL(10,2) NOT NULL,
    final_amount DECIMAL(10,2) NOT NULL,
    order_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP
    );

CREATE TABLE IF NOT EXISTS order_items (
                                           item_id INT PRIMARY KEY AUTO_INCREMENT,
                                           order_id INT NOT NULL,
                                           product_id INT NOT NULL,
                                           quantity INT NOT NULL,
                                           unit_price DECIMAL(10,2) NOT NULL,
    item_total DECIMAL(10,2) NOT NULL,
    FOREIGN KEY (order_id) REFERENCES orders(order_id),
    FOREIGN KEY (product_id) REFERENCES products(product_id)
    );