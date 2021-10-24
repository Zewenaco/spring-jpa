FROM adoptopenjdk:11-jre-hotspot

ENV ARTIFACT_NAME=spring-jpa
RUN mkdir /opt/app
COPY ./target/*.jar /opt/app
EXPOSE 8080
CMD ["java", "-jar", "-Dspring.profiles.active=dev", "/opt/app/spring-jpa-0.1.0-SNAPSHOT.jar"]