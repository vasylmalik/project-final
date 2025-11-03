# syntax=docker/dockerfile:1.7
FROM eclipse-temurin:17-jdk-jammy AS builder
WORKDIR /app

# Copy build files first to leverage Docker layer cache
COPY .mvn/ .mvn/
COPY mvnw pom.xml ./
RUN chmod +x mvnw
# Speed up builds by caching the Maven repo (BuildKit needed)
RUN --mount=type=cache,target=/root/.m2 ./mvnw -B -DskipTests dependency:go-offline

# Now add sources and build
COPY src/ src/
RUN --mount=type=cache,target=/root/.m2 ./mvnw -B -DskipTests clean package

FROM eclipse-temurin:17-jre-jammy
WORKDIR /app

# Run as non-root
RUN useradd -ms /bin/bash appuser
USER appuser

# Copy the fat jar
COPY --from=builder /app/target/*.jar /app/app.jar

EXPOSE 8080

ENTRYPOINT ["java","-jar","/app/app.jar"]
