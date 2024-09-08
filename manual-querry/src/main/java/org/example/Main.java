package org.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;

public class Main {

    // Database URL, username, and password
    static final String DB_URL = "jdbc:postgresql://localhost:5454/dev";
    static final String USER = "cyno";
    static final String PASS = "desu";

    public static void main(String[] args) {

        Connection conn = null;
        Statement stmt = null;
        try {
            // Register JDBC driver
            Class.forName("org.postgresql.Driver");

            // Open a connection
            System.out.println("Connecting to database...");
            conn = DriverManager.getConnection(DB_URL, USER, PASS);

            // Execute a query
            System.out.println("Creating statement..");
            stmt = conn.createStatement();
            String sql = """
                            CREATE TABLE app_user_authority (
                                app_user_id BIGINT NOT NULL,
                                authority_name VARCHAR(255) NOT NULL,
                                PRIMARY KEY (app_user_id, authority_name),
                                FOREIGN KEY (app_user_id) REFERENCES app_user(id),
                                FOREIGN KEY (authority_name) REFERENCES authority(name)
                            );
            """;

            ResultSet rs = stmt.executeQuery(sql);

            // Extract data from result set
            while (rs.next()) {
                // Retrieve by column name
                int id = rs.getInt("id");
                String name = rs.getString("name");

                // Display values
                System.out.print("ID: " + id);
                System.out.println(", Name: " + name);
            }

            // Clean-up environment
            rs.close();
            stmt.close();
            conn.close();
        } catch (SQLException | ClassNotFoundException se) {
            // Handle errors for JDBC and Class.forName
            se.printStackTrace();
        } finally {
            // finally block used to close resources
            try {
                if (stmt != null) stmt.close();
            } catch (SQLException se2) { }
            try {
                if (conn != null) conn.close();
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
        System.out.println("Goodbye!");
    }
}
