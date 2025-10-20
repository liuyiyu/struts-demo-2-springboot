package com.example.action;

import com.example.model.User;
import com.example.service.UserService;
import com.opensymphony.xwork2.ActionSupport;

import java.util.List;

/**
 * User action class that handles all user-related operations
 */
public class UserAction extends ActionSupport {
    private static final long serialVersionUID = 1L;
    
    private UserService userService;
    private List<User> users;
    private User user;
    private Long userId;
    
    public UserAction() {
        this.userService = UserService.getInstance();
        this.user = new User();
    }
    
    /**
     * List all users
     */
    public String list() throws Exception {
        users = userService.getAllUsers();
        return SUCCESS;
    }
    
    /**
     * Show the user form for creating a new user
     */
    public String showForm() throws Exception {
        // If userId is provided, load the user for editing
        if (userId != null) {
            user = userService.getUserById(userId);
            if (user == null) {
                addActionError("User not found with ID: " + userId);
                return ERROR;
            }
        }
        return SUCCESS;
    }
    
    /**
     * Edit an existing user
     */
    public String edit() throws Exception {
        if (userId == null) {
            addActionError("User ID is required for editing");
            return ERROR;
        }
        
        user = userService.getUserById(userId);
        if (user == null) {
            addActionError("User not found with ID: " + userId);
            return ERROR;
        }
        
        return SUCCESS;
    }
    
    /**
     * Save a user (create or update)
     */
    public String save() throws Exception {
        // Validate the user data
        if (!validateUser()) {
            return INPUT;
        }
        
        try {
            userService.save(user);
            if (user.getId() == null) {
                addActionMessage("User created successfully!");
            } else {
                addActionMessage("User updated successfully!");
            }
        } catch (Exception e) {
            addActionError("Error saving user: " + e.getMessage());
            return INPUT;
        }
        
        return SUCCESS;
    }
    
    /**
     * Delete a user
     */
    public String delete() throws Exception {
        if (userId == null) {
            addActionError("User ID is required for deletion");
            return ERROR;
        }
        
        try {
            userService.deleteUser(userId);
            addActionMessage("User deleted successfully!");
        } catch (Exception e) {
            addActionError("Error deleting user: " + e.getMessage());
        }
        
        return SUCCESS;
    }
    
    /**
     * Validate user data
     */
    private boolean validateUser() {
        boolean isValid = true;
        
        if (user.getFirstName() == null || user.getFirstName().trim().isEmpty()) {
            addFieldError("user.firstName", "First name is required");
            isValid = false;
        }
        
        if (user.getLastName() == null || user.getLastName().trim().isEmpty()) {
            addFieldError("user.lastName", "Last name is required");
            isValid = false;
        }
        
        if (user.getEmail() == null || user.getEmail().trim().isEmpty()) {
            addFieldError("user.email", "Email is required");
            isValid = false;
        } else if (!isValidEmail(user.getEmail())) {
            addFieldError("user.email", "Please enter a valid email address");
            isValid = false;
        }
        
        return isValid;
    }
    
    /**
     * Simple email validation
     */
    private boolean isValidEmail(String email) {
        return email != null && email.matches("^[A-Za-z0-9+_.-]+@(.+)$");
    }
    
    // Getters and setters
    public List<User> getUsers() {
        return users;
    }
    
    public void setUsers(List<User> users) {
        this.users = users;
    }
    
    public User getUser() {
        return user;
    }
    
    public void setUser(User user) {
        this.user = user;
    }
    
    public Long getUserId() {
        return userId;
    }
    
    public void setUserId(Long userId) {
        this.userId = userId;
    }
}