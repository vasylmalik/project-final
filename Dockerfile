FROM maven:3.9.6-amazoncorretto-17-debian
LABEL maintainer="Timofej"
LABEL description="JiraRush application"
LABEL version="1.0"
WORKDIR /opt/jirarush
COPY pom.xml .
COPY src ./src
COPY resources ./resources
COPY lombok.config ./lombok.config
COPY config/_application-prod.yaml ./src/main/resources/application-prod.yaml
RUN mvn clean install -DskipTests
RUN mv ./target/*.jar ./jirarush.jar
RUN rm -rf ./target
RUN rm -rf ./src
RUN rm lombok.config
RUN rm pom.xml
EXPOSE 8080
ENTRYPOINT [ "java", "-Dspring.profiles.active=prod", "-jar", "jirarush.jar" ]