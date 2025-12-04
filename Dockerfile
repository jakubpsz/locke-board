# ---------- Build stage ----------
FROM maven:3.9.11-jdk-25 AS build

# Set working directory
WORKDIR /app

# Copy pom.xml first to leverage Docker cache
COPY pom.xml .

# Download dependencies
RUN mvn dependency:go-offline

# Copy source code
COPY src ./src

# Package the application
RUN mvn clean package -DskipTests

# ---------- Runtime stage ----------
FROM eclipse-temurin:25-jdk

WORKDIR /app
COPY --from=build /app/target/*.jar app.jar

# Expose application port
EXPOSE 8080

CMD ["java", "-jar", "app.jar"]
