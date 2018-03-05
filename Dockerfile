FROM java:8-jre-alpine

MAINTAINER automate.website <hello@automate.website>

COPY ./target/runner-*.jar /app.jar

CMD ["java", "-jar", "/app.jar"]
