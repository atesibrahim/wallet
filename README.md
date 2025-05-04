# Wallet Application

## Overview
The Wallet Application is a Spring Boot-based project designed to manage user authentication, authorization, and wallet-related operations. 
It leverages Spring Security for securing endpoints, Spring Data JPA for database interactions, and Gradle for build automation. 
The application is containerized using Docker for easy deployment.

## Features
- User authentication and authorization using Spring Security.
- Role-based (CUSTOMER, ADMIN, USER) access control with `AuthorityType` enums.
- RESTful APIs for wallet operations.
- Swagger integration for API documentation.
- Integration with OAuth2 resource server.
- In-memory H2 database for testing and development.
- Dockerized for seamless deployment.
- Unit and integration tests for core functionalities.

## Prerequisites
Before running the application, ensure the following are installed:
- Java 17 or higher
- Gradle
- Docker (optional, for containerized deployment)

## Getting Started

### 1. Clone the Repository
```bash
git clone https://github.com/atesibrahim/wallet.git
cd wallet
```
### 2. Build the Project
Use Gradle to build the application:
```bash
./gradlew clean build
```
This command will compile the code, run tests, and package the application into a JAR file.

### 3. Run the Application
If you prefer to run the JAR file directly:
```bash
java -jar wallet-1.0.jar
```

Or alternatively, you can run the application using Gradle:
```bash
./gradlew bootRun
```

The application will start on http://localhost:8080.

### 4. Docker Deployment (Optional)
If you want to run the application in a Docker container, you can build the Docker image using the provided Dockerfile:
```bash
docker build -t wallet-app .
```
Then, run the Docker container:
```bash
docker run -p 8080:8080 wallet-app
```
The application will be accessible at http://localhost:8080.

### 5. API Endpoints
The application exposes several RESTful APIs for wallet operations. Below are some of the key endpoints:
- `POST /wallet/v1/auth/login`: Authenticate a user and obtain a JWT token.
- `POST /wallet/v1/wallets`: Create a new wallet.
- `GET /wallet/v1/wallets/{customerId}`: Retrieve wallet details by Customer ID.
- `GET /wallet/v1/transactions/{walletId}`: Retrieve transactions for a specific wallet.'
- `POST /wallet/v1/transactions/deposit`: Deposit an amount into a wallet.
- `POST /wallet/v1/transactions/withdraw`: Withdraw an amount from a wallet.
- `POST /wallet/v1/transactions/approve`: Approve a transaction.

### 6. Testing
The project includes unit and integration tests. You can run the tests using:
```bash
./gradlew test
```
The test results will be available in the `build/test-results` directory.

### 7. Configuration
The application uses an H2 in-memory database for development and testing. 
You can configure the database settings in `src/main/resources/application.yml`. For production, consider using a more robust database like PostgreSQL or MySQL.

### 8. Security
The application uses Spring Security for authentication and authorization. 
The security configuration is defined in `SecurityConfig.java`. 

It includes JWT token generation and validation, 
as well as role-based access control for different endpoints.

### 9. Database Initialization
The application uses `schema.sql` and `data.sql` files for database initialization.

- **`schema.sql`**: Contains the SQL statements to define the database schema, such as creating tables and setting up constraints. This file is executed automatically when the application starts.

- **`data.sql`**: Contains the SQL statements to populate the database with initial data. This is useful for setting up default values or test data during development.

Both files are located in the `src/main/resources` directory. You can modify these files to customize the database schema and initial data as per your requirements.

### 10. API Documentation
Swagger is used for API documentation.
You can access the Swagger UI at:
``` http://localhost:8080/swagger-ui/index.html#/
```
![img.png](swagger.png)
### 11. Using the API Endpoints

To interact with the API endpoints, follow these steps:

#### Step 1: Login and Obtain a JWT Token
- Use the `POST /wallet/v1/auth/login` endpoint to authenticate a user.

