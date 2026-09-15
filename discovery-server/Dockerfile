FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /workspace

COPY pom.xml .
COPY discovery-server/pom.xml discovery-server/
COPY discovery-starter/pom.xml discovery-starter/
COPY user-service/pom.xml user-service/
COPY order-service/pom.xml order-service/

RUN mvn -pl discovery-server dependency:go-offline

COPY discovery-server/src discovery-server/src

RUN mvn -pl discovery-server package -DskipTests

FROM eclipse-temurin:17-jre
WORKDIR /app

COPY --from=build /workspace/discovery-server/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]