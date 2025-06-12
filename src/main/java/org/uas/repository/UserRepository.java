package org.uas.repository;

import org.uas.data.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserRepository {
    private final Connection connection;

    public UserRepository(Connection connection) {
        this.connection = connection;
        createTable();
    }

    public void createTable() {

        String userTableSql = "CREATE TABLE IF NOT EXISTS users ("
                + "email TEXT NOT NULL PRIMARY KEY,"
                + "username TEXT NOT NULL UNIQUE,"
                + "password TEXT NOT NULL"
                + ")";

        if (connection != null) {
            try (Statement stmt = connection.createStatement()) {
                stmt.execute(userTableSql);
                // Insert default admin user
                insertDefaultAdmin();
            } catch (SQLException e) {
                System.err.println("Error creating table: " + e.getMessage());
            }
        }
    }


    private void insertDefaultAdmin() {
        String checkSql = "SELECT COUNT(*) FROM users WHERE username = 'admin'";
        String insertSql = "INSERT INTO users (email, username, password) VALUES (?, ?, ?)";

        try (PreparedStatement checkStmt = connection.prepareStatement(checkSql)) {
            ResultSet rs = checkStmt.executeQuery();
            if (rs.next() && rs.getInt(1) == 0) {

                try (PreparedStatement insertStmt = connection.prepareStatement(insertSql)) {
                    insertStmt.setString(1, "admin@ukdw.com");
                    insertStmt.setString(2, "admin");
                    insertStmt.setString(3, "admin123");
                    insertStmt.executeUpdate();
                    System.out.println("Default admin user created");
                }
            }
        } catch (SQLException e) {
            System.err.println("Error inserting default admin: " + e.getMessage());
        }
    }


    public List<User> findAll() {
        ArrayList<User> users = new ArrayList<>();
        String sql = "SELECT email, username, password FROM users";

        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                users.add(new User(
                        rs.getString("email"),
                        rs.getString("username"),
                        rs.getString("password")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Gagal menemukan semua users: " + e.getMessage());
        }

        return users;
    }


    public boolean authenticateUser(String username, String password) {
        String sql = "SELECT COUNT(*) FROM users WHERE username = ? AND password = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, username);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();

            return rs.next() && rs.getInt(1) > 0;
        } catch (SQLException e) {
            System.err.println("Gagal authenticating user: " + e.getMessage());
            return false;
        }
    }


    public boolean insertUser(String email, String username, String password) {
        String sql = "INSERT INTO users (email, username, password) VALUES (?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, email);
            stmt.setString(2, username);
            stmt.setString(3, password);

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Gagal memasukan user: " + e.getMessage());
            return false;
        }
    }


    public boolean updateUser(String email, String username, String password) {
        String sql = "UPDATE users SET username = ?, password = ? WHERE email = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, username);
            stmt.setString(2, password);
            stmt.setString(3, email);

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Gagal memperbarui user: " + e.getMessage());
            return false;
        }
    }


    public boolean deleteUser(String email) {
        String sql = "DELETE FROM users WHERE email = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, email);

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error deleting user: " + e.getMessage());
            return false;
        }
    }
}