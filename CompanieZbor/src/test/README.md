# Test Documentation - CompanieZbor Application

## Overview
This document describes the comprehensive test suite for the CompanieZbor (Flight Company) REST API application. The application is built using Spring Boot, Hibernate, and H2 database, and includes full test coverage for all layers: Controllers (REST endpoints), Services (business logic), and integration tests.

## Test Architecture

### Test Types
1. **Integration Tests** - Testing REST controllers with full Spring context
2. **Unit Tests** - Testing service layer with mocked repositories (Mockito)

### Technologies Used
- **JUnit 5** - Testing framework
- **Mockito** - Mocking framework for unit tests
- **Spring Boot Test** - Integration testing support
- **MockMvc** - Testing REST controllers without starting HTTP server
- **H2 Database** - In-memory database for integration tests
- **Jackson** - JSON serialization/deserialization

## Test Configuration

### Test Database
Tests use an in-memory H2 database to avoid file locking issues and ensure test isolation:

```properties
spring.datasource.url=jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE
spring.jpa.hibernate.ddl-auto=create-drop
```

Key features:
- **In-memory database**: Each test run uses a fresh database
- **Auto schema creation**: Database schema is created automatically
- **No persistence**: Database is destroyed after tests complete
- **Transaction rollback**: Each test is wrapped in a transaction that rolls back

## Test Structure

### 1. Controller Tests (Integration Tests)

#### UserControllerTest
**Location**: `src/test/java/com/example/companiezbor/rest/UserControllerTest.java`

**Purpose**: Tests all REST endpoints for User management

**Test Coverage**:
- ✅ Get all users
- ✅ Get user by ID (exists/not exists)
- ✅ Create new user
- ✅ Update existing user
- ✅ Delete user
- ✅ User login functionality
- ✅ Admin vs regular user scenarios
- ✅ Edge cases (empty database, invalid IDs)

**Sample Tests**:
```java
@Test
void testGetAllUsers_ReturnsListOfUsers() // Tests GET /api/users

@Test
void testGetUserById_UserExists_ReturnsUser() // Tests GET /api/users/{id}

@Test
void testCreateUser_ValidUser_ReturnsCreated() // Tests POST /api/users

@Test
void testUpdateUser_UserExists_ReturnsUpdatedUser() // Tests PUT /api/users/{id}

@Test
void testDeleteUser_UserExists_ReturnsNoContent() // Tests DELETE /api/users/{id}

@Test
void testLogin_ValidCredentials_ReturnsUser() // Tests POST /api/users/login
```

**Key Features**:
- Uses `@SpringBootTest` for full application context
- Uses `@Transactional` for automatic rollback after each test
- Uses `MockMvc` to simulate HTTP requests
- Tests both successful and error scenarios
- Validates JSON responses using JSONPath

---

#### FlightControllerTest
**Location**: `src/test/java/com/example/companiezbor/rest/FlightControllerTest.java`

**Purpose**: Tests all REST endpoints for Flight management

**Test Coverage**:
- ✅ Get all flights
- ✅ Get flight by ID (exists/not exists)
- ✅ Create new flight
- ✅ Update existing flight (full and partial updates)
- ✅ Delete flight
- ✅ Search flights by departure and arrival
- ✅ Long-haul flight scenarios
- ✅ Price updates
- ✅ Multiple flight creation

**Sample Tests**:
```java
@Test
void testGetAllFlights_ReturnsListOfFlights() // Tests GET /api/flights

@Test
void testGetFlightById_FlightExists_ReturnsFlight() // Tests GET /api/flights/{id}

@Test
void testCreateFlight_ValidFlight_ReturnsCreated() // Tests POST /api/flights

@Test
void testUpdateFlight_FlightExists_ReturnsUpdatedFlight() // Tests PUT /api/flights/{id}

@Test
void testDeleteFlight_FlightExists_ReturnsNoContent() // Tests DELETE /api/flights/{id}

@Test
void testFindByDepartureAndArrival_FlightExists_ReturnsFlight() // Tests GET /api/flights/search
```

**Test Data Examples**:
- Bucharest → Paris (180 min, Boeing 737, €299.99)
- London → New York (480 min, Airbus A380, €599.99)
- Los Angeles → Singapore (960 min, Airbus A350, €1299.99)

---

#### ReservationControllerTest
**Location**: `src/test/java/com/example/companiezbor/rest/ReservationControllerTest.java`

**Purpose**: Tests all REST endpoints for Reservation management

**Test Coverage**:
- ✅ Get all reservations
- ✅ Get reservation by ID (exists/not exists)
- ✅ Create new reservation
- ✅ Update existing reservation
- ✅ Delete reservation
- ✅ Find reservations by user ID
- ✅ Find reservations by flight ID
- ✅ Find reservations by user ID and flight ID
- ✅ Multiple reservations for same user
- ✅ Edge cases and error scenarios

