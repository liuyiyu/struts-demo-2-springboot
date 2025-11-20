# Data Model: User Management

**Phase**: 1 - Design & Contracts  
**Date**: November 20, 2025

## Overview

This document defines the data model for the user management system after migration to Spring Boot with JPA. The model maintains functional equivalence with the existing Struts application while adopting modern JPA patterns.

## Entity: User

### Purpose

Represents a person registered in the system. Users are the primary entity in this application, supporting full CRUD operations.

### Attributes

| Field | Type | Constraints | Description |
|-------|------|-------------|-------------|
| `id` | Long | Primary Key, Auto-generated | Unique identifier for the user |
| `firstName` | String | Required, Max 50 chars | User's given name |
| `lastName` | String | Required, Max 50 chars | User's family name |
| `email` | String | Required, Unique, Max 100 chars, Valid email format | User's email address for contact |
| `phone` | String | Optional, Max 20 chars | User's phone number |

### Validation Rules

- **firstName**: 
  - MUST NOT be null or empty
  - MUST NOT exceed 50 characters
  - Leading/trailing whitespace should be trimmed

- **lastName**:
  - MUST NOT be null or empty
  - MUST NOT exceed 50 characters
  - Leading/trailing whitespace should be trimmed

- **email**:
  - MUST NOT be null or empty
  - MUST match standard email format (RFC 5322 subset)
  - MUST be unique across all users in the system
  - MUST NOT exceed 100 characters
  - Should be stored in lowercase for consistency

- **phone**:
  - MAY be null (optional field)
  - If provided, MUST NOT exceed 20 characters
  - No specific format validation (accepts international formats)

### Database Mapping

**Table Name**: `users`

**Column Mappings**:
```
id          → BIGINT          PRIMARY KEY AUTO_INCREMENT
first_name  → VARCHAR(50)     NOT NULL
last_name   → VARCHAR(50)     NOT NULL
email       → VARCHAR(100)    NOT NULL UNIQUE
phone       → VARCHAR(20)     NULL
```

### JPA Entity Structure

```java
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "first_name", nullable = false, length = 50)
    @NotBlank(message = "First name is required")
    @Size(max = 50, message = "First name must not exceed 50 characters")
    private String firstName;
    
    @Column(name = "last_name", nullable = false, length = 50)
    @NotBlank(message = "Last name is required")
    @Size(max = 50, message = "Last name must not exceed 50 characters")
    private String lastName;
    
    @Column(name = "email", nullable = false, unique = true, length = 100)
    @NotBlank(message = "Email is required")
    @Email(message = "Email must be a valid email address")
    @Size(max = 100, message = "Email must not exceed 100 characters")
    private String email;
    
    @Column(name = "phone", length = 20)
    @Size(max = 20, message = "Phone must not exceed 20 characters")
    private String phone;
    
    // Constructors, getters, setters, equals, hashCode, toString
}
```

### State Transitions

User entities follow these state transitions:

```
[New] --create--> [Persisted]
[Persisted] --update--> [Persisted (modified)]
[Persisted] --delete--> [Removed]
```

**State Definitions**:
- **New**: Entity created in application memory, not yet in database (id = null)
- **Persisted**: Entity exists in database with assigned ID
- **Persisted (modified)**: Entity in database with pending changes (managed by JPA)
- **Removed**: Entity deleted from database (no longer accessible)

### Relationships

Currently, the User entity has no relationships to other entities. This is a standalone entity.

**Future Extensibility**:
- One-to-Many with `Address` entity (user can have multiple addresses)
- Many-to-Many with `Role` entity (user can have multiple roles for authorization)
- One-to-Many with `Activity` entity (audit trail of user actions)

### Data Transfer Objects (DTOs)

To separate API contracts from database entities, define DTOs for different operations:

#### UserDTO (Response)
```java
public class UserDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    
    // Maps from User entity for API responses
}
```

