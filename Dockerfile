FROM openjdk:17
COPY config/_application-prod.yaml /config/_application-prod.yaml
COPY ./resources /resources
COPY ./src/main/resources /src/main/resources
COPY target/jira-1.0.jar jira-1.0.jar
COPY .env .env
ENTRYPOINT ["java", "-jar", "/jira-1.0.jar", "--spring.profiles.active=prod"]