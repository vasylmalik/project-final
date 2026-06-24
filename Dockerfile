FROM eclipse-temurin:17-jdk

WORKDIR /app

COPY target/jira-1.0.jar app.jar
COPY application-secrets.yaml .
COPY resources ./resources

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app/app.jar"]