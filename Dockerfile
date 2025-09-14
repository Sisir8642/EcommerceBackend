# Use Maven with Java 17 for building
FROM maven:3.9.5-eclipse-temurin-17 AS builder

# Set working directory
WORKDIR /app

# Copy project files
COPY . .

# Build the app
RUN mvn clean package -DskipTests

# Use a smaller Java image for runtime
FROM eclipse-temurin:17-jdk-alpine

WORKDIR /app

# Copy only the built jar file from the builder stage
COPY --from=builder /app/target/*.jar app.jar

# Expose the application port
EXPOSE 8081

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
