# Research: Struts to Spring Boot Migration

**Phase**: 0 - Outline & Research  
**Date**: November 20, 2025

## Overview

This document consolidates research findings for migrating a Struts 2 + JSP application to Spring Boot + React.JS. The migration involves replacing server-side rendering (JSP) with a REST API backend and modern React frontend.

## Technology Stack Decisions

### Decision: Spring Boot 3.2.x with Java 17

**Rationale**:
- Spring Boot 3.2.x is the latest stable LTS release with strong community support
- Java 17 is the current LTS version, providing modern language features while maintaining stability
- Seamless integration with Spring Data JPA for database operations
- Built-in embedded server (Tomcat) eliminates need for external application server
- Auto-configuration reduces boilerplate significantly compared to legacy Spring configurations

**Alternatives considered**:
- **Spring Boot 2.7.x with Java 11**: More conservative but nearing end-of-life; missed opportunity for modern features
- **Micronaut**: Steeper learning curve, smaller ecosystem, less suitable for straightforward migration
- **Quarkus**: Better for cloud-native/Kubernetes but overkill for this use case

### Decision: React 18.x with TypeScript

**Rationale**:
- React 18 provides modern concurrent rendering features
- TypeScript adds type safety, reducing runtime errors and improving maintainability
- Vite as build tool offers faster development experience than Create React App
- React Hooks pattern simplifies state management without Redux (appropriate for this scale)
- Large ecosystem of components and tooling

**Alternatives considered**:
- **Vue.js 3**: Simpler learning curve but smaller ecosystem; team familiarity factor
- **Angular 17**: More opinionated and heavier; overkill for CRUD application
- **Plain JavaScript React**: Loses type safety benefits critical for migration confidence

### Decision: Spring Data JPA with H2 Database

**Rationale**:
- Spring Data JPA eliminates boilerplate JDBC code (current pain point in UserService)
- Repository pattern provides clean abstraction over data access
- H2 in-memory database maintains existing behavior (sample data on startup)
- JPA entities map naturally to existing User model
- Transaction management handled automatically

**Alternatives considered**:
- **Plain JDBC with JdbcTemplate**: Still requires significant boilerplate compared to JPA
- **jOOQ**: Type-safe SQL but adds complexity; overkill for simple CRUD
- **MyBatis**: XML-based mapping less intuitive than JPA annotations

### Decision: REST API Design with OpenAPI Specification

**Rationale**:
- RESTful endpoints map naturally to existing Struts actions (list, create, update, delete)
- JSON as data format enables clean React integration
- OpenAPI spec provides contract-first development and documentation
- Standard HTTP status codes (200, 201, 400, 404, 500) for operation results
- Stateless design enables better scalability than session-based Struts

**Alternatives considered**:
- **GraphQL**: More complex; unnecessary for straightforward CRUD operations
- **gRPC**: Binary protocol less suitable for browser-based frontend
- **Server-sent Events/WebSockets**: Real-time features not required by specification

## Migration Strategy

### Decision: Backend-First Migration with Dual Running Period

**Rationale**:
- Backend API can be developed and tested independently
- Allows validation of business logic migration before frontend work
- Backend can be deployed and tested with tools like Postman/curl
- React frontend consumes well-defined API contract
- Reduces risk by isolating concerns

**Alternatives considered**:
- **Big Bang Rewrite**: High risk, no incremental validation
- **Frontend-First**: Backend API contract unknown, causes rework
- **Parallel Maintenance**: Duplicated effort maintaining both systems

## Best Practices Research

### Spring Boot REST Controllers

**Pattern**: Use `@RestController` with method-level `@RequestMapping` annotations
- `@GetMapping` for retrieving data (list all, get by ID)
- `@PostMapping` for creating new resources
- `@PutMapping` for updating existing resources
- `@DeleteMapping` for removing resources

**Validation**: Use Bean Validation (`@Valid`, `@NotBlank`, `@Email`) on request DTOs
- Automatic validation before method execution
- Returns 400 Bad Request with error details for invalid input

**Error Handling**: Implement `@ControllerAdvice` for global exception handling
- Consistent error response format across all endpoints
- Maps exceptions to appropriate HTTP status codes

### React Component Architecture

**Pattern**: Functional components with Hooks
- `useState` for component-level state (form data, loading states)
- `useEffect` for side effects (API calls, data fetching)
- Custom hooks for reusable logic (useUsers, useUserForm)

