# Этап сборки
FROM maven:3.9.6-eclipse-temurin-17-alpine AS build

WORKDIR /app

COPY pom.xml .
RUN mvn dependency:go-offline

COPY src ./src
COPY resources ./resources
RUN mvn clean package -DskipTests

# Этап запуска
FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

# Копируем JAR из этапа сборки
COPY --from=build /app/target/*.jar app.jar
COPY --from=build /app/resources ./resources

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]