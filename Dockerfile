FROM maven:3.9-eclipse-temurin-17 AS build

WORKDIR /build
COPY pom.xml .

RUN mvn dependency:go-offline
COPY src ./src

RUN mvn clean package -DskipTests

FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

COPY --from=build /build/target/jira-1.0.jar app.jar
COPY resources ./resources

EXPOSE 8081

ENTRYPOINT ["java", "-jar", "app.jar", "--spring.profiles.active=prod"]