- Example request for customer role:
  ```json
  {
  "email": "b@test.com",
  "password": "test"
  }
  ```
- Example request for admin role:
  ```json
  {
  "email": "a@test.com",
  "password": "abcde"
  }
  ```
  
- The response will include a JWT token:
  ```json
  {
    "data": {
        "enabled": true,
        "name": "Bob",
        "surname": "Johnson",
        "email": "b@test.com",
        "authorities": [
            "CUSTOMER",
            "LOGGED_IN"
        ],
        "token": "eyJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJpbmctd2FsbGV0Iiwic3ViIjoiYkB0ZXN0LmNvbSIsImV4cCI6MTc0NjMyODY5NCwiaWF0IjoxNzQ2MzI4NTE0LCJ1c2VyIjoiQUFBQUFBQUFBQUFBQUFBQWhGT2l1VGNkYzg3RnV5VElnejhHQnVuQzljRTd5dmtQNG1UalJqaHpqZ0NEeEZWNGV3emRsVW41U2w4TnpKNU81Rk81TDZ3Njl1SVRBYnR5Lzcza0oxQzdzWDYyTzZ5RVFNcWY1OGxEWGpTY1hGVHg1NWt3bXRMdGJvT0xObjUzYjVzU3Z3ck45K1VJVXJhbHJxU2pLUGg4b09nMjA0TWozcGg4QlhrRGEwQUYvVGJ2K0Q5dEF4dTMifQ.mhZhlHvo3PdihfFdNcZMpBBMjE3ftroBih2LhwAXRhQ"
    },
    "errorResponse": null,
    "success": true
  }
  ```

#### Step 2: Add the JWT Token to the Authorization Header
- For all subsequent API requests, include the JWT token in the `Authorization` header as a Bearer token:
  ```
  Authorization: Bearer <JWT_TOKEN>
  ```

#### Step 3: Call Other Endpoints
- Use the token to access protected endpoints. For example:

1. **Create a Wallet**  
  Customer role has access to create a wallet.
   Endpoint: `POST localhost:8080/wallet/v1/wallets`  
   Example request:
   ```json
   {
   "walletName": "My Wallet2",
   "currency": "EUR",
   "activeForShopping": true,
   "activeForWithdraw": true,
   "customerId": 1
   }
   ```

2. **List Wallets**  
    Customer role has access to list wallets.
   Endpoint: `GET localhost:8080/wallet/v1/wallets/{customerId}`  
   Example request:
   ```
   GET localhost:8080/wallet/v1/wallets/1
   ```

3. **Deposit Funds**  
    Customer role has access to deposit funds.
   Endpoint: `POST localhost:8080/wallet/v1/transactions/deposit`  
   Example request:
   ```json
   {
   "amount": "1230",
   "walletId": "TR11234",
   "source": "TR4545"
   }
   ```

4. **Withdraw Funds**  
    Customer role has access to withdraw funds.
   Endpoint: `POST localhost:8080/wallet/v1/transactions/withdraw`  
   Example request:
   ```json
   {
   "amount": "300",
   "walletId": "TR11234",
   "destination": "TR123"
   }
   ```
5. **List Transactions**  
    Customer role has access to list transactions.
   Endpoint: `GET localhost:8080/wallet/v1/transactions/{walletId}`  
   Example request:
   ```
   GET localhost:8080/wallet/v1/transactions/TR11234
   ```

6. **Approve a Transaction**  
    Admin role has access to approve transactions.
   Endpoint: `POST localhost:8080/wallet/v1/transactions/approve`  
   Example request:
   ```json
   {
   "transactionId": "100",
   "status": "APPROVED"
   } 
   ```

#### Notes
- Ensure the JWT token is valid and not expired.
- If the token is invalid or missing, the API will return a 401 Unauthorized error.
- Please note that the API endpoints may require specific roles (e.g., CUSTOMER, ADMIN) for access. Ensure that the user has the appropriate role before making requests.