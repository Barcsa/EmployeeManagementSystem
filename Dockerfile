# Multi-stage Dockerfile for Java Spring Boot Backend and React Frontend

# Stage 1: Build the Java Spring Boot Backend
FROM openjdk:11-jre-slim as backend-builder

WORKDIR /app

# Copy only the necessary files for building the backend
COPY ems-backend/pom.xml ems-backend/
COPY ems-backend/src ems-backend/src/

# Build the backend application
RUN cd ems-backend && ./mvnw clean package -DskipTests

# Stage 2: Build the React Frontend
FROM node:14-alpine as frontend-builder

WORKDIR /app

# Copy only the necessary files for building the frontend
COPY ems-frontend/package.json ems-frontend/package-lock.json ems-frontend/
COPY ems-frontend/public ems-frontend/public/
COPY ems-frontend/src ems-frontend/src/

# Install dependencies and build the frontend application
RUN cd ems-frontend && npm install && npm run build

# Stage 3: Create the final image
FROM openjdk:11-jre-slim

WORKDIR /app

# Copy the backend JAR file from the backend-builder stage
COPY --from=backend-builder /app/ems-backend/target/your-backend.jar /app/ems-backend.jar

# Copy the static files from the frontend-builder stage to the Nginx web root directory
COPY --from=frontend-builder /app/ems-frontend/build /usr/share/nginx/html

# Expose port 8080 for the backend
EXPOSE 8080

# Expose port 80 for the frontend (assuming it will be served through a reverse proxy or Nginx)
EXPOSE 80

# Start the backend when the container launches
CMD ["java", "-jar", "ems-backend.jar"]