FROM openjdk:17
EXPOSE 8080
COPY --from=build /app/target/trademate-0.0.1-SNAPSHOT.jar trademate.jar
ENTRYPOINT ["java", "-jar", "trademate.jar"]

