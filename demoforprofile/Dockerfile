FROM alpine:latest

LABEL maintainer="epopoola" version="0.1"

RUN apk upgrade --available
RUN apk --no-cache add openjdk17

COPY target/demoforprofile*.jar app.jar

ENTRYPOINT ["java","-jar", "app.jar"]

EXPOSE 8889
