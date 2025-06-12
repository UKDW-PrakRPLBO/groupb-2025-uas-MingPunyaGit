package org.uas.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnectionManager {
    private static DBConnectionManager instance;
    private static Connection connection;
    private static final String DB_URL = "jdbc:sqlite:dbuas.db";


    private DBConnectionManager() {
        try {
            connection = DriverManager.getConnection(DB_URL);
            System.out.println("Database connection berhasil cuyy");
        } catch (SQLException e) {
            System.err.println("Koneksi gagal cuyy: " + e.getMessage());
        }
    }


    public static synchronized Connection getConnection() {
        if (instance == null) {
            instance = new DBConnectionManager();
        }
        return connection;
    }
}