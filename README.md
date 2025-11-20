# Struts Demo Application

A sample Struts 2 web application demonstrating MVC architecture with JSP views, Struts actions, and an in-memory H2 database.

## Features

- **Struts 2 Framework**: Implements MVC pattern with actions and interceptors
- **JSP Pages**: Dynamic web pages using Struts tags and JSTL
- **In-Memory Database**: H2 database for data persistence (automatically initialized)
- **User Management**: Complete CRUD operations for user entities
- **Form Validation**: Server-side validation with error handling
- **Responsive Design**: Clean, modern web interface

## Project Structure

```
struts-demo/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/example/
│   │   │       ├── action/          # Struts action classes
│   │   │       ├── model/           # Domain model classes
│   │   │       └── service/         # Business logic services
│   │   ├── resources/
│   │   │   └── struts.xml          # Struts configuration
│   │   └── webapp/
│   │       ├── WEB-INF/
│   │       │   └── web.xml         # Web application configuration
│   │       ├── common/             # Shared JSP includes
│   │       ├── index.jsp           # Home page
│   │       ├── user-list.jsp       # User listing page
│   │       └── user-form.jsp       # User create/edit form
└── pom.xml                         # Maven configuration
```

## Technologies Used

- **Java 8+**
- **Apache Struts 2.5.30**
- **H2 Database 2.2.224**
- **Maven 3.x**
- **JSP & JSTL**
- **HTML5 & CSS3**

## Prerequisites

- Java Development Kit (JDK) 8 or higher
- Apache Maven 3.x
- Web browser

## Getting Started

### 1. Clone or Download the Project

Make sure all project files are in the `struts-demo` directory.

### 2. Build the Project

Open a terminal/command prompt in the project root directory and run:

```bash
mvn clean compile
```

### 3. Run the Application

You can run the application using the embedded Jetty server:

```bash
mvn jetty:run
```

The application will be available at: `http://localhost:8080/struts-demo`

### 4. Alternative: Deploy to Tomcat

If you prefer to deploy to Tomcat:

1. Build the WAR file:
   ```bash
   mvn clean package
   ```

2. Copy the generated `target/struts-demo.war` to your Tomcat `webapps` directory

3. Start Tomcat and access: `http://localhost:8080/struts-demo`

## Application Features

### Home Page
- Welcome page with application overview
- Navigation links to main features

### User Management
- **View Users**: List all users in a table format
- **Add User**: Create new users with form validation
- **Edit User**: Modify existing user information
- **Delete User**: Remove users with confirmation

### Database
- H2 in-memory database automatically initialized
- Sample data populated on startup
- Users table with fields: ID, First Name, Last Name, Email, Phone

## Configuration Files

### struts.xml
Contains Struts action mappings and configuration:
- Action definitions
- Result mappings
- Global constants

### web.xml
Web application deployment descriptor:
- Struts filter configuration
- Welcome file list
- Session configuration

## Code Structure

### Actions
- `IndexAction`: Handles home page requests
- `UserAction`: Manages all user-related operations (CRUD)

### Services
- `UserService`: Singleton service managing database operations
- Handles H2 database initialization and data access

### Models
- `User`: Entity class representing user data

## Development Notes

### Database Configuration
The H2 database is configured as in-memory with the connection string:
```
jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1
```

### Sample Data
The application automatically creates sample users on startup:
- John Doe (john.doe@example.com)
- Jane Smith (jane.smith@example.com) 
- Mike Johnson (mike.johnson@example.com)

### Validation
Server-side validation is implemented for:
- Required fields (First Name, Last Name, Email)
- Email format validation
- Error messages displayed on form

## Troubleshooting

### Common Issues

1. **Port 8080 already in use**
   - Change the port in `pom.xml` under the Jetty plugin configuration
   - Or stop the process using port 8080

2. **Maven dependencies not downloading**
   - Check internet connection
   - Run `mvn clean install -U` to force update

3. **JSP compilation errors**
   - Ensure proper JSP syntax
   - Check Struts tag library declarations

### Logs
When running with Jetty, check the console output for:
- Application startup messages
- Database initialization logs
- Error messages and stack traces

## Extending the Application

### Adding New Features
1. Create new action classes in `com.example.action`
2. Add action mappings in `struts.xml`
3. Create corresponding JSP views
4. Update navigation in header.jsp

### Database Changes
1. Modify the table creation in `UserService.initializeDatabase()`
2. Update the `User` model class
3. Adjust service methods for new fields
4. Update JSP forms and displays

## License

This is a demo application for educational purposes.# struts-demo
