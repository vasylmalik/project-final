FROM --platform=linux/amd64 eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY target/jira-1.0.jar jiraApp.jar
EXPOSE 8080
CMD ["java", "-jar", "jiraApp.jar"]