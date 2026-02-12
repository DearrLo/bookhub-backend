# 📚 BookHub - Library Management System - Backend Service
BookHub is a full-stack application designed to digitize library management. It enables the management of a book catalog, automation of loans, and queue management through a reservation system.

BookHub API : This repository contains the Backend service for the BookHub application. It is a robust REST API built with Spring Boot, handling business logic, data persistence, and security via JWT.

## Technical Stack

Language: Java 17

Framework: Spring Boot 3 (or 4)

Security: Spring Security & JWT (Stateless)

Persistence: Spring Data JPA / Hibernate

Database: H2 (Persistent mode for development)

Tools: Lombok, Gradle, Javadoc

## Installation and Setup

1. **Fork and Clone the project**

```bash
git clone https://github.com/your-username/bookhub.git
cd bookhub
```

2. **Database Configuration**

Database parameters and the JWT secret key are located in src/main/resources/application.properties.
Uncomment the Docker database connection and comment out the local connection.

To launch:

```Bash
docker compose up -d
```

3. **Launch the application**:

```Bash
./gradlew bootRun
```

4. **Run the Backend (Spring Boot)**

Ensure you have JDK 17 installed.

Run `BookhubApplication.java`

The API will be accessible at: `http://localhost:8080`


## Testing and Quality

Unit Tests: JUnit 5 & Mockito (verifying Service layer logic).

To run the tests:

```Bash
./gradlew test
```

## Documentation

**Javadoc** Documentation: Generate the complete application documentation with:

```Bash
./gradlew javadoc
```
Run the `index.html` available afterwards in `build/docs/javadoc/`


**Swagger UI**: Once the server is launched, access the interactive endpoint documentation at:

`http://localhost:8080/swagger-ui/index.html`
