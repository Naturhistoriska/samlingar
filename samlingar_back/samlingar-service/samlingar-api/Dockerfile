#FROM openjdk:8-jdk-stretch
FROM eclipse-temurin:8-jdk






COPY target/samlingarApi-thorntail.jar /usr/src/myapp/
WORKDIR /usr/src/myapp


ENTRYPOINT [""]
#EXPOSE 8080
CMD ["java", "-jar", "samlingarApi-thorntail.jar", "-Sinitdata"]
