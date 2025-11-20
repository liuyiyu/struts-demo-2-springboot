# Tasks: Struts to Spring Boot Migration

**Input**: Design documents from `/specs/main/`
**Prerequisites**: plan.md ✓, spec.md ✓, research.md ✓, data-model.md ✓, contracts/ ✓

**Tests**: Tests are NOT explicitly requested in the specification, so test tasks are omitted. Focus is on code-level changes required for successful compilation.

**Organization**: Tasks are grouped by user story to enable independent implementation and testing of each story.

## Format: `[ID] [P?] [Story] Description`

- **[P]**: Can run in parallel (different files, no dependencies)
- **[Story]**: Which user story this task belongs to (e.g., US1, US2, US3, US4)
- Include exact file paths in descriptions

---

## Phase 1: Setup (Shared Infrastructure)

**Purpose**: Project initialization and basic structure for both backend and frontend

- [X] T001 Create backend project structure: backend/src/main/java/com/example/usermanagement/ with controller/, service/, repository/, model/, dto/, exception/ subdirectories
- [X] T002 Create backend/pom.xml with Spring Boot 3.2.x parent, spring-boot-starter-web, spring-boot-starter-data-jpa, h2 database, validation-api dependencies
- [X] T003 [P] Create backend/src/main/resources/application.properties with H2 database configuration, JPA settings, server port 8080, CORS configuration
- [X] T004 [P] Create frontend project structure using Vite: frontend/src/ with components/, services/, types/ subdirectories
- [X] T005 [P] Create frontend/package.json with React 18, TypeScript 5, Vite 5, Axios, React Router dependencies
- [X] T006 [P] Create frontend/tsconfig.json and frontend/vite.config.ts configuration files
- [X] T007 Create backend/src/main/java/com/example/usermanagement/UserManagementApplication.java as Spring Boot main class with @SpringBootApplication

---

## Phase 2: Foundational (Blocking Prerequisites)

**Purpose**: Core infrastructure that MUST be complete before ANY user story can be implemented

**⚠️ CRITICAL**: No user story work can begin until this phase is complete

- [X] T008 Create User JPA entity in backend/src/main/java/com/example/usermanagement/model/User.java with @Entity, @Table(name="users"), @Id, @GeneratedValue, all fields with @Column annotations
- [X] T009 Add Bean Validation annotations to User entity (@NotBlank, @Email, @Size) matching data-model.md specification
- [X] T010 [P] Create UserDTO in backend/src/main/java/com/example/usermanagement/dto/UserDTO.java for API responses with all User fields
- [X] T011 [P] Create CreateUserRequest DTO in backend/src/main/java/com/example/usermanagement/dto/CreateUserRequest.java with validation annotations
- [X] T012 [P] Create UpdateUserRequest DTO in backend/src/main/java/com/example/usermanagement/dto/UpdateUserRequest.java with validation annotations
- [X] T013 Create UserRepository interface in backend/src/main/java/com/example/usermanagement/repository/UserRepository.java extending JpaRepository<User, Long> with findByEmail and existsByEmail methods
- [X] T014 Create GlobalExceptionHandler in backend/src/main/java/com/example/usermanagement/exception/GlobalExceptionHandler.java with @ControllerAdvice, handling MethodArgumentNotValidException, DataIntegrityViolationException, EntityNotFoundException
- [X] T015 [P] Create backend/src/main/resources/data.sql with INSERT statements for 3 sample users (John Doe, Jane Smith, Mike Johnson)
- [X] T016 [P] Create User TypeScript interface in frontend/src/types/User.ts matching UserDTO structure
- [X] T017 [P] Create userService.ts API client in frontend/src/services/userService.ts with Axios instance configured for http://localhost:8080/api base URL

**Checkpoint**: Foundation ready - user story implementation can now begin in parallel

---

## Phase 3: User Story 1 - View User List (Priority: P1) 🎯 MVP

**Goal**: Users can view all registered users in a table with ID, first name, last name, email, and phone

**Independent Test**: Navigate to application root, verify table displays 3 sample users with all fields visible

### Implementation for User Story 1

