FROM openjdk:8-jdk-alpine
EXPOSE 8761
VOLUME /tmp
ARG JAR_FILE
COPY build/libs/registry.jar app.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]