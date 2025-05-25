FROM maven:3.8.4-amazoncorretto-17 AS build
LABEL org.opencontainers.image.authors="Safargulov"

WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

FROM amazoncorretto:17-alpine
WORKDIR /app

COPY --from=build /app/target/jira-1.0.jar /app/jira-1.0.jar

COPY config /app/config

EXPOSE 8080

HEALTHCHECK --interval=30s --timeout=3s \
  CMD curl -f http://localhost:8080/actuator/health || exit 1

CMD ["java", "-Xmx512m", "-Xms256m", "-jar", "jira-1.0.jar"]