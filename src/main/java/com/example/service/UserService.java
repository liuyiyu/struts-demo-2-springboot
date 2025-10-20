package com.example.service;

import com.example.model.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * User service class that handles database operations using H2 in-memory database
 */
public class UserService {
    private static final String DB_URL = "jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1";
    private static final String DB_USER = "sa";
    private static final String DB_PASSWORD = "";
    
    private static UserService instance;
    
    private UserService() {
        initializeDatabase();
        insertSampleData();
    }
    
    public static synchronized UserService getInstance() {
        if (instance == null) {
            instance = new UserService();
        }
        return instance;
    }
    
    /**
     * Initialize the H2 in-memory database and create the users table
     */
    private void initializeDatabase() {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             Statement stmt = conn.createStatement()) {
            
            // Create users table
            String createTableSQL = "CREATE TABLE IF NOT EXISTS users (" +
                    "id BIGINT AUTO_INCREMENT PRIMARY KEY, " +
                    "first_name VARCHAR(50) NOT NULL, " +
                    "last_name VARCHAR(50) NOT NULL, " +
                    "email VARCHAR(100) NOT NULL UNIQUE, " +
                    "phone VARCHAR(20)" +
                    ")";
            stmt.executeUpdate(createTableSQL);
            
        } catch (SQLException e) {
            throw new RuntimeException("Failed to initialize database", e);
        }
    }
    
    /**
     * Insert sample data into the users table
     */
    private void insertSampleData() {
        List<User> sampleUsers = List.of(
            new User("John", "Doe", "john.doe@example.com", "555-0101"),
            new User("Jane", "Smith", "jane.smith@example.com", "555-0102"),
            new User("Mike", "Johnson", "mike.johnson@example.com", "555-0103")
        );
        
        for (User user : sampleUsers) {
            save(user);
        }
    }
    
    /**
     * Get all users from the database
     */
    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM users ORDER BY id";
        
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                User user = new User();
                user.setId(rs.getLong("id"));
                user.setFirstName(rs.getString("first_name"));
                user.setLastName(rs.getString("last_name"));
                user.setEmail(rs.getString("email"));
                user.setPhone(rs.getString("phone"));
                users.add(user);
            }
            
        } catch (SQLException e) {
            throw new RuntimeException("Failed to retrieve users", e);
        }
        
        return users;
    }
    
    /**
     * Get a user by ID
     */
    public User getUserById(Long id) {
        String sql = "SELECT * FROM users WHERE id = ?";
        
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, id);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                User user = new User();
                user.setId(rs.getLong("id"));
                user.setFirstName(rs.getString("first_name"));
                user.setLastName(rs.getString("last_name"));
                user.setEmail(rs.getString("email"));
                user.setPhone(rs.getString("phone"));
                return user;
            }
            
        } catch (SQLException e) {
            throw new RuntimeException("Failed to retrieve user with id: " + id, e);
        }
        
        return null;
    }
    
    /**
     * Save a new user or update an existing one
     */
    public void save(User user) {
        if (user.getId() == null) {
            insertUser(user);
        } else {
            updateUser(user);
        }
    }
    
    /**
     * Insert a new user
     */
    private void insertUser(User user) {
        String sql = "INSERT INTO users (first_name, last_name, email, phone) VALUES (?, ?, ?, ?)";
        
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setString(1, user.getFirstName());
            stmt.setString(2, user.getLastName());
            stmt.setString(3, user.getEmail());
            stmt.setString(4, user.getPhone());
            
            stmt.executeUpdate();
            
            // Get the generated ID
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                user.setId(rs.getLong(1));
            }
            
        } catch (SQLException e) {
            throw new RuntimeException("Failed to insert user", e);
        }
    }
    
    /**
     * Update an existing user
     */
    private void updateUser(User user) {
        String sql = "UPDATE users SET first_name = ?, last_name = ?, email = ?, phone = ? WHERE id = ?";
        
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, user.getFirstName());
            stmt.setString(2, user.getLastName());
            stmt.setString(3, user.getEmail());
            stmt.setString(4, user.getPhone());
            stmt.setLong(5, user.getId());
            
            stmt.executeUpdate();
            
        } catch (SQLException e) {
            throw new RuntimeException("Failed to update user", e);
        }
    }
    
    /**
     * Delete a user by ID
     */
    public void deleteUser(Long id) {
        String sql = "DELETE FROM users WHERE id = ?";
        
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, id);
            stmt.executeUpdate();
            
        } catch (SQLException e) {
            throw new RuntimeException("Failed to delete user with id: " + id, e);
        }
    }
}