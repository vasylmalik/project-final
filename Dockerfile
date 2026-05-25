#FROM postgres:18
#RUN apt-get update && apt-get install -y tzdata
#ENV TZ=Europe/Kyiv
## Оновлення tzdata та перевстановлення /etc/localtime
#RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone


#FROM eclipse-temurin:17-jre
#
#RUN apt-get update && apt-get install -y tzdata && rm -rf /var/lib/apt/lists/*
#
#ENV TZ=Europe/Kyiv
#RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone
#
#WORKDIR /app
#
## Копіюємо лише правильний виконуваний jar (ігноруючи .jar.original)
##COPY target/*[!o][!r][!i][!g][!i][!n][!a][!l].jar app.jar
#COPY target/*.jar app.jar
#
#ENTRYPOINT ["java", "-jar", "app.jar"]


FROM eclipse-temurin:17-jre

RUN apt-get update && apt-get install -y tzdata && rm -rf /var/lib/apt/lists/*

ENV TZ=Europe/Kyiv
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

WORKDIR /app

# Універсальне копіювання будь-якого головного jar-файлу
COPY target/*.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]

