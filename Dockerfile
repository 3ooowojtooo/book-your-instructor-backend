#
# Build stage
#
FROM maven:3.6.0-jdk-11-slim AS build
COPY adapter /home/app/adapter
COPY model /home/app/model
COPY usecase /home/app/usecase
COPY pom.xml /home/app
RUN mvn -f /home/app/pom.xml clean package

#
# Package stage
#
FROM openjdk:11-jre-slim

ENV TZ=Europe/Warsaw

COPY --from=build /home/app/adapter/target/adapter.jar /usr/local/lib/app.jar
EXPOSE 8080

CMD ["java", "-jar", "/usr/local/lib/app.jar"]