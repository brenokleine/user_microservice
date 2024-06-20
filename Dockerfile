FROM maven:3.8.5-openjdk-17 AS build

COPY src /app/src
COPY pom.xml /app

WORKDIR /app
RUN mvn clean install

FROM openjdk:17-jdk-slim

COPY --from=build /app/target/user_microservice-0.0.1-SNAPSHOT.jar /app/app.jar

WORKDIR /app

EXPOSE 8082

CMD ["java", "-jar", "app.jar"]
