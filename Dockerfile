FROM openjdk:17

WORKDIR /app

COPY target/jira-1.0.jar /app/final-jira.jar
COPY src/main/resources/application.properties src/main/resources/application.properties

EXPOSE 8080

CMD ["java", "-jar", "/app/final-jira.jar"]
