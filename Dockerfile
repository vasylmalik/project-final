FROM maven:3.9.9-amazoncorretto-17 AS build
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline -B
COPY src ./src
RUN mvn clean package -Pprod -DskipTests

# Runtime stage
FROM --platform=linux/amd64 eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=build /app/target/*.jar jiraApp.jar
EXPOSE 8080
CMD ["java", "-jar", "jiraApp.jar"]