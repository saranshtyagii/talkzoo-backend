# Stage 1: Build the application
FROM maven:3.9.6-eclipse-temurin-21-alpine AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# Stage 2: Run the application
FROM eclipse-temurin:21-jdk-alpine
WORKDIR /app
COPY --from=build /app/target/talkzoo-eureka-0.0.1-SNAPSHOT.jar talkzoo-eureka.jar
COPY --from=build /app/target/talkzoo-auth-0.0.1-SNAPSHOT.jar talkzoo-auth.jar
COPY --from=build /app/target/notification-queue-0.0.1-SNAPSHOT.jar notification-queue.jar

# Run whichever jar you want as default (example: eureka)
CMD ["java", "-jar", "talkzoo-eureka.jar"]