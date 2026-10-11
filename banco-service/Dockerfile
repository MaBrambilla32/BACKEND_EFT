FROM eclipse-temurin:17-jre

WORKDIR /app

COPY banco-service/target/*.jar app.jar
COPY fin_legacy_data /fin_legacy_data

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
