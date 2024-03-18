FROM maven:3-eclipse-temurin-21 as builder

COPY ../CommonMessaging /usr/src/CommonMessaging
RUN cd usr/src/CommonMessaging && mvn clean install

COPY customer-management-services /usr/src/customer-management-services
RUN cd /usr/src/customer-management-services && mvn clean package

FROM openjdk:11-jre-slim
COPY --from=builder /usr/src/customer-management-services/customer-management-services-*.jar /usr/app/customer-management-services.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/usr/app/customer-management-services.jar"]
