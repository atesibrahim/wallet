FROM eclipse-temurin:17-jdk-alpine

WORKDIR /app
COPY wallet-1.0.jar wallet-app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "wallet-app.jar"]