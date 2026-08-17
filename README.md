# 📚 Library Management System API

A production-ready REST API for managing books, members, and borrowing operations — built with Java 21 and Spring Boot 3.

![Java](https://img.shields.io/badge/Java-21-orange?style=flat-square)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2-brightgreen?style=flat-square)
![MySQL](https://img.shields.io/badge/MySQL-8.0-blue?style=flat-square)
![JWT](https://img.shields.io/badge/Auth-JWT-red?style=flat-square)
![License](https://img.shields.io/badge/License-MIT-yellow?style=flat-square)

---

## 📌 Features

- **JWT Authentication** — secure register and login with role-based access (ADMIN / USER)
- **Book Management** — full CRUD with genre filtering and keyword search
- **Member Management** — student and faculty members with different borrowing limits
- **Borrow & Return** — complete borrowing lifecycle with automatic fine calculation
- **Overdue Tracking** — query all overdue records in real time
- **Pagination & Sorting** — all list endpoints support page, size, sortBy, sortDir
- **Input Validation** — request validation with descriptive error messages
- **Global Exception Handling** — consistent JSON error responses across all endpoints
- **API Documentation** — interactive Swagger UI with JWT support

---

## 🛠️ Tech Stack

| Layer | Technology |
|---|---|
| Language | Java 21 |
| Framework | Spring Boot 3.2 |
| Security | Spring Security + JWT (JJWT 0.12.3) |
| Database | MySQL 8.0 |
| ORM | Spring Data JPA + Hibernate |
| Validation | Jakarta Bean Validation |
| Documentation | SpringDoc OpenAPI (Swagger UI) |
| Build Tool | Maven |
| Testing | JUnit 5 + Mockito |

---

## 🚀 Getting Started

### Prerequisites

```
Java 21+
MySQL 8.0+
Maven 3.8+
```

### 1. Clone the repository

```bash
git clone https://github.com/your-username/library-api.git
cd library-api
```

### 2. Create the database

```sql
CREATE DATABASE library_db;
```

### 3. Configure environment

Open `src/main/resources/application.properties` and update:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/library_db
spring.datasource.username=your_mysql_username
spring.datasource.password=your_mysql_password

jwt.secret=your-base64-encoded-secret-key
jwt.expiration=your-expiration-time-in-milliseconds(ex-86400000)
```
Open `docker-compose.yml` and update:

```properties
MYSQL_ROOT_PASSWORD:your_mysql_password
SPRING_DATASOURCE_URL:jdbc:mysql://mysql:3306/library_db
JWT_SECRET:your-base64-encoded-secret-key
JWT_EXPIRATION:your-expiration-time-in-milliseconds(ex-86400000)
```

> **Generating a JWT secret:** use any Base64-encoded string of at least 48 bytes.
> You can generate one with: `openssl rand -base64 64`

### 4. Run the application

```bash
mvn spring-boot:run
```

The server starts at `http://localhost:8080/swagger-ui/index.html#`

### 5. 🌐 Live Demo
API Base URL: https://library-api-xxxx.railway.app
Swagger UI:   https://library-api-xxxx.railway.app/swagger-ui.html

Test credentials (Initialized on start):
Admin → email: admin@library.com  password: admin123
User  → email: user@library.com   password: user123

---

## 🔐 Authentication

All endpoints except registration, login, and public book browsing require a JWT token.

### Register

```http
POST /api/auth/register
Content-Type: application/json

{
  "name": "Syed Asma Saeed",
  "email": "asma@example.com",
  "password": "password123",
  "role": "ADMIN"
}
```

### Login

```http
POST /api/auth/login
Content-Type: application/json

{
  "email": "asma@example.com",
  "password": "password123"
}
```

**Response:**
```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9..."
}
```

### Using the token

Include the token in the `Authorization` header of every protected request:

```
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9...
```

---

## 📋 API Endpoints

### Authentication

| Method | Endpoint | Access | Description |
|---|---|---|---|
| POST | `/api/auth/register` | Public | Register a new user |
| POST | `/api/auth/login` | Public | Login and get JWT token |

### Books

| Method | Endpoint | Access | Description |
|---|---|---|---|
| GET | `/api/books` | Public | Get all books (paginated) |
| GET | `/api/books/{id}` | Public | Get book by ID |
| GET | `/api/books/search?keyword=` | Public | Search books by title |
| POST | `/api/books` | ADMIN | Add a new book |
| PUT | `/api/books/{id}` | ADMIN | Update a book |
| DELETE | `/api/books/{id}` | ADMIN | Delete a book |

### Members

| Method | Endpoint | Access | Description |
|---|---|---|---|
| GET | `/api/members` | ADMIN | Get all members (paginated) |
| GET | `/api/members/{id}` | ADMIN | Get member by ID |
| POST | `/api/members` | ADMIN | Add a new member |
| PUT | `/api/members/{id}` | ADMIN | Update a member |
| DELETE | `/api/members/{id}` | ADMIN | Delete a member |

### Borrow Operations

| Method | Endpoint | Access | Description |
|---|---|---|---|
| POST | `/api/borrow` | Authenticated | Borrow a book |
| POST | `/api/borrow/return/{recordId}` | Authenticated | Return a book |
| GET | `/api/borrow/overdue` | Authenticated | Get all overdue records |
| GET | `/api/borrow/history/{memberId}` | Authenticated | Get member borrow history |

---

## 📄 Pagination & Sorting

All list endpoints support these query parameters:

| Parameter | Default | Description |
|---|---|---|
| `page` | `0` | Page number (0-indexed) |
| `size` | `10` | Items per page (max 100) |
| `sortBy` | `title` | Field to sort by |
| `sortDir` | `asc` | Sort direction: `asc` or `desc` |

**Example:**
```
GET /api/books?page=0&size=5&sortBy=author&sortDir=desc
```

---

## ⚙️ Business Rules

### Member Borrowing Limits

| Member Type | Borrow Limit | Loan Period |
|---|---|---|
| STUDENT | 3 books | 14 days |
| FACULTY | 10 books | 30 days |

### Fine Calculation

Overdue books are charged **₹2 per day** after the due date.

```
Fine = Days Overdue × ₹2.00
```

Fines are calculated automatically on return.

---

## 🗂️ Project Structure

```
src/main/java/com/library/
├── config/
│   ├── OpenApiConfig.java          # Swagger / OpenAPI configuration
│   └── SecurityConfig.java         # Spring Security + JWT filter chain
├── controller/
│   ├── AuthController.java
│   ├── BookController.java
│   ├── BorrowController.java
│   └── MemberController.java
├── dto/
│   ├── request/                    # Incoming request bodies
│   └── response/                   # Outgoing response bodies
├── enums/
│   ├── Genre.java
│   ├── MemberType.java
│   └── Role.java
├── exception/
│   ├── GlobalExceptionHandler.java # @ControllerAdvice — all exceptions
│   └── *.java                      # Custom exception classes
├── model/
│   ├── Book.java                   # @Entity
│   ├── BorrowRecord.java           # @Entity
│   ├── Member.java                 # @Entity
│   └── User.java                   # @Entity — implements UserDetails
├── repository/
│   ├── BookRepository.java
│   ├── BorrowRecordRepository.java
│   ├── MemberRepository.java
│   └── UserRepository.java
├── security/
│   ├── JwtAuthFilter.java          # JWT validation on every request
│   └── UserDetailsServiceImpl.java
└── service/
    ├── AuthService.java
    ├── BookService.java
    ├── BorrowService.java
    ├── JwtService.java
    └── MemberService.java
```

---

## 🧪 Testing

Unit tests written with JUnit 5 and Mockito covering:

- `BookServiceTest` — CRUD operations, exception scenarios
- `MemberServiceTest` — member management, duplicate detection
- `BorrowServiceTest` — borrow/return lifecycle, limit enforcement, fine calculation

```bash
# Run all tests
mvn test

# Run specific test class
mvn test -Dtest=BookServiceTest
```

---

## 📬 Error Responses

All errors return a consistent JSON structure:

```json
{
  "status": 404,
  "message": "Book not found: 99"
}
```

| Status Code | Meaning |
|---|---|
| 400 | Validation failed or bad request |
| 401 | Missing or invalid JWT token |
| 403 | Insufficient permissions |
| 404 | Resource not found |
| 409 | Conflict (duplicate email etc.) |
| 500 | Internal server error |

---

## 🔮 Planned Improvements

- [ ] Integration tests with MockMvc
- [ ] AWS deployment (EC2 + RDS)
- [ ] Email notifications for overdue books
- [ ] Book reservation system
- [ ] Refresh token support

---

## 👩‍💻 Author

Built as part of a structured Java backend engineering learning journey.

- GitHub: [syed-asma-saeed](https://github.com/syed-asma-saeed)
- LinkedIn: [Syed Asma Saeed](https://www.linkedin.com/in/syed-asma-saeed/)