- [X] T018 [P] [US1] Implement UserService.getAllUsers() method in backend/src/main/java/com/example/usermanagement/service/UserService.java calling userRepository.findAll() and mapping to UserDTO list
- [X] T019 [US1] Implement GET /api/users endpoint in backend/src/main/java/com/example/usermanagement/controller/UserController.java with @RestController, @RequestMapping("/api/users"), @GetMapping returning List<UserDTO>
- [X] T020 [US1] Add getAllUsers() method to frontend/src/services/userService.ts making GET request to /api/users endpoint
- [X] T021 [US1] Create UserList component in frontend/src/components/UserList.tsx with useState, useEffect, table rendering all user fields, loading state, error handling
- [X] T022 [US1] Add empty state message in UserList.tsx when users array is empty, with text "No users found. Add the first user" and link
- [X] T023 [US1] Create App.tsx in frontend/src/App.tsx as main component rendering UserList with basic layout and styling
- [X] T024 [US1] Create main.tsx entry point in frontend/src/main.tsx with ReactDOM.createRoot and App component rendering
- [X] T025 [US1] Create frontend/public/index.html with root div and app title

**Checkpoint**: User Story 1 complete - can view all users in table independently

---

## Phase 4: User Story 2 - Create New User (Priority: P2)

**Goal**: Users can add new users via form with validation and see them in the list

**Independent Test**: Click "Add New User", fill form with valid data, submit, verify new user appears in list

### Implementation for User Story 2

- [X] T026 [P] [US2] Implement UserService.createUser(CreateUserRequest request) method in backend/src/main/java/com/example/usermanagement/service/UserService.java with email uniqueness check, entity mapping, save, and DTO return
- [X] T027 [US2] Implement POST /api/users endpoint in backend/src/main/java/com/example/usermanagement/controller/UserController.java with @PostMapping, @Valid @RequestBody CreateUserRequest, @ResponseStatus(HttpStatus.CREATED)
- [X] T028 [US2] Add createUser(userData) method to frontend/src/services/userService.ts making POST request with user data
- [X] T029 [US2] Create UserForm component in frontend/src/components/UserForm.tsx with form state (firstName, lastName, email, phone), input fields, validation state, onSubmit handler, onCancel handler
- [X] T030 [US2] Add form validation to UserForm.tsx checking required fields (firstName, lastName, email), email format, displaying field-level errors
- [X] T031 [US2] Add server error handling to UserForm.tsx parsing validation errors from backend, displaying field-specific and general errors
- [X] T032 [US2] Add "Add New User" button to UserList.tsx component that shows UserForm component
- [X] T033 [US2] Implement form submission in UserForm.tsx calling userService.createUser(), handling success (show message, redirect to list), handling errors (display validation messages)
- [X] T034 [US2] Add cancel button handler in UserForm.tsx to return to list without saving
- [X] T035 [US2] Add success message display in UserList.tsx after successful user creation

**Checkpoint**: User Stories 1 AND 2 complete - can view and create users independently

---

## Phase 5: User Story 3 - Update Existing User (Priority: P3)

**Goal**: Users can edit existing users, pre-fill form with current data, save changes

**Independent Test**: Click "Edit" on a user, modify fields, save, verify changes appear in list

### Implementation for User Story 3

- [X] T036 [P] [US3] Implement UserService.getUserById(Long id) method in backend/src/main/java/com/example/usermanagement/service/UserService.java with Optional handling, throwing EntityNotFoundException if not found
- [X] T037 [US3] Implement GET /api/users/{id} endpoint in backend/src/main/java/com/example/usermanagement/controller/UserController.java with @GetMapping("/{id}"), @PathVariable Long id
- [X] T038 [US3] Implement UserService.updateUser(Long id, UpdateUserRequest request) method in backend/src/main/java/com/example/usermanagement/service/UserService.java finding user, checking email uniqueness (excluding current user), updating fields, saving
- [X] T039 [US3] Implement PUT /api/users/{id} endpoint in backend/src/main/java/com/example/usermanagement/controller/UserController.java with @PutMapping("/{id}"), @PathVariable, @Valid @RequestBody
- [X] T040 [US3] Add getUserById(id) method to frontend/src/services/userService.ts making GET request to /api/users/{id}
- [X] T041 [US3] Add updateUser(id, userData) method to frontend/src/services/userService.ts making PUT request to /api/users/{id}
- [X] T042 [US3] Modify UserForm.tsx to accept optional user prop for edit mode, loading user data via useEffect when userId provided
- [X] T043 [US3] Update UserForm.tsx to call userService.updateUser() instead of createUser() when in edit mode
- [X] T044 [US3] Add "Edit" button to each user row in UserList.tsx table that opens UserForm with userId prop
- [X] T045 [US3] Update UserForm.tsx to display "Edit User" vs "Add New User" heading based on mode
- [X] T046 [US3] Add success message display after successful user update in UserList.tsx

