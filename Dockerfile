# Build stage
FROM maven:3.8.6-openjdk-11-slim AS build
WORKDIR /app

# Copier pom.xml et télécharger les dépendances
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copier le code source et builder
COPY src ./src
RUN mvn clean package -DskipTests

# Runtime stage - Utiliser Eclipse Temurin au lieu de openjdk
FROM eclipse-temurin:11-jre-alpine
WORKDIR /app

# Copier le JAR depuis le build stage
COPY --from=build /app/target/*.jar app.jar

# Port de l'application
EXPOSE 8080

# Variables d'environnement
ENV JAVA_OPTS=""

# Démarrer l'application
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]