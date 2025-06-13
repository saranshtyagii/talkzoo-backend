# ---------- Stage 1: Build the selected module ----------
FROM maven:3.9.6-eclipse-temurin-21-alpine AS build
WORKDIR /app

# Accept module name from build argument
ARG MODULE=talkzoo-auth

# Copy all project files
COPY . .

# Build only the specified module and its dependencies
RUN mvn clean package -DskipTests -pl ${MODULE} -am

# ---------- Stage 2: Run the selected module ----------
FROM eclipse-temurin:21-jdk-alpine
WORKDIR /app

# Accept module name again for this stage
ARG MODULE=auth-service

# Copy the JAR from the built module
COPY --from=build /app/${MODULE}/target/*.jar app.jar

# Run the application
CMD ["java", "-jar", "app.jar"]