**Sample Tests**:
```java
@Test
void testGetAllReservations_ReturnsListOfReservations() // Tests GET /api/reservations

@Test
void testGetReservationById_ReservationExists_ReturnsReservation() // Tests GET /api/reservations/{id}

@Test
void testCreateReservation_ValidReservation_ReturnsCreated() // Tests POST /api/reservations

@Test
void testUpdateReservation_ReservationExists_ReturnsUpdatedReservation() // Tests PUT /api/reservations/{id}

@Test
void testDeleteReservation_ReservationExists_ReturnsNoContent() // Tests DELETE /api/reservations/{id}

@Test
void testFindByUserId_ReservationsExist_ReturnsReservations() // Tests GET /api/reservations/user/{userId}

@Test
void testFindByFlightId_ReservationsExist_ReturnsReservations() // Tests GET /api/reservations/flight/{flightId}

@Test
void testFindByUserIdAndFlightId_ReservationExists_ReturnsReservations() // Tests GET /api/reservations/search
```

---

### 2. Service Tests (Unit Tests)

#### UserServiceTest
**Location**: `src/test/java/com/example/companiezbor/service/UserServiceTest.java`

**Purpose**: Tests business logic in UserService with mocked repository

**Test Coverage**:
- ✅ Find all users (with data/empty)
- ✅ Find user by ID (exists/not exists)
- ✅ Save new user
- ✅ Delete user by ID
- ✅ Find by username (exists/not exists)
- ✅ Login validation (valid/invalid credentials)
- ✅ Admin user scenarios

**Key Features**:
- Uses `@ExtendWith(MockitoExtension.class)` for Mockito support
- Uses `@Mock` for repository mocking
- Uses `@InjectMocks` for service injection
- Tests business logic in isolation
- Verifies repository method calls with `verify()`

**Sample Tests**:
```java
@Test
void testFindAll_ReturnsAllUsers()

@Test
void testFindById_UserExists()

@Test
void testSave_NewUser()

@Test
void testDeleteById_UserExists()

@Test
void testFindByUsername_UserExists()

@Test
void testLogin_ValidCredentials_ReturnsUser()

@Test
void testLogin_InvalidPassword_ReturnsEmpty()
```

---

#### FlightServiceTest
**Location**: `src/test/java/com/example/companiezbor/service/FlightServiceTest.java`

**Purpose**: Tests business logic in FlightService with mocked repository

**Test Coverage**:
- ✅ Find all flights (with data/empty)
- ✅ Find flight by ID (exists/not exists)
- ✅ Save new flight
- ✅ Delete flight by ID
- ✅ Find by departure and arrival (exists/not exists)
- ✅ Complex flight scenarios
- ✅ Flight data validation

**Sample Tests**:
```java
@Test
void testFindAll_ReturnsAllFlights()

@Test
void testFindById_FlightExists()

@Test
void testSave_NewFlight()

@Test
void testDeleteById_FlightExists()

@Test
void testFindByDepartureAndArrival_FlightExists()

@Test
void testFindByDepartureAndArrival_FlightDoesNotExist()
```

---

#### ReservationServiceTest
**Location**: `src/test/java/com/example/companiezbor/service/ReservationServiceTest.java`

**Purpose**: Tests business logic in ReservationService with mocked repository

**Test Coverage**:
- ✅ Find all reservations (with data/empty)
- ✅ Find reservation by ID (exists/not exists)
- ✅ Save new reservation
- ✅ Delete reservation by ID
- ✅ Find by user ID (single/multiple/none)
- ✅ Find by flight ID (single/multiple/none)
- ✅ Find by user ID and flight ID (exists/not exists)
- ✅ Complex reservation scenarios

**Sample Tests**:
```java
@Test
void testFindAll_ReturnsAllReservations()

@Test
void testFindById_ReservationExists()

@Test
void testSave_NewReservation()

@Test
void testDeleteById_ReservationExists()

@Test
void testFindByUserId_SingleReservation()

@Test
void testFindByUserId_MultipleReservations()

@Test
void testFindByFlightId_ReservationsExist()

@Test
void testFindByUserIdAndFlightId_ReservationExists()
```

---

## Running the Tests

### Run All Tests
```bash
mvn test
```

### Run Specific Test Class
```bash
mvn test -Dtest=UserControllerTest
mvn test -Dtest=FlightServiceTest
mvn test -Dtest=ReservationControllerTest
```

### Run Specific Test Method
```bash
mvn test -Dtest=UserControllerTest#testGetAllUsers_ReturnsListOfUsers
```

### Run Tests with Coverage
```bash
mvn test jacoco:report
```

### Run from IDE
- **IntelliJ IDEA**: Right-click on test class/method → Run 'TestName'
- **Eclipse**: Right-click on test class/method → Run As → JUnit Test

---

## Test Statistics

### Total Test Count
- **Controller Tests**: ~60+ integration tests
  - UserControllerTest: ~20 tests
  - FlightControllerTest: ~18 tests
  - ReservationControllerTest: ~22 tests

- **Service Tests**: ~50+ unit tests
  - UserServiceTest: ~15 tests
  - FlightServiceTest: ~17 tests
  - ReservationServiceTest: ~18 tests

**Total**: **110+ tests**

### Test Coverage
- ✅ All REST endpoints covered
- ✅ All service methods covered
- ✅ Success scenarios tested
- ✅ Error scenarios tested
- ✅ Edge cases tested
- ✅ Data validation tested

