
package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class DBConnection {

    public static Connection getConnection() {

        Properties properties = new Properties();

        try (FileInputStream input =
                     new FileInputStream("db.properties")) {

            properties.load(input);

            String URL = properties.getProperty("db.url");
            String USER = properties.getProperty("db.username");
            String PASSWORD = properties.getProperty("db.password");

            Class.forName("com.mysql.cj.jdbc.Driver");

            Connection connection = DriverManager.getConnection(
                    URL, USER, PASSWORD
            );

            return connection;

        } catch (IOException e) {
            System.out.println("Database configuration file not found!");
            e.printStackTrace();

        } catch (ClassNotFoundException e) {
            System.out.println("MySQL JDBC Driver not found!");
            e.printStackTrace();

        } catch (SQLException e) {
            System.out.println("Unable to connect to database!");
            e.printStackTrace();
        }

        return null;
    }
}
