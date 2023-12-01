# Use an official Maven image as a build stage
FROM maven:3.8.4-openjdk-11-slim AS backend-builder

# Set the working directory inside the container
WORKDIR /app

# Copy only the POM file to resolve dependencies
COPY ems-backend/pom.xml .

# Download dependencies and build the project
RUN mvn dependency:go-offline

# Copy the rest of the backend code
COPY ems-backend .

# Build the Spring Boot application
RUN mvn clean install

# Use an official Node.js image as another build stage
FROM node:14-alpine AS frontend-builder

# Set the working directory inside the container
WORKDIR /app

# Copy package.json and package-lock.json to install dependencies
COPY ems-frontend/package*.json ./

# Install frontend dependencies
RUN npm install

# Copy the rest of the frontend code
COPY ems-frontend .

# Build the React app
RUN npm run build

# Use a lightweight runtime image for the final stage
FROM openjdk:11-jre-slim

# Set the working directory inside the container
WORKDIR /app

# Copy the compiled JAR file from the backend-builder stage
COPY --from=backend-builder /app/target/ems-backend.jar .

# Copy the build artifacts from the frontend-builder stage
COPY --from=frontend-builder /app/build ./ems-frontend/build

# Expose the port that your Spring Boot application will run on
EXPOSE 8080

# Command to run your Spring Boot application
CMD ["java", "-jar", "ems-backend.jar"]