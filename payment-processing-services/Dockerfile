FROM maven:3-eclipse-temurin-21 as builder

COPY CommonMessaging /usr/src/app/CommonMessaging
WORKDIR /usr/src/app/CommonMessaging
RUN mvn clean install

COPY payment-processing-services /usr/src/app/payment-processing-services
WORKDIR /usr/src/app/payment-processing-services
RUN mvn clean package

FROM openjdk:11-jre-slim
COPY --from=builder /usr/src/app/payment-processing-services/target/payment-processing-services-0.0.1-SNAPSHOT.jar /usr/app/payment-processing-services.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/usr/app/microservice.jar"]
