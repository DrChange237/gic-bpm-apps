FROM openjdk:11.0.1-jdk-slim
WORKDIR /app
COPY gic-service.jar /app/gic-service.jar
ENTRYPOINT ["java", "-jar", "gic-service.jar"]