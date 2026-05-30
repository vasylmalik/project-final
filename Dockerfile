FROM maven:3.9.6-eclipse-temurin-17 AS build

WORKDIR /app

COPY pom.xml .
COPY src ./src
COPY resources ./resources
COPY config ./config

RUN mvn clean package -DskipTests

FROM eclipse-temurin:17-jdk

WORKDIR /app

COPY --from=build /app/target/*.jar app.jar
COPY --from=build /app/resources ./resources
COPY --from=build /app/config ./config

EXPOSE 8080

CMD ["java", "-jar", "app.jar"]