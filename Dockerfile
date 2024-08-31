FROM openjdk:17
EXPOSE 8080
COPY --from=build /app/target/ecommerce-0.0.1-SNAPSHOT.jar ecommerce.jar
ENTRYPOINT ["java", "-jar", "trademate.jar"]

