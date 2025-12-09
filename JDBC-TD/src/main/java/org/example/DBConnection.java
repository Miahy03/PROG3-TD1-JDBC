package org.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    public Connection getDBConnection() throws SQLException {
        try {
            String DB_URL = "jdbc:postgresql://localhost:5432/product_management_db";
            String user = "product_manager_user";
            String password = "123456";

            return DriverManager.getConnection(DB_URL, user, password);
        } catch (SQLException e) {
            throw new SQLException(e);
        }
    }
}
