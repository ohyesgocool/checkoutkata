# Use a Maven base image with Java 21 to build the application
FROM maven:3.9.6-eclipse-temurin-21 AS build

# Set the working directory
WORKDIR /app

# Copy the pom.xml and source code
COPY pom.xml .
COPY src ./src

# Build the application, skipping tests
RUN mvn clean package -DskipTests

# Use a smaller OpenJDK 21 image for running the application
FROM eclipse-temurin:21-jre

# Set the working directory
WORKDIR /app

# Copy the built JAR file from the build stage
COPY --from=build /app/target/*.jar app.jar

# Copy the data.sql file to initialize the H2 database
COPY src/main/resources/data.sql /app/data.sql

# Expose the port your Spring Boot app runs on (default is 8080)
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]