package org.example;

import java.sql.Connection;

public class Main {
    public static void main(String[] args) {
        Connection connection = DBConnection.getConnection();
        if (connection != null) {
            System.out.println("Vous êtes connecté à PostgreSQL !");
        }
    }
}