**State Management**: Component-level state sufficient for this application
- No Redux needed for simple CRUD operations
- Prop drilling avoided with composition patterns

**API Integration**: Axios for HTTP client
- Centralized API service module for all backend calls
- Consistent error handling and response transformation
- TypeScript interfaces matching backend DTOs

### Testing Strategy

**Backend Testing**:
- JUnit 5 for unit tests (`@SpringBootTest` for integration tests)
- MockMvc for REST controller testing without starting server
- Testcontainers if integration with real database needed (optional)

**Frontend Testing**:
- Jest for unit tests
- React Testing Library for component tests (user-centric approach)
- Vitest as faster alternative to Jest (Vite-native)

## Build and Development Tools

### Decision: Maven for Backend, Vite for Frontend

**Rationale**:
- Maven already used in existing project (continuity)
- Spring Initializr generates Maven projects by default
- Vite provides near-instant HMR (Hot Module Replacement) for React development
- Vite has simpler configuration than Webpack

**Development Workflow**:
- Backend runs on `localhost:8080` (Spring Boot embedded Tomcat)
- Frontend dev server on `localhost:5173` (Vite default) with proxy to backend
- CORS configuration in Spring Boot for development mode

## Database Migration

### Decision: Maintain H2 In-Memory with Spring Boot Auto-Configuration

**Rationale**:
- Preserves existing behavior (database initializes on startup)
- Spring Boot auto-configures H2 console for debugging
- JPA schema generation from entities (no manual DDL scripts)
- Sample data via `data.sql` or `@PostConstruct` method

**Schema Mapping**:
```
Existing Table: users
├── id (BIGINT, AUTO_INCREMENT)
├── first_name (VARCHAR(50))
├── last_name (VARCHAR(50))
├── email (VARCHAR(100), UNIQUE)
└── phone (VARCHAR(20))

Maps to JPA Entity: User
├── @Id @GeneratedValue(strategy = IDENTITY)
├── @Column(name = "first_name")
├── @Column(name = "last_name")
├── @Column(name = "email", unique = true)
└── @Column(name = "phone")
```

## Validation Rules Migration

### Current Struts Validation → Spring Boot Bean Validation

| Struts Validation | Spring Boot Equivalent |
|-------------------|------------------------|
| `required="true"` for firstName | `@NotBlank(message = "First name is required")` |
| `required="true"` for lastName | `@NotBlank(message = "Last name is required")` |
| `type="email"` for email | `@Email(message = "Email must be valid")` + `@NotBlank` |
| Email uniqueness check | Custom validation or service-level check before save |
| Phone optional | No annotation (nullable by default) |

## Security Considerations

### Decision: Basic Security Configuration for Development

**Rationale**:
- Current Struts app has no authentication/authorization
- Migration focuses on functional parity, not adding new features
- Spring Security can be added later without changing API contracts
- CORS configuration required for local development (frontend/backend separate origins)

**Future Enhancement Path**:
- Spring Security with JWT for stateless authentication
- OAuth2 integration for enterprise SSO
- Role-based access control (RBAC) for admin features

## Performance Targets

### Baseline from Current Application

- **Page Load**: Current JSP renders in ~500ms (measured via browser DevTools)
- **Target**: React SPA initial load <2s, subsequent navigation <200ms
- **API Response**: Target <500ms for all CRUD operations (current JDBC performance baseline)
- **Concurrent Users**: H2 in-memory supports 100+ concurrent connections (sufficient for demo)

## Unknowns Resolved

1. **Java Version**: Upgrade from Java 8 to Java 17 (current LTS, required for Spring Boot 3.x)
2. **Build Tool**: Maven (continuity with existing project)
3. **Frontend Framework**: React 18 with TypeScript and Vite
4. **Database**: H2 remains, accessed via Spring Data JPA
5. **API Style**: RESTful JSON APIs (industry standard for SPA backends)
6. **Testing**: JUnit 5 + MockMvc (backend), Jest/Vitest + React Testing Library (frontend)
7. **Packaging**: JAR with embedded Tomcat (Spring Boot default, simpler than WAR)

## References

- [Spring Boot 3.2 Documentation](https://docs.spring.io/spring-boot/docs/3.2.x/reference/htmlsingle/)
- [Spring Data JPA Reference](https://docs.spring.io/spring-data/jpa/docs/current/reference/html/)
- [React 18 Documentation](https://react.dev/)
- [Vite Documentation](https://vitejs.dev/)
- [OpenAPI 3.0 Specification](https://swagger.io/specification/)
