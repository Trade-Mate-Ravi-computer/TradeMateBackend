From maven:3.8.8-eclipse-temurin-21-alpine AS build
COPY . .
RUN mvn cleam package -DskipTests

From openjdk:21-jdk-slim
COPY --from=build /target/trademate-0.0.1-SNAPSHOT.jar demo.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","demo.jar"]