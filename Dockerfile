FROM openjdk:8-jdk-alpine

WORKDIR /app

COPY target/gic-bpm-apps-0.0.1-SNAPSHOT.jar app.jar

ENTRYPOINT ["java","-jar","app.jar"]