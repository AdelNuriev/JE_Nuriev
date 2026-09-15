FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /workspace

COPY pom.xml .
COPY discovery-server/pom.xml discovery-server/
COPY discovery-starter/pom.xml discovery-starter/
COPY user-service/pom.xml user-service/
COPY order-service/pom.xml order-service/

RUN mvn -pl user-service -am dependency:go-offline

COPY discovery-starter/src discovery-starter/src
COPY user-service/src user-service/src

RUN mvn -pl user-service -am package -DskipTests

FROM eclipse-temurin:17-jre
WORKDIR /app

COPY --from=build /workspace/user-service/target/*.jar app.jar

EXPOSE 8081

ENTRYPOINT ["java", "-jar", "app.jar"]