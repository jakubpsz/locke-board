# ---------- Build stage ----------
FROM maven:3.9.3-eclipse-temurin-17 AS build

# Set working directory
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline
COPY src ./src
RUN mvn clean package -Dmaven.test.skip=true

# ---------- Runtime stage ----------
FROM eclipse-temurin:17-jdk AS runtime

WORKDIR /app
COPY --from=build /app/target/*.jar app.jar

# Expose application port
EXPOSE 8080

CMD ["java", "-jar", "app.jar"]
