# Docker example using fatjar
# - docker build -t example/vertx-starter .
# - docker run -t -i -p 8888:8888 example/vertx-starter

# https://hub.docker.com/_/adoptopenjdk
FROM adoptopenjdk:11-jre-hotspot

# Alternative https://hub.docker.com/_/amazoncorretto
# FROM amazoncorretto:11

ENV FAT_JAR vertx-starter-1.0.0-SNAPSHOT-fat.jar
ENV APP_HOME /usr/app

EXPOSE 8888

COPY build/libs/$FAT_JAR $APP_HOME/

WORKDIR $APP_HOME
ENTRYPOINT ["sh", "-c"]
CMD ["exec java -jar $FAT_JAR"]
