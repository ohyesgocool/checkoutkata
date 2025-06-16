# Supermarket Checkout Kata

This project implements a supermarket checkout system that calculates the total price of a list of items, applying special offers when applicable. The system is built as a Spring Boot REST API using Java 21, Spring Data JPA, and an H2 in-memory database for persistence. It is containerized using Docker and can be run locally with a single `docker-compose up` command.

## Table of Contents
- [Features](#features)
- [Prerequisites](#prerequisites)
- [Setup Instructions](#setup-instructions)
- [Running the Application](#running-the-application)
- [API Usage](#api-usage)

## Features
- Calculates the total price for a list of items scanned at the checkout.
- Supports individual item pricing and special offers (e.g., "2 apples for 45").
- REST API endpoint to process checkout requests.
- Uses H2 in-memory database for storing item prices and offers, initialized with sample data.
- Containerized with Docker for easy setup and deployment.
- Input validation and error handling for invalid items.
- Logging for debugging and monitoring.
- Extensible design for adding new items and offers.

## Prerequisites
- **Docker**: Ensure Docker and Docker Compose are installed. Verify with:
  ```bash
  docker --version
  docker-compose --version
- Java 21 (optional, for local development without Docker): Ensure JDK 21 is installed if you want to run the application outside Docker.

- Maven (optional, for local development): Required for building the project locally without Docker.

- curl or Postman (optional): For testing the API endpoint.

## Setup Instructions
Follow these steps to set up the project locally using Docker and an H2 in-memory database.

1. **Clone the Repository**:
   If the project is hosted on a remote repository, clone it:
   ```bash
   git clone https://github.com/ohyesgocool/checkoutkata.git
   cd checkoutkata
   ```
   If you're working locally without a remote repository, ensure all required files (Dockerfile, docker-compose.yml, pom.xml, src/main/resources/data.sql, src/main/resources/application.properties) are in the project root directory (checkoutkata).

2. **Verify Required Files**:
   Ensure the following files exist in the project root directory (`checkoutkata`):
    - `Dockerfile`
    - `docker-compose.yml`
    - `pom.xml`
    - `src/main/resources/data.sql`
    - `src/main/resources/application.properties`

## Running the Application
Follow these steps to run the application locally using Docker and the H2 in-memory database.

1. **Build and Start the Application**:
   Ensure you are in the project root directory (`checkoutkata`) and run:
   ```bash
   docker-compose up --build
   ```
    - This command builds the Docker image using the Dockerfile, starts the container, and initializes the H2 in-memory database with the data from src/main/resources/data.sql.

- The Spring Boot application will start and be accessible at http://localhost:8080.

- You should see logs in the terminal indicating the application startup, database initialization, and SQL statements (due to spring.jpa.show-sql=true).
2. **Stop the Application**:

To stop the container, press Ctrl+C in the terminal where docker-compose is running, or execute:

 ```bash
docker-compose down
   ```


## API Usage
The application exposes a single REST endpoint to calculate the total price of a list of scanned items, applying special offers where applicable.

- **Example Request with curl**:
  ```bash
  curl -X POST http://localhost:8080/v1/checkout/total \
     -H "Content-Type: application/json" \
     -d '{"items": ["apple", "banana","apple", "banana", "banana"]}'
  ```
  

- **Endpoint**: `POST /v1/checkout/total`
- **Content-Type**: `application/json`
- **Request Body**:
  ```json
  {
    "items": ["apple", "banana","apple", "banana", "banana"]
  }
  ```
- **Response**:
  ```json
  {
  "total": 175
  }
  ```













