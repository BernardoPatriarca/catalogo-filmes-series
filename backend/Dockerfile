FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /build

COPY pom.xml .
RUN mvn -B -q dependency:go-offline

COPY src ./src
RUN mvn -B -q -DskipTests package

FROM eclipse-temurin:21-jre-alpine
WORKDIR /deployments

RUN addgroup -S quarkus && adduser -S quarkus -G quarkus
COPY --from=build --chown=quarkus:quarkus /build/target/quarkus-app/lib/ ./lib/
COPY --from=build --chown=quarkus:quarkus /build/target/quarkus-app/*.jar ./
COPY --from=build --chown=quarkus:quarkus /build/target/quarkus-app/app/ ./app/
COPY --from=build --chown=quarkus:quarkus /build/target/quarkus-app/quarkus/ ./quarkus/

USER quarkus
EXPOSE 8080
ENV QUARKUS_HTTP_HOST=0.0.0.0

ENTRYPOINT ["java", "-jar", "quarkus-run.jar"]
