package com.example.service;

import com.example.model.User;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

/**
 * Unit tests for UserService
 */
public class UserServiceTest {
    
    private UserService userService;
    
    @Before
    public void setUp() {
        userService = UserService.getInstance();
    }
    
    @Test
    public void testGetAllUsers() {
        List<User> users = userService.getAllUsers();
        assertNotNull("Users list should not be null", users);
        assertTrue("Should have sample users", users.size() >= 3);
    }
    
    @Test
    public void testSaveAndGetUser() {
        User user = new User("Test", "User", "test@example.com", "555-0123");
        userService.save(user);
        
        assertNotNull("User ID should be generated", user.getId());
        
        User retrievedUser = userService.getUserById(user.getId());
        assertNotNull("Retrieved user should not be null", retrievedUser);
        assertEquals("First names should match", "Test", retrievedUser.getFirstName());
        assertEquals("Last names should match", "User", retrievedUser.getLastName());
        assertEquals("Emails should match", "test@example.com", retrievedUser.getEmail());
    }
    
    @Test
    public void testUpdateUser() {
        // Get an existing user
        List<User> users = userService.getAllUsers();
        assertTrue("Should have users to test with", users.size() > 0);
        
        User user = users.get(0);
        String originalFirstName = user.getFirstName();
        
        // Update the user
        user.setFirstName("Updated");
        userService.save(user);
        
        // Retrieve and verify
        User updatedUser = userService.getUserById(user.getId());
        assertEquals("First name should be updated", "Updated", updatedUser.getFirstName());
        
        // Restore original name
        user.setFirstName(originalFirstName);
        userService.save(user);
    }
    
    @Test
    public void testDeleteUser() {
        // Create a test user
        User user = new User("Delete", "Test", "delete@example.com", "555-9999");
        userService.save(user);
        Long userId = user.getId();
        
        // Verify user exists
        assertNotNull("User should exist before deletion", userService.getUserById(userId));
        
        // Delete user
        userService.deleteUser(userId);
        
        // Verify user is deleted
        assertNull("User should not exist after deletion", userService.getUserById(userId));
    }
}