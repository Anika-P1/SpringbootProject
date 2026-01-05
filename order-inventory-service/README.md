# Order Inventory Service

## Overview
The `order-inventory-service` is a microservice designed to manage orders and inventory for a mid-scale commerce platform. Built using Java 21 and Spring Boot 3.x, this service provides a robust and scalable solution for handling order processing and inventory management.

## Architecture
The service follows a layered architecture consisting of:
- **Controller Layer**: Handles incoming HTTP requests and responses.
- **Service Layer**: Contains business logic for order and inventory management.
- **Repository Layer**: Interacts with the database using Spring Data JPA.
- **Model Layer**: Defines the data structures used in the application.
- **Configuration Layer**: Manages application configuration, security, and observability.

## Dependencies
The project uses the following key dependencies:
- Spring Boot Starter Web
- Spring Boot Starter Data JPA
- PostgreSQL Driver
- Flyway for database migrations
- Spring Boot Starter Security
- Spring Boot Actuator for monitoring
- Micrometer for metrics
- Testcontainers for integration testing

## Setup Instructions
1. **Clone the Repository**
   ```bash
   git clone <repository-url>
   cd order-inventory-service
   ```

2. **Build the Project**
   ```bash
   mvn clean install
   ```

3. **Run the Application**
   ```bash
   mvn spring-boot:run
   ```

4. **Access the API**
   The service runs on `http://localhost:8080`. You can access the API endpoints for orders and inventory.

## API Examples
- **Create Order**
  - `POST /api/orders`
  
- **Get Inventory**
  - `GET /api/inventory`

## Security
The service is secured using JWT-based authentication. Ensure to include a valid JWT token in the Authorization header for protected endpoints.

## Observability
The service integrates with Spring Boot Actuator and Micrometer to provide metrics and health checks. Access the actuator endpoints at `http://localhost:8080/actuator`.

## Testing
Unit and integration tests are included in the `src/test` directory. Use the following command to run the tests:
```bash
mvn test
```

## Configuration
Configuration files are located in `src/main/resources`:
- `application.yml`: Main configuration.
- `application-prod.yml`: Production-specific configuration.

## Database Migration
Database schema is managed using Flyway. The migration scripts are located in `src/main/resources/db/migration`.

## Docker
The project includes a Dockerfile for containerization. Build the Docker image using:
```bash
docker build -t order-inventory-service .
```

## Kubernetes
Kubernetes deployment and service configurations are provided in the `k8s` directory for easy deployment in a cloud environment.

## Helm
Helm charts for deploying the service are located in the `helm` directory, allowing for easy management of Kubernetes resources.

## Contribution
Contributions are welcome! Please submit a pull request or open an issue for any enhancements or bug fixes.

## License
This project is licensed under the MIT License. See the LICENSE file for details.