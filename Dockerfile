FROM openjdk:17

#overwrite jdbc:postgresql://localhost:5432/jira from application.yaml
#ENV DATASOURCE_URL=jdbc:postgresql://172.17.0.2:5432/jira

COPY target/jira-1.0.jar ./jira-1.0.jar
COPY resources ./resources

ENTRYPOINT ["java", "-Dspring.profiles.active=prod", "-jar", "/jira-1.0.jar"]

#docker build -t jira-app .
#docker run --name jirarush -p 8080:8080 -t jira-app
