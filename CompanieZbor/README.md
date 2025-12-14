# CompanieZbor - Flight Company REST API

A comprehensive REST API backend application for managing flight bookings, built with Spring Boot, Hibernate, and H2 database.

## 📋 Table of Contents

- [Overview](#overview)
- [Technologies](#technologies)
- [Features](#features)
- [Getting Started](#getting-started)
- [API Documentation](#api-documentation)
- [Database](#database)
- [Testing](#testing)
- [Project Structure](#project-structure)
- [Configuration](#configuration)
- [Troubleshooting](#troubleshooting)

## 🌟 Overview

CompanieZbor is a flight booking management system that provides RESTful APIs for managing:
- **Users** - Customer and admin user accounts
- **Flights** - Flight schedules and details
- **Reservations** - Flight booking records

The application follows a layered architecture with Controllers, Services, and Repositories, fully tested with integration and unit tests.

## 🛠️ Technologies

### Core Technologies
- **Java 21** - Programming language
- **Spring Boot 4.0.0** - Application framework
- **Spring Data JPA (Hibernate)** - ORM and data access
- **H2 Database** - Embedded database
- **Maven** - Build and dependency management

### Testing
- **JUnit 5** - Testing framework
- **Mockito** - Mocking framework
- **Spring Boot Test** - Integration testing
- **MockMvc** - REST API testing

## ✨ Features

### User Management
- ✅ User registration and authentication
- ✅ Admin and regular user roles
- ✅ Secure password handling (write-only)
- ✅ Login with username/password

### Flight Management
- ✅ Create, read, update, delete flights
- ✅ Search flights by departure and arrival cities
- ✅ Track flight details (time, duration, seats, price)
- ✅ Support for both domestic and international flights

### Reservation Management
- ✅ Book flights for users
- ✅ View all reservations
- ✅ Search by user or flight
- ✅ Cancel reservations

### Additional Features
- ✅ H2 Console for database management
- ✅ RESTful API design
- ✅ Automatic data initialization
- ✅ Comprehensive test coverage (110+ tests)

## 🚀 Getting Started

### Prerequisites
- Java 21 or higher
- Maven 3.6 or higher
- Git (optional)

### Installation

1. **Clone the repository** (or download the source code)
   ```bash
   git clone <repository-url>
   cd CompanieZbor
   ```

2. **Build the project**
   ```bash
   mvn clean install
   ```

3. **Run the application**
   ```bash
   mvn spring-boot:run
   ```
   
   Or run the generated JAR:
   ```bash
   java -jar target/CompanieZbor-0.0.1-SNAPSHOT.jar
   ```

4. **Access the application**
   - API Base URL: `http://localhost:8080/api`
   - H2 Console: `http://localhost:8080/h2-console`

### Quick Start Example

Test the API with a simple curl command:

```bash
# Get all flights
curl http://localhost:8080/api/flights

# Get all users
curl http://localhost:8080/api/users

# Login
curl -X POST http://localhost:8080/api/users/login \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"admin123"}'
```

## 📚 API Documentation

### Base URL
```
http://localhost:8080/api
```

---

### 👤 User Endpoints

#### Get All Users
```http
GET /api/users
```
**Response:** `200 OK`
```json
[
  {
    "id": 1,
    "username": "admin",
    "admin": true
  }
]
```

#### Get User by ID
```http
GET /api/users/{id}
```
**Response:** `200 OK` | `404 Not Found`

#### Create User
```http
POST /api/users
Content-Type: application/json

{
  "username": "newuser",
  "password": "password123",
  "admin": false
}
```
**Response:** `201 Created`

#### Update User
```http
PUT /api/users/{id}
Content-Type: application/json

{
  "username": "updateduser",
  "password": "newpassword",
  "admin": false
}
```
**Response:** `200 OK` | `404 Not Found`

#### Delete User
```http
DELETE /api/users/{id}
```
**Response:** `204 No Content` | `404 Not Found`

#### Login
```http
POST /api/users/login
Content-Type: application/json

{
  "username": "admin",
  "password": "admin123"
}
```
**Response:** `200 OK` | `401 Unauthorized`

---

### ✈️ Flight Endpoints

#### Get All Flights
```http
GET /api/flights
```
**Response:** `200 OK`
```json
[
  {
    "id": 1,
    "departure": "Bucuresti",
    "arrival": "Paris",
    "departure_time": "2025-01-12T07:30:00",
    "duration": 180,
    "nrSeats": 180,
    "plane_name": "Boeing 737",
    "price": 199.99
  }
]
```

#### Get Flight by ID
```http
GET /api/flights/{id}
```
**Response:** `200 OK` | `404 Not Found`

#### Create Flight
```http
POST /api/flights
Content-Type: application/json

{
  "departure": "London",
  "arrival": "New York",
  "departure_time": "2025-12-25T14:00:00",
  "duration": 480,
  "nrSeats": 200,
  "plane_name": "Airbus A380",
  "price": 599.99
}
```
**Response:** `201 Created`

#### Update Flight
```http
PUT /api/flights/{id}
Content-Type: application/json

{
  "departure": "London",
  "arrival": "New York",
  "departure_time": "2025-12-25T15:00:00",
  "duration": 480,
  "nrSeats": 200,
  "plane_name": "Airbus A380",
  "price": 649.99
}
```
**Response:** `200 OK` | `404 Not Found`

#### Delete Flight
```http
DELETE /api/flights/{id}
```
**Response:** `204 No Content` | `404 Not Found`

#### Search Flights
```http
GET /api/flights/search?departure=Bucuresti&arrival=Paris
```
**Response:** `200 OK` | `404 Not Found`

---

### 🎫 Reservation Endpoints

#### Get All Reservations
```http
GET /api/reservations
```
**Response:** `200 OK`
```json
[
  {
    "id": 1,
    "userId": 2,
    "flightId": 5
  }
]
```

#### Get Reservation by ID
```http
GET /api/reservations/{id}
```
**Response:** `200 OK` | `404 Not Found`

#### Create Reservation
```http
POST /api/reservations
Content-Type: application/json

{
  "userId": 2,
  "flightId": 5
}
```
**Response:** `201 Created`

#### Update Reservation
```http
PUT /api/reservations/{id}
Content-Type: application/json

{
  "userId": 2,
  "flightId": 6
}
```
**Response:** `200 OK` | `404 Not Found`

#### Delete Reservation
```http
DELETE /api/reservations/{id}
```
**Response:** `204 No Content` | `404 Not Found`

#### Get Reservations by User
```http
GET /api/reservations/user/{userId}
```
**Response:** `200 OK`

#### Get Reservations by Flight
```http
GET /api/reservations/flight/{flightId}
```
**Response:** `200 OK`

#### Search Reservation
```http
GET /api/reservations/search?userId=2&flightId=5
```
**Response:** `200 OK` | `404 Not Found`

---

## 🗄️ Database

### H2 Database Configuration

The application uses H2, an embedded Java SQL database, perfect for development and testing.

**Production Configuration:**
- **URL:** `jdbc:h2:file:./data/companiezbor-db`
- **Username:** `sa`
- **Password:** `12345`
- **Console:** Enabled at `http://localhost:8080/h2-console`

**Test Configuration:**
- **URL:** `jdbc:h2:mem:testdb` (in-memory)
- **Schema:** Auto-created and dropped per test
- **Console:** Disabled

### Database Schema

#### Users Table
```sql
CREATE TABLE users (
    id INTEGER PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255),
    admin BOOLEAN
);
```

#### Flight Table
```sql
CREATE TABLE flight (
    id INTEGER PRIMARY KEY AUTO_INCREMENT,
    departure VARCHAR(255) NOT NULL,
    arrival VARCHAR(255) NOT NULL,
    departure_time TIMESTAMP NOT NULL,
    duration INTEGER NOT NULL,
    nr_seats INTEGER NOT NULL,
    plane_name VARCHAR(255),
    price DOUBLE NOT NULL
);
```

#### Reservation Table
```sql
CREATE TABLE reservation (
    id INTEGER PRIMARY KEY AUTO_INCREMENT,
    user_id INTEGER NOT NULL,
    flight_id INTEGER NOT NULL
);
```

### Initial Data

The application automatically loads sample data on startup:

**Users:**
- `admin` / `admin123` (Admin)
- `Ion Popescu` / `parola1` (Regular)
- `John Doe` / `parola2` (Regular)
- `Mike Michael` / `parola3` (Regular)
- `Andrei Popovici` / `manager123` (Admin)

**Flights:**
- Domestic flights: Bucuresti ↔ Cluj-Napoca, Timisoara, Iasi
- International flights: Bucuresti ↔ Paris, Roma, Berlin

### Accessing H2 Console

1. Navigate to: `http://localhost:8080/h2-console`
2. Use these settings:
   - **JDBC URL:** `jdbc:h2:file:./data/companiezbor-db`
   - **Username:** `sa`
   - **Password:** `12345`
3. Click "Connect"

---

## 🧪 Testing

The application includes comprehensive test coverage with 110+ tests.

### Run All Tests
```bash
mvn test
```

### Run Specific Test Class
```bash
mvn test -Dtest=UserControllerTest
mvn test -Dtest=FlightServiceTest
```

### Test Coverage

**Integration Tests (Controller Layer):**
- `UserControllerTest` - 20 tests
- `FlightControllerTest` - 18 tests
- `ReservationControllerTest` - 22 tests

**Unit Tests (Service Layer):**
- `UserServiceTest` - 15 tests
- `FlightServiceTest` - 17 tests
- `ReservationServiceTest` - 18 tests

For detailed test documentation, see [Test README](src/test/README.md).

---

## 📁 Project Structure

```
CompanieZbor/
├── src/
│   ├── main/
│   │   ├── java/com/example/companiezbor/
│   │   │   ├── CompanieZborApplication.java    # Main application class
│   │   │   ├── model/                          # Entity classes
│   │   │   │   ├── User.java
│   │   │   │   ├── Flight.java
│   │   │   │   └── Reservation.java
│   │   │   ├── repository/                     # Data access layer
│   │   │   │   ├── UserRepository.java
│   │   │   │   ├── FlightRepository.java
│   │   │   │   └── ReservationRepository.java
│   │   │   ├── service/                        # Business logic layer
│   │   │   │   ├── UserService.java
│   │   │   │   ├── FlightService.java
│   │   │   │   └── ReservationService.java
│   │   │   └── rest/                           # REST controllers
│   │   │       ├── UserController.java
│   │   │       ├── FlightController.java
│   │   │       └── ReservationController.java
│   │   └── resources/
│   │       ├── application.properties          # App configuration
│   │       └── data.sql                        # Initial data
│   └── test/
│       ├── java/com/example/companiezbor/
│       │   ├── rest/                           # Controller tests
│       │   │   ├── UserControllerTest.java
│       │   │   ├── FlightControllerTest.java
│       │   │   └── ReservationControllerTest.java
│       │   └── service/                        # Service tests
│       │       ├── UserServiceTest.java
│       │       ├── FlightServiceTest.java
│       │       └── ReservationServiceTest.java
│       ├── resources/
│       │   └── application.properties          # Test configuration
│       └── README.md                           # Test documentation
├── data/                                       # Database files
│   └── companiezbor-db.mv.db
├── target/                                     # Build output
├── pom.xml                                     # Maven configuration
└── README.md                                   # This file
```

### Architecture Layers

1. **Model Layer** - JPA entities representing database tables
2. **Repository Layer** - Spring Data JPA repositories for database access
3. **Service Layer** - Business logic and transaction management
4. **Controller Layer** - REST endpoints and HTTP request handling

---

## ⚙️ Configuration

### Application Properties

**Main Configuration** (`src/main/resources/application.properties`):
```properties
spring.application.name=CompanieZbor

# H2 Database Configuration
spring.datasource.url=jdbc:h2:file:./data/companiezbor-db
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=12345

# JPA/Hibernate Configuration
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

# H2 Console Configuration
spring.h2.console.enabled=true
spring.h2.console.path=/h2-console

# Data Initialization
spring.sql.init.mode=embedded
spring.jpa.defer-datasource-initialization=true
```

**Test Configuration** (`src/test/resources/application.properties`):
```properties
spring.application.name=CompanieZbor

# In-memory database for tests
spring.datasource.url=jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=

# Test-specific JPA settings
spring.jpa.hibernate.ddl-auto=create-drop
spring.jpa.show-sql=true
spring.h2.console.enabled=false
spring.sql.init.mode=never
spring.jpa.defer-datasource-initialization=false
spring.jpa.open-in-view=false
```

### Port Configuration

By default, the application runs on port `8080`. To change it, add to `application.properties`:
```properties
server.port=9090
```

---

## 🔧 Troubleshooting

### Common Issues

#### 1. Database Lock Error
**Problem:** `Database may be already in use`

**Solutions:**
- Close all H2 Console connections
- Stop any other instances of the application
- Delete `data/companiezbor-db.mv.db.lock.db` file
- Restart the application

#### 2. Port Already in Use
**Problem:** `Port 8080 was already in use`

**Solutions:**
- Change the port in `application.properties`:
  ```properties
  server.port=9090
  ```
- Or stop the application using port 8080

#### 3. Tests Failing
**Problem:** Tests fail with database errors

**Solutions:**
- Ensure you're using the in-memory test database
- Check that `src/test/resources/application.properties` is properly configured
- Run `mvn clean test` to rebuild

#### 4. Data Not Loading
**Problem:** Sample data doesn't appear in the database

**Solutions:**
- Check `spring.sql.init.mode=embedded` in `application.properties`
- Ensure `data.sql` is in `src/main/resources/`
- If using `ddl-auto=create`, data is reloaded on each restart

#### 5. H2 Console Access Issues
**Problem:** Cannot access H2 Console

**Solutions:**
- Verify the application is running
- Check `spring.h2.console.enabled=true` in properties
- Use correct JDBC URL: `jdbc:h2:file:./data/companiezbor-db`
- Ensure username is `sa` and password is `12345`

---

## 🚀 Development

### Adding New Endpoints

1. **Create/Update Entity** in `model/`
2. **Create Repository** interface in `repository/`
3. **Create Service** class in `service/`
4. **Create Controller** in `rest/`
5. **Write Tests** in `test/`

### Code Style

- Follow Java naming conventions
- Use constructor injection for dependencies
- Keep controllers thin (delegate to services)
- Write descriptive test names
- Document public APIs

### Building for Production

```bash
# Build JAR file
mvn clean package

# Run the JAR
java -jar target/CompanieZbor-0.0.1-SNAPSHOT.jar
```

---

## 📊 API Response Codes

| Code | Description |
|------|-------------|
| `200 OK` | Request successful |
| `201 Created` | Resource created successfully |
| `204 No Content` | Resource deleted successfully |
| `401 Unauthorized` | Invalid credentials |
| `404 Not Found` | Resource not found |
| `500 Internal Server Error` | Server error |

---

## 🔐 Security Considerations

⚠️ **Note:** This is a development/learning project. For production use, consider:

- Adding Spring Security for authentication/authorization
- Using BCrypt for password hashing
- Implementing JWT tokens for stateless auth
- Adding input validation and sanitization
- Implementing rate limiting
- Using HTTPS
- Securing H2 Console or disabling in production
- Moving to a production database (PostgreSQL, MySQL)

---

## 📄 License

This project is for educational purposes.

---

**Version:** 0.0.1-SNAPSHOT  
**Last Updated:** December 14, 2025  
**Java Version:** 21  
**Spring Boot Version:** 4.0.0