**Checkpoint**: User Stories 1, 2, AND 3 complete - can view, create, and edit users independently

---

## Phase 6: User Story 4 - Delete User (Priority: P4)

**Goal**: Users can delete users with confirmation dialog, see updated list

**Independent Test**: Click "Delete" on a user, confirm, verify user removed from list

### Implementation for User Story 4

- [X] T047 [P] [US4] Implement UserService.deleteUser(Long id) method in backend/src/main/java/com/example/usermanagement/service/UserService.java checking if user exists, calling repository.deleteById()
- [X] T048 [US4] Implement DELETE /api/users/{id} endpoint in backend/src/main/java/com/example/usermanagement/controller/UserController.java with @DeleteMapping("/{id}"), @ResponseStatus(HttpStatus.NO_CONTENT)
- [X] T049 [US4] Add deleteUser(id) method to frontend/src/services/userService.ts making DELETE request to /api/users/{id}
- [X] T050 [US4] Add "Delete" button to each user row in UserList.tsx table with onClick handler
- [X] T051 [US4] Implement delete confirmation dialog in UserList.tsx using window.confirm() with message "Are you sure you want to delete this user?"
- [X] T052 [US4] Implement delete handler in UserList.tsx calling userService.deleteUser(id), removing user from state on success, refreshing list
- [X] T053 [US4] Add error handling for delete operation in UserList.tsx displaying appropriate error message if delete fails

**Checkpoint**: All user stories complete - full CRUD functionality operational

---

## Phase 7: Polish & Cross-Cutting Concerns

**Purpose**: Final improvements ensuring production readiness

- [ ] T054 [P] Add CORS configuration to UserController or create WebConfig class allowing http://localhost:5173 origin for development
- [ ] T055 [P] Add CSS styling to frontend components for professional appearance (table borders, button styles, form layout)
- [ ] T056 [P] Add loading spinners to UserList.tsx and UserForm.tsx during API calls
- [ ] T057 [P] Verify application.properties has correct H2 configuration (jdbc:h2:mem:usersdb, ddl-auto=create-drop)
- [ ] T058 Test complete application flow following quickstart.md: build backend with mvn clean install, build frontend with npm install && npm run build
- [ ] T059 Verify application starts successfully: backend on localhost:8080, frontend dev server on localhost:5173
- [ ] T060 Manual testing of all user stories: view list, create user, edit user, delete user with all edge cases
- [ ] T061 Verify sample data loads correctly on application startup (3 users: John, Jane, Mike)
- [ ] T062 [P] Update README.md with migration notes, new build/run instructions, architecture overview
- [ ] T063 Code review and cleanup: remove unused imports, format code, check for compilation warnings

---

## Dependencies & Execution Order

### Phase Dependencies

- **Setup (Phase 1)**: No dependencies - can start immediately
  - Creates project structure for backend and frontend
- **Foundational (Phase 2)**: Depends on Setup (T001-T007) completion - BLOCKS all user stories
  - Creates data model, DTOs, repository, error handling, sample data
- **User Story 1 (Phase 3)**: Depends on Foundational (T008-T017) - READ operations
  - First story to implement, serves as foundation for all others
- **User Story 2 (Phase 4)**: Depends on Foundational + US1 (for list display after create)
  - CREATE operations, builds on list display from US1
- **User Story 3 (Phase 5)**: Depends on Foundational + US1 (for list display) + US2 (form component reuse)
  - UPDATE operations, reuses form from US2
- **User Story 4 (Phase 6)**: Depends on Foundational + US1 (for list display and refresh after delete)
  - DELETE operations, updates list from US1
- **Polish (Phase 7)**: Depends on all user stories (T018-T053) being complete
  - Final validation and production readiness

### Critical Path

```
Setup (T001-T007)
  ↓
Foundational (T008-T017) ← BLOCKS everything
  ↓
├─ US1: View List (T018-T025) ← MVP, highest priority
│    ↓
├─ US2: Create User (T026-T035) ← Depends on US1 for list display
│    ↓
├─ US3: Update User (T036-T046) ← Depends on US1 & US2
│    ↓
└─ US4: Delete User (T047-T053) ← Depends on US1
     ↓
Polish (T054-T063) ← Final validation
```

