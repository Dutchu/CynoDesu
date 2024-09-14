
# Cynodesu Project

## Overview

Cynodesu is a Spring Boot application that serves as a dodgy doggy Doge application. It includes features for managing dogs, facilities, competitions, and more. The application uses PostgreSQL for database management, Spring Security for securing endpoints, and a variety of other libraries for various functionalities like data validation, web MVC, Thymeleaf for templating, JWT for authentication, etc.

## Prerequisites

To run this project, ensure you have the following installed:

- Java 17
- Docker
- Docker Compose
- Maven

## Installation

1. **Clone the Repository:**

   ```bash
   git clone https://github.com/your-repo/cynodesu.git
   cd cynodesu
   ```

2. **Build the Application:**

   Use Maven to package the application:

   ```bash
   mvn clean package
   ```

   This command will compile the application, run the tests, and package it into a JAR file.

3. **Configure the Application:**

   The application uses several configuration files:

    - `application.yml`: Main configuration file.
    - `application-dev.yml`: Development environment configuration.
    - `breeds.properties`: Contains properties related to dog breeds.
    - `cert.properties`: Contains properties related to certificates.

   Make sure these files are correctly configured for your environment.

## Running the Application

### Using Docker Compose

The project includes a `docker-compose.yml` file to set up the environment:

1. **Run Docker Compose:**

   ```bash
   docker-compose up -d
   ```

   This command will start all the necessary services, such as the PostgreSQL database.

2. **Access the Application:**

   The application should now be running and accessible at [http://localhost:8080](http://localhost:8080).

### Without Docker

Alternatively, you can run the application without Docker:

1. **Start the PostgreSQL Database:**

   Ensure you have a running PostgreSQL instance. Update your `application.yml` or `application-dev.yml` file with the correct database connection settings.

2. **Run the Application:**

   ```bash
   mvn spring-boot:run
   ```

   The application will start on [http://localhost:8080](http://localhost:8080).

## Logging Configuration

The application uses Logback for logging, configured in the `logback-spring.xml` file:

- **Console Appender:** Logs to the console.
- **File Appender:** Logs to `logs/myapp.log` and rolls over daily, keeping 30 days of logs.

You can adjust the log level and other settings in the `logback-spring.xml` file.

## Running Tests

To run the unit and integration tests:

```bash
mvn test
```

## Contributing

Contributions are welcome! Please fork the repository and create a pull request with your changes.

## License

This project is licensed under the MIT License - see the LICENSE file for details.
