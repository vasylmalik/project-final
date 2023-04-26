FROM maven:3.8.7-openjdk-18-slim

LABEL maintainer="Konstantin"
LABEL description="JiraRush application"
LABEL version="1.0"
LABEL profiles="prod (for run with postgresql in compose)"
LABEL build="docker build -t jira-prod-img ."
LABEL run="docker-compose up -d"

WORKDIR /app
COPY pom.xml .
COPY src ./src
COPY resources ./resources
COPY lombok.config ./lombok.config
COPY config/_application-prod.yaml ./src/main/resources/application-prod.yaml
RUN mvn clean package -DskipTests
RUN mv ./target/*.jar ./app.jar
RUN rm -rf ./target
RUN rm -rf ./src
EXPOSE 8080
ENTRYPOINT ["sh", "-c", "cd /app && java -jar /app/app.jar --spring.profiles.active=prod"]