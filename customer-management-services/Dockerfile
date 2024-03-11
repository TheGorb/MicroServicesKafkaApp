FROM maven:3-eclipse-temurin-21 as builder

COPY CommonMessaging /usr/src/app/CommonMessaging
WORKDIR /usr/src/app/CommonMessaging
RUN mvn clean install

COPY customer-management-services /usr/src/app/customer-management-services
WORKDIR /usr/src/app/customer-management-services
RUN mvn clean package

FROM openjdk:11-jre-slim
COPY --from=builder /usr/src/app/customer-management-services/target/customer-management-services-0.0.1-SNAPSHOT.jar /usr/app/customer-management-services.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/usr/app/microservice.jar"]