#### CreateUserRequest (Request)
```java
public class CreateUserRequest {
    @NotBlank(message = "First name is required")
    @Size(max = 50)
    private String firstName;
    
    @NotBlank(message = "Last name is required")
    @Size(max = 50)
    private String lastName;
    
    @NotBlank(message = "Email is required")
    @Email(message = "Email must be valid")
    @Size(max = 100)
    private String email;
    
    @Size(max = 20)
    private String phone;
}
```

#### UpdateUserRequest (Request)
```java
public class UpdateUserRequest {
    @NotBlank(message = "First name is required")
    @Size(max = 50)
    private String firstName;
    
    @NotBlank(message = "Last name is required")
    @Size(max = 50)
    private String lastName;
    
    @NotBlank(message = "Email is required")
    @Email(message = "Email must be valid")
    @Size(max = 100)
    private String email;
    
    @Size(max = 20)
    private String phone;
}
```

### Sample Data

The system initializes with the following sample users on startup:

```java
User user1 = new User(null, "John", "Doe", "john.doe@example.com", "555-0101");
User user2 = new User(null, "Jane", "Smith", "jane.smith@example.com", "555-0102");
User user3 = new User(null, "Mike", "Johnson", "mike.johnson@example.com", "555-0103");
```

This matches the existing Struts application sample data for continuity.

### Business Rules

1. **Email Uniqueness**: The system MUST prevent two users from having the same email address
   - Enforced at database level (UNIQUE constraint)
   - Validated at service level before save operations
   - Returns 400 Bad Request if duplicate email detected

2. **Immutable ID**: Once a user is persisted, their ID never changes
   - ID is auto-generated on first save
   - Update operations do not modify ID

3. **Required Fields**: firstName, lastName, and email are mandatory for all operations
   - Create operation fails if any required field is missing
   - Update operation fails if any required field is missing

4. **Soft Delete Not Used**: Delete operations permanently remove users from database
   - No soft delete flag or archived status
   - Deleted users cannot be recovered (matches existing behavior)

## Repository Layer

### Interface: UserRepository

```java
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
}
```

**Methods Provided**:
- `findAll()`: Retrieve all users (inherited from JpaRepository)
- `findById(Long id)`: Retrieve user by ID (inherited)
- `save(User user)`: Create or update user (inherited)
- `deleteById(Long id)`: Delete user by ID (inherited)
- `findByEmail(String email)`: Find user by email (custom query method)
- `existsByEmail(String email)`: Check if email exists (custom query method)

## Migration Notes

### Differences from Struts Version

1. **Field Access**: Struts used direct JDBC with manual column mapping; JPA uses object-relational mapping
2. **ID Generation**: Same strategy (database auto-increment) but handled by JPA
3. **Validation**: Moved from Struts XML config to Java annotations on entity/DTOs
4. **Transactions**: Automatic transaction management via `@Transactional` instead of manual connection handling

### Preserved Behavior

1. **Table Structure**: Identical column names and types
2. **Sample Data**: Same three initial users
3. **Validation Rules**: Equivalent constraints (required fields, email format)
4. **Business Logic**: Same CRUD operations with same semantics

## Performance Considerations

- **Indexing**: Email column should have index for uniqueness check performance
- **Lazy Loading**: Not applicable (no relationships currently)
- **Batch Operations**: Use JPA batch processing if bulk operations needed in future
- **Query Optimization**: Use `findById` and `findByEmail` methods which generate optimized SQL

## Testing Strategy

### Unit Tests
- Test entity validation annotations
- Test equals/hashCode implementations
- Test DTO mapping logic

### Integration Tests
- Test repository CRUD operations against H2 database
- Test email uniqueness constraint enforcement
- Test cascade operations (if relationships added later)
- Test sample data initialization

### Test Data Builders
```java
public class UserTestBuilder {
    public static User aValidUser() {
        return new User(null, "Test", "User", "test@example.com", "555-0000");
    }
    
    public static CreateUserRequest aValidCreateRequest() {
        return new CreateUserRequest("Test", "User", "test@example.com", "555-0000");
    }
}
```
