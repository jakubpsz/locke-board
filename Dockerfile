# ============================
# 1) BUILD STAGE
# ============================
FROM eclipse-temurin:25-jdk AS build

# Set work directory
WORKDIR /app

# Copy Maven descriptor first (better caching)
COPY pom.xml .

# Download dependencies (cached layer)
RUN mvn -q -B dependency:go-offline

# Copy the rest of the project
COPY src ./src

# Build application
RUN mvn -q -B clean package -DskipTests


# ============================
# 2) RUNTIME STAGE
# ============================
FROM eclipse-temurin:25-jre

WORKDIR /app

# Copy JAR from build stage
COPY --from=build /app/target/whiteboard-0.1.0.jar app.jar

# Expose port Spring Boot runs on
EXPOSE 8080

# Start the application
ENTRYPOINT ["java", "-jar", "app.jar"]
