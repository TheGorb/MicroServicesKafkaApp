FROM maven:3-eclipse-temurin-21 as builder

COPY ../CommonMessaging /usr/src/CommonMessaging
RUN cd usr/src/CommonMessaging && mvn clean install

COPY payment-processing-services /usr/src/payment-processing-services
RUN cd /usr/src/payment-processing-services && mvn test && mvn clean package

FROM eclipse-temurin:21
COPY --from=builder /usr/src/payment-processing-services/target/payment-processing-services-*.jar /usr/app/payment-processing-services.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/usr/app/payment-processing-services.jar"]
