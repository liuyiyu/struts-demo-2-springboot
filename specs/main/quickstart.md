# Quickstart Guide: Migrated User Management Application

**Last Updated**: November 20, 2025  
**Version**: 1.0.0 (Spring Boot + React)

## Overview

This guide provides step-by-step instructions for building, running, and testing the migrated user management application. The application consists of a Spring Boot backend (REST API) and a React frontend (single-page application).

## Prerequisites

### Required Software

- **Java Development Kit (JDK)**: Version 17 or higher
  - Verify: `java -version`
  - Download: [Oracle JDK](https://www.oracle.com/java/technologies/downloads/) or [OpenJDK](https://adoptium.net/)

- **Maven**: Version 3.8 or higher
  - Verify: `mvn -version`
  - Download: [Apache Maven](https://maven.apache.org/download.cgi)

- **Node.js**: Version 18 or higher (includes npm)
  - Verify: `node -version` and `npm -version`
  - Download: [Node.js](https://nodejs.org/)

- **Git**: For version control
  - Verify: `git --version`

### Optional Tools

- **IDE**: IntelliJ IDEA, Eclipse, or VS Code with Java extensions
- **API Testing**: Postman, Insomnia, or curl
- **Browser**: Modern browser (Chrome, Firefox, Safari, Edge)

## Project Structure

```
pstruts2springboot/
├── backend/                    # Spring Boot application
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/
│   │   │   │   └── com/example/usermanagement/
│   │   │   │       ├── UserManagementApplication.java
│   │   │   │       ├── controller/
│   │   │   │       │   └── UserController.java
│   │   │   │       ├── service/
│   │   │   │       │   └── UserService.java
│   │   │   │       ├── repository/
│   │   │   │       │   └── UserRepository.java
│   │   │   │       ├── model/
│   │   │   │       │   └── User.java
│   │   │   │       ├── dto/
│   │   │   │       │   ├── UserDTO.java
│   │   │   │       │   ├── CreateUserRequest.java
│   │   │   │       │   └── UpdateUserRequest.java
│   │   │   │       └── exception/
│   │   │   │           └── GlobalExceptionHandler.java
│   │   │   └── resources/
│   │   │       ├── application.properties
│   │   │       └── data.sql
│   │   └── test/
│   │       └── java/
│   └── pom.xml
├── frontend/                   # React application
│   ├── src/
│   │   ├── components/
│   │   │   ├── UserList.tsx
│   │   │   └── UserForm.tsx
│   │   ├── services/
│   │   │   └── userService.ts
│   │   ├── types/
│   │   │   └── User.ts
│   │   ├── App.tsx
│   │   └── main.tsx
│   ├── package.json
│   └── vite.config.ts
└── README.md
```

## Backend Setup (Spring Boot)

### Step 1: Build the Backend

```bash
# Navigate to backend directory
cd backend

# Clean and build the project
mvn clean install

# This will:
# - Compile Java sources
# - Run unit tests
# - Package as JAR file
# - Output: target/user-management-1.0.0.jar
```

### Step 2: Run the Backend

```bash
# Option 1: Using Maven (development)
mvn spring-boot:run

# Option 2: Using JAR file (production-like)
java -jar target/user-management-1.0.0.jar

# The backend will start on http://localhost:8080
```

### Step 3: Verify Backend is Running

```bash
# Test the health endpoint
curl http://localhost:8080/actuator/health

# Expected response: {"status":"UP"}

# Test get all users
curl http://localhost:8080/api/users

# Expected response: JSON array with 3 sample users
```

### Backend Configuration

The backend can be configured via `src/main/resources/application.properties`:

```properties
# Server Configuration
server.port=8080

# Database Configuration (H2 in-memory)
spring.datasource.url=jdbc:h2:mem:usersdb
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=

# JPA Configuration
spring.jpa.database-platform=org.hibernate.dialect.H2Dialect
spring.jpa.hibernate.ddl-auto=create-drop
spring.jpa.show-sql=false

# H2 Console (for debugging)
spring.h2.console.enabled=true
spring.h2.console.path=/h2-console

# CORS Configuration (for local development)
cors.allowed-origins=http://localhost:5173
```

## Frontend Setup (React)

### Step 1: Install Dependencies

```bash
# Navigate to frontend directory
cd frontend

# Install npm packages
npm install

# This installs:
# - React 18
# - TypeScript
# - Vite
# - Axios
# - React Router
# - Other dependencies
```

### Step 2: Configure API Base URL

Edit `src/services/userService.ts` to set the backend URL:

```typescript
const API_BASE_URL = 'http://localhost:8080/api';
```

### Step 3: Run the Frontend

```bash
# Start development server
npm run dev

# The frontend will start on http://localhost:5173
# Opens automatically in your default browser
```

### Step 4: Build for Production

```bash
# Create optimized production build
npm run build

# Output: dist/ directory
# Can be served by any static file server
```

## Running the Complete Application

### Development Mode

1. **Terminal 1 - Backend**:
   ```bash
   cd backend
   mvn spring-boot:run
   ```

2. **Terminal 2 - Frontend**:
   ```bash
   cd frontend
   npm run dev
   ```

3. **Access the Application**:
   - Frontend UI: http://localhost:5173
   - Backend API: http://localhost:8080/api
   - H2 Console: http://localhost:8080/h2-console

### Production Mode

```bash
# Build backend
cd backend
mvn clean package

# Build frontend
cd ../frontend
npm run build

# Deploy backend JAR to server
# Deploy frontend dist/ to static file server or serve via Spring Boot
```

## Testing

### Backend Tests

```bash
cd backend

# Run all tests
mvn test

# Run specific test class
mvn test -Dtest=UserServiceTest

# Run with coverage
mvn test jacoco:report
# View report at target/site/jacoco/index.html
```

### Frontend Tests

```bash
cd frontend

# Run unit tests
npm run test

# Run tests in watch mode
npm run test:watch

# Run with coverage
npm run test:coverage
```

### Integration Testing

```bash
# Start backend
cd backend && mvn spring-boot:run

# In another terminal, test API endpoints
curl -X GET http://localhost:8080/api/users
curl -X POST http://localhost:8080/api/users \
  -H "Content-Type: application/json" \
  -d '{"firstName":"Test","lastName":"User","email":"test@example.com","phone":"555-0000"}'
```

## API Documentation

### Swagger UI (Interactive API Docs)

When the backend is running, access Swagger UI:
- URL: http://localhost:8080/swagger-ui.html
- Provides interactive API testing interface

### OpenAPI Specification

The OpenAPI spec is available at:
- URL: http://localhost:8080/v3/api-docs
- File: `specs/main/contracts/user-api.yaml`

## Database Access

### H2 Console

1. Navigate to: http://localhost:8080/h2-console
2. Connection settings:
   - **JDBC URL**: `jdbc:h2:mem:usersdb`
   - **Username**: `sa`
   - **Password**: (leave empty)
3. Click "Connect"
4. Run SQL queries:
   ```sql
   SELECT * FROM users;
   ```

## Common Operations

### Create a User

**Via curl**:
```bash
curl -X POST http://localhost:8080/api/users \
  -H "Content-Type: application/json" \
  -d '{
    "firstName": "Alice",
    "lastName": "Brown",
    "email": "alice.brown@example.com",
    "phone": "555-0104"
  }'
```

**Via Frontend**:
1. Click "Add New User" button
2. Fill form fields
3. Click "Save User"

### Update a User

**Via curl**:
```bash
curl -X PUT http://localhost:8080/api/users/1 \
  -H "Content-Type: application/json" \
  -d '{
    "firstName": "John",
    "lastName": "Doe Updated",
    "email": "john.updated@example.com",
    "phone": "555-9999"
  }'
```

**Via Frontend**:
1. Click "Edit" button for a user
2. Modify form fields
3. Click "Save User"

### Delete a User

**Via curl**:
```bash
curl -X DELETE http://localhost:8080/api/users/1
```

**Via Frontend**:
1. Click "Delete" button for a user
2. Confirm deletion in dialog

## Troubleshooting

### Backend Won't Start

**Issue**: Port 8080 already in use
```
Caused by: java.net.BindException: Address already in use
```

**Solution**:
```bash
# Windows
netstat -ano | findstr :8080
taskkill /PID <PID> /F

# Linux/Mac
lsof -ti:8080 | xargs kill -9

# Or change port in application.properties
server.port=8081
```

### Frontend Can't Connect to Backend

**Issue**: CORS error in browser console
```
Access to XMLHttpRequest blocked by CORS policy
```

**Solution**:
- Verify backend is running on http://localhost:8080
- Check CORS configuration in backend application.properties
- Ensure frontend API_BASE_URL matches backend URL

### Database Not Initializing

**Issue**: Sample users not appearing

**Solution**:
- Check `data.sql` exists in `src/main/resources/`
- Verify `spring.jpa.hibernate.ddl-auto=create-drop` in application.properties
- Check backend console logs for SQL execution errors

### Tests Failing

**Issue**: Unit tests fail after code changes

**Solution**:
```bash
# Clean and rebuild
mvn clean install -DskipTests

# Run tests individually
mvn test -Dtest=UserControllerTest

# Check for:
# - Incorrect test data
# - Missing mock configurations
# - Database state issues
```

## Performance Tuning

### Backend

- Enable connection pooling for production:
  ```properties
  spring.datasource.hikari.maximum-pool-size=10
  ```

- Disable SQL logging in production:
  ```properties
  spring.jpa.show-sql=false
  ```

### Frontend

- Use production build:
  ```bash
  npm run build
  # Enables minification, tree-shaking, code splitting
  ```

- Enable React production mode:
  ```bash
  NODE_ENV=production npm run build
  ```

## Next Steps

- **Add Authentication**: Integrate Spring Security with JWT
- **Add Pagination**: Implement pagination for user list API
- **Add Search**: Add filtering and search capabilities
- **Deploy**: Deploy to cloud platform (AWS, Azure, Heroku)
- **Monitoring**: Add application monitoring (Actuator, Prometheus)

## Support

For issues or questions:
- Check existing tests for usage examples
- Review API documentation at `/swagger-ui.html`
- Consult Spring Boot and React documentation
