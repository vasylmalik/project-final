# Stage 1: build with Maven and OpenJDK 17
FROM maven:3.9.5-eclipse-temurin-17 AS build

WORKDIR /app

COPY pom.xml .
RUN mvn dependency:go-offline

COPY src ./src

RUN mvn clean package -DskipTests

# Stage 2: runtime image with OpenJDK 17
FROM eclipse-temurin:17-jdk

WORKDIR /app

COPY --from=build /app/target/*.jar app.jar

ENTRYPOINT ["java",  "-jar", "app.jar"]