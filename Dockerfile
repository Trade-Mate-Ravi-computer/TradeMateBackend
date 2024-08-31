FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app
COPY --from=build /app/target/trademate-0.0.1-SNAPSHOT.jar trademate.jar
EXPOSE 8080
CMD ["java","-jar","/app/trademate.ja"]
