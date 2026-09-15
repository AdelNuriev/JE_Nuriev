FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /workspace

COPY pom.xml .
COPY discovery-server/pom.xml discovery-server/
COPY discovery-starter/pom.xml discovery-starter/
COPY user-service/pom.xml user-service/
COPY order-service/pom.xml order-service/

RUN mvn -pl order-service -am dependency:go-offline

COPY discovery-starter/src discovery-starter/src
COPY order-service/src order-service/src

RUN mvn -pl order-service -am package -DskipTests

FROM eclipse-temurin:17-jre
WORKDIR /app

COPY --from=build /workspace/order-service/target/*.jar app.jar

EXPOSE 8082

ENTRYPOINT ["java", "-jar", "app.jar"]