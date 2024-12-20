FROM maven:3.9.9-amazoncorretto-17 AS build
WORKDIR /app

COPY pom.xml ./
COPY src ./src

RUN mvn clean package -Pprod

FROM openjdk:17-jdk-slim
WORKDIR /app

COPY --from=build /app/target/*.jar jira.jar
COPY /resources /app/resources

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app/jira.jar"]
CMD ["--spring.profiles.active=prod"]
