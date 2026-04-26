# Step 1: Build the application
FROM maven:3.8.4-openjdk-17-slim AS build
COPY . /app
WORKDIR /app
RUN mvn clean package -DskipTests

# Step 2: Run the application
FROM eclipse-temurin:17-jdk-alpine
COPY --from=build /app/target/*.jar app.jar
# Use $PORT environment variable provided by Render
ENTRYPOINT ["java","-Dserver.port=${PORT:8080}","-jar","/app.jar"]
EXPOSE 8080