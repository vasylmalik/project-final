FROM openjdk:${OPENJDK_VERSION:-17}
WORKDIR /
COPY src ./src
COPY resources ./resources
COPY target/*.jar app.jar
ENTRYPOINT ["java","-jar","app.jar", "--spring.profiles.active=prod"]