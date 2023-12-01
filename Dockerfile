# Backend builder image
FROM maven:3.8.4-openjdk-17-slim AS backend-builder
WORKDIR /backend
COPY ./ems-backend .
RUN mvn clean package -DskipTests

# Frontend builder image
FROM node:18 AS frontend-builder
WORKDIR /frontend
COPY ./ems-frontend .
RUN npm install && npm run build

# Runtime image
FROM openjdk:17-jdk-slim
RUN mkdir /app
WORKDIR /app
COPY --from=backend-builder /backend/target/*.jar app.jar
COPY --from=frontend-builder /frontend/build /frontend
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]