---

## Test Patterns and Best Practices

### 1. AAA Pattern (Arrange-Act-Assert)
All unit tests follow the AAA pattern:
```java
@Test
void testExample() {
    // Arrange - Set up test data and mocks
    when(repository.findById(1)).thenReturn(Optional.of(user));
    
    // Act - Execute the method being tested
    Optional<User> result = service.findById(1);
    
    // Assert - Verify the results
    assertTrue(result.isPresent());
    assertEquals("testuser", result.get().getUsername());
    verify(repository, times(1)).findById(1);
}
```

### 2. Test Isolation
- Each test is independent
- `@BeforeEach` sets up fresh test data
- `@Transactional` ensures database rollback
- Repository is cleared before each test

### 3. Descriptive Test Names
Test names clearly describe what they test:
- `testGetAllUsers_ReturnsListOfUsers`
- `testFindById_UserDoesNotExist_ReturnsEmpty`
- `testLogin_InvalidPassword_ReturnsEmpty`

### 4. HTTP Status Code Validation
Integration tests verify correct HTTP responses:
- `200 OK` - Successful GET
- `201 Created` - Successful POST
- `204 No Content` - Successful DELETE
- `404 Not Found` - Resource not found

### 5. JSON Response Validation
Using JSONPath for response validation:
```java
.andExpect(jsonPath("$.username", is("testuser")))
.andExpect(jsonPath("$.admin", is(false)))
.andExpect(jsonPath("$", hasSize(2)))
```

---

## Common Test Scenarios

### Testing CRUD Operations
Every entity (User, Flight, Reservation) has full CRUD test coverage:
1. **Create** - Valid data, invalid data
2. **Read** - Single item, all items, not found scenarios
3. **Update** - Full update, partial update, not found
4. **Delete** - Successful deletion, not found scenarios

### Testing Search Operations
- Search by single criteria (username, departure/arrival, user ID)
- Search by multiple criteria (user ID + flight ID)
- Search with no results
- Search with multiple results

### Testing Edge Cases
- Empty database queries
- Invalid IDs (999, negative numbers)
- Null values
- Case sensitivity in searches
- Multiple items with same foreign key

---

## Troubleshooting

### Common Issues and Solutions

#### 1. Database Lock Error
**Problem**: `Database may be already in use`

**Solution**: Tests use in-memory database (`jdbc:h2:mem:testdb`) which prevents file locking issues.

#### 2. Test Data Conflicts
**Problem**: Tests interfere with each other

**Solution**: 
- Use `@Transactional` on test class
- Clear repository in `@BeforeEach`
- Use unique test data per test

#### 3. JSON Mapping Issues
**Problem**: ObjectMapper not found

**Solution**: Autowire ObjectMapper in test class:
```java
@Autowired
private ObjectMapper objectMapper;
```

#### 4. MockMvc Not Found
**Problem**: Cannot perform HTTP requests

**Solution**: Initialize MockMvc in `@BeforeEach`:
```java
@BeforeEach
void setUp() {
    mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
}
```

---

## Future Test Improvements

### Potential Enhancements
1. **Performance Tests** - Test response times for large datasets
2. **Security Tests** - Test authentication and authorization
3. **Integration Tests** - Test complete user flows (book flight, cancel reservation)
4. **Validation Tests** - Test input validation and error messages
5. **Concurrent Tests** - Test thread safety and concurrent operations
6. **Database Migration Tests** - Test schema changes
7. **API Documentation Tests** - Ensure API docs match implementation

### Code Coverage Goals
- Line coverage: >80%
- Branch coverage: >70%
- Method coverage: >90%

---

## Resources

### Documentation
- [Spring Boot Testing](https://docs.spring.io/spring-boot/docs/current/reference/html/features.html#features.testing)
- [JUnit 5 User Guide](https://junit.org/junit5/docs/current/user-guide/)
- [Mockito Documentation](https://javadoc.io/doc/org.mockito/mockito-core/latest/org/mockito/Mockito.html)
- [MockMvc Documentation](https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/test/web/servlet/MockMvc.html)

### Related Files
- Production config: `src/main/resources/application.properties`
- Test config: `src/test/resources/application.properties`
- Models: `src/main/java/com/example/companiezbor/model/`
- Controllers: `src/main/java/com/example/companiezbor/rest/`
- Services: `src/main/java/com/example/companiezbor/service/`
- Repositories: `src/main/java/com/example/companiezbor/repository/`

---

## Summary

This test suite provides comprehensive coverage of the CompanieZbor application:

✅ **110+ tests** covering all functionality
✅ **Integration tests** for REST endpoints
✅ **Unit tests** for business logic
✅ **In-memory database** for fast, isolated tests
✅ **Best practices** (AAA pattern, descriptive names, isolation)
✅ **Full CRUD coverage** for all entities
✅ **Error scenarios** tested
✅ **Edge cases** covered

The tests ensure the application works correctly and helps prevent regressions during development.

---

**Last Updated**: December 14, 2025
**Test Framework**: JUnit 5
**Spring Boot Version**: 4.0.0
**Java Version**: 21

