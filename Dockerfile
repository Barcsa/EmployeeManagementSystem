# Stage 1: Build the Java Spring Boot Application
FROM maven:3.8.4-openjdk-17-slim AS build-backend
WORKDIR /app

# Copy only necessary files for Maven dependencies
COPY ems-backend/pom.xml .
COPY ems-backend/.mvn .mvn

# Download dependencies only (skip building the actual application)
RUN mvn dependency:go-offline

# Copy the entire backend source code
COPY ems-backend/src src

# Build the application
RUN mvn package -DskipTests

# Stage 2: Build the React Frontend
FROM node:16 AS build-frontend
WORKDIR /app

# Copy only necessary files for npm dependencies
COPY ems-frontend .

# Install npm dependencies
RUN npm ci

# Copy the entire frontend source code
COPY ems-frontend/src src
COPY ems-frontend/public public

# Build the React application
RUN npm run build

# Stage 3: Create the final image
FROM openjdk:17-jdk-slim
WORKDIR /app

# Copy the built JAR file from the first stage
COPY --from=build-backend /app/target/*.jar app.jar

# Copy the built React frontend from the second stage
COPY --from=build-frontend /app/build/ ems-frontend/

# Expose the port that the application will run on
EXPOSE 8080

# Command to run the application
CMD ["java", "-jar", "app.jar"]