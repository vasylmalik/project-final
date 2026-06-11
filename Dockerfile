FROM maven:3.8-openjdk-17 AS build
WORKDIR /app

COPY pom.xml .
RUN mvn dependency:go-offline -B

COPY src src
RUN mvn clean package -DskipTests -B

FROM openjdk:17-jre-slim
WORKDIR /app

COPY --from=build /app/target/jira-1.0.jar app.jar

RUN mkdir -p /app/attachments

EXPOSE 8080

ENTRYPOINT ["java", "-XX:+UseContainerSupport", "-XX:MaxRAMPercentage=75.0", "-jar", "app.jar"]