FROM openjdk:17

COPY target/jira-1.0.jar ./jira-1.0.jar
COPY resources ./resources

ENTRYPOINT ["java", "-Dspring.profiles.active=prod", "-jar", "/jira-1.0.jar"]