### Parallel Opportunities Within Phases

**Setup Phase (All parallel after T001-T002)**:
- T003 (backend config) || T004-T006 (frontend setup) can run simultaneously
- T007 (Spring Boot main) depends on T001-T002

**Foundational Phase**:
- T010-T012 (all DTOs) can run in parallel after T008-T009 (User entity)
- T015-T017 (data.sql, TS types, API client) can run in parallel after T008-T013
- T008-T009 must complete before T013 (repository needs entity)

**User Story Implementation**:
- Backend work (service + controller) can proceed in parallel with frontend work (component) for the same story
- Within backend: service method first, then controller endpoint
- Within frontend: API client method first, then component using it

**Polish Phase**:
- T054-T057 (config and styling) can all run in parallel
- T058-T063 must be sequential (build → start → test → document)

### Recommended Execution Strategy

**MVP First (User Story 1)**:
1. Complete Setup and Foundational phases fully
2. Implement US1 end-to-end (backend + frontend)
3. Verify US1 works independently
4. This gives you a working application showing data

**Incremental Feature Addition**:
5. Implement US2 (create) - now can view and add
6. Implement US3 (update) - now can view, add, edit
7. Implement US4 (delete) - full CRUD complete
8. Polish phase

**Parallel Development (if multiple developers)**:
- Developer 1: Setup + Foundational + US1 backend
- Developer 2: Setup + Foundational + US1 frontend
- After US1: Dev 1 takes US2+US4, Dev 2 takes US3+Polish

### Task Count Summary

- **Phase 1 (Setup)**: 7 tasks
- **Phase 2 (Foundational)**: 10 tasks ← CRITICAL BLOCKER
- **Phase 3 (US1 - View)**: 8 tasks ← MVP
- **Phase 4 (US2 - Create)**: 10 tasks
- **Phase 5 (US3 - Update)**: 11 tasks
- **Phase 6 (US4 - Delete)**: 7 tasks
- **Phase 7 (Polish)**: 10 tasks

**Total**: 63 tasks

**Estimated Effort**:
- Setup + Foundational: ~8-12 hours
- US1 (MVP): ~6-8 hours
- US2-US4: ~20-25 hours
- Polish: ~4-6 hours
- **Total**: ~40-50 hours for complete migration

---

## Implementation Strategy

### Backend-First Approach

For each user story, complete backend implementation before frontend:

1. **Backend Service**: Business logic, validation, data access
2. **Backend Controller**: REST endpoint, request/response mapping
3. **Manual API Test**: Use curl or Postman to verify endpoint works
4. **Frontend Service**: API client method
5. **Frontend Component**: UI consuming API
6. **End-to-End Test**: Verify through browser

This approach reduces risk by validating backend independently before UI integration.

### Validation Strategy

Each phase has a checkpoint validating completion:

- **Phase 1**: Project structure exists, dependencies resolve
- **Phase 2**: Application starts without errors, sample data loads
- **Phase 3**: Can view user list in browser showing 3 users
- **Phase 4**: Can create new user and see it in list
- **Phase 5**: Can edit user and see changes in list
- **Phase 6**: Can delete user and see it removed from list
- **Phase 7**: Application builds, runs, passes manual testing

### Success Criteria Mapping

All tasks are designed to satisfy the specification success criteria:

- **SC-001 (Zero compilation errors)**: T058 validates build success
- **SC-002 (Functional parity)**: T060 validates all CRUD operations
- **SC-003 (List loads <2s)**: US1 implementation optimized
- **SC-004 (Create workflow <30s)**: US2 form is simple and direct
- **SC-005 (Validation errors <1s)**: Client + server validation
- **SC-006 (Startup <10s)**: Spring Boot embedded server
- **SC-007 (100+ concurrent)**: H2 + stateless REST supports this
- **SC-008 (Validation preserved)**: Bean Validation matches Struts rules
- **SC-009 (3 sample users)**: T015 creates data.sql
- **SC-010 (No vulnerabilities)**: Modern framework versions
- **SC-011 (Browser support)**: React 18 + modern build
- **SC-012 (API <500ms)**: In-memory DB, simple queries
