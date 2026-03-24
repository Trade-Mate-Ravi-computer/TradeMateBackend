# Build stage
FROM maven:3.8.5-openjdk-17 AS build

# Set working directory
WORKDIR /app

# Copy project files
COPY . .

# Build the project
RUN mvn clean package -DskipTests

# Runtime stage (FIXED IMAGE)
FROM eclipse-temurin:17-jdk

# Set working directory
WORKDIR /app

# Copy JAR from build stage
COPY --from=build /app/target/trademate-0.0.1-SNAPSHOT.jar app.jar

# Expose port
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
