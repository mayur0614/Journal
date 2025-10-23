# Stage 1: Build the application using Maven official image
FROM maven:3.8.6-openjdk-11 AS build

# Set working directory
WORKDIR /app

# Copy POM and source code
COPY pom.xml .
COPY src ./src

# Build the Spring Boot application and skip tests
RUN mvn clean package -DskipTests

# Stage 2: Create a lightweight runtime image
FROM openjdk:11-jre-slim

# Set working directory
WORKDIR /app

# Copy the built JAR from the build stage
COPY --from=build /app/target/Journal-0.0.1-SNAPSHOT.jar app.jar

# Expose the port (Render sets PORT automatically)
EXPOSE 8080

# Run the Spring Boot app with the assigned PORT
CMD ["sh", "-c", "java -jar app.jar --server.port=${PORT:-8080} --server.address=0.0.0.0"]
