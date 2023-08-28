FROM openjdk:17
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar
COPY ./resources/view /resources/view
COPY ./resources/mails /resources/mails
COPY ./resources/static /resources/static


ENTRYPOINT ["java","-jar","/app.jar"]