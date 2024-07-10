# Use the official Maven image from the Docker Hub as the base image for the build stage
FROM maven:3.8.5-openjdk-17 AS build

# Set the working directory
WORKDIR /app

# Copy the entire project to the container
COPY . .

# Run Maven package command to build the project and skip the tests
RUN mvn clean package -DskipTests

# Use the official OpenJDK image as the base image for the runtime stage
FROM openjdk:17-jdk-slim

# Set the working directory
WORKDIR /app

# Copy the JAR file from the build stage to the runtime stage
COPY --from=build /app/target/trademate-0.0.1-SNAPSHOT.jar trademate.jar

# Expose the port the application will run on
EXPOSE 8080

# Set the entry point to run the JAR file
ENTRYPOINT ["java", "-jar", "/app/trademate.jar"]
