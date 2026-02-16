# Build stage
# 1. Build stage avec Maven + JDK 11
FROM maven:3.8.8-openjdk-11 AS build
WORKDIR /app

# Copier uniquement le pom.xml pour le cache Maven
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copier le code source
COPY src ./src

# Compiler l'application
RUN mvn clean package -DskipTests

# 2. Runtime stage avec JRE 11 léger
FROM openjdk:11-jre-slim
WORKDIR /app

# Copier le jar compilé depuis le build stage
COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java","-jar","app.jar"]