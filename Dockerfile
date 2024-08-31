FROM openjdk:17
EXPOSE 8080
ADD target/trademate-0.0.1-SNAPSHOT.jar trademate.jar
ENTRYPOINT ["java", "-jar", "/app/trademate.jar"]
