FROM eclipse-temurin:8-jdk-alpine

WORKDIR /app

COPY target/gic-service-0.0.1-SNAPSHOT.jar app.jar

ENTRYPOINT ["java","-jar","app.jar"]