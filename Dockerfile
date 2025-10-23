# Use OpenJDK 11 base image
FROM openjdk:11-jdk-slim AS build

WORKDIR /app

COPY mvnw .mvn .mvn/
COPY pom.xml ./

RUN ./mvnw dependency:go-offline

COPY src ./src

RUN ./mvnw clean package -DskipTests

FROM openjdk:11-jre-slim

WORKDIR /app

COPY --from=build /app/target/Journal-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080

CMD ["java", "-jar", "app.jar"]
