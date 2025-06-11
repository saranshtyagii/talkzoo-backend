# ---------- Stage 1: Build ----------
FROM eclipse-temurin:21-jdk AS build

WORKDIR /app

# Copy Maven files and source code
COPY pom.xml .
COPY mvnw .
COPY .mvn .mvn
COPY src ./src

# Make the Maven wrapper executable and build the app
RUN chmod +x ./mvnw
RUN ./mvnw clean package -DskipTests

# ---------- Stage 2: Run ----------
FROM eclipse-temurin:21-jdk

WORKDIR /app

# Copy the built JAR file from the build stage
COPY --from=build /app/target/*.jar app.jar

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
