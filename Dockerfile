# === ЭТАП СБОРКИ ===
FROM maven:3.9.6-eclipse-temurin-17-alpine AS builder

WORKDIR /app

# Шаг 1: Копируем только файл настроек зависимостей.
COPY pom.xml .

# Скачиваем зависимости в офлайн-режим
RUN mvn dependency:go-offline -B

# Шаг 2: Копируем исходный код проекта
COPY src ./src

# Собираем приложение в jar-пакет, пропуская тесты для ускорения сборки
RUN mvn clean package -DskipTests

# === ЭТАП ЗАПУСКА ===
FROM eclipse-temurin:17-jre-alpine AS runner

WORKDIR /app

# Создаем безопасную группу и пользователя spring, чтобы не запускать приложение от root
RUN addgroup -S spring && adduser -S spring -G spring

# Копируем скомпилированный jar-файл из предыдущего этапа
COPY --from=builder /app/target/*.jar app.jar

# Меняем владельца директории приложения на пользователя spring
RUN chown -R spring:spring /app

# Переключаемся на безопасного пользователя
USER spring:spring

# Стандартный порт для Spring Boot приложений
EXPOSE 8080

ENTRYPOINT  ["java", "-XX:+UseG1GC", "-jar", "app.jar"]