FROM maven:3-eclipse-temurin-21 as builder

COPY ../CommonMessaging /usr/src/CommonMessaging
RUN cd usr/src/CommonMessaging && mvn clean install

COPY balance-services /usr/src/balance-services
RUN cd /usr/src/balance-services && mvn test && mvn clean package

FROM eclipse-temurin:21
COPY --from=builder /usr/src/balance-services/target/balance-services-*.jar /usr/app/balance-services.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/usr/app/balance-services.jar"]
