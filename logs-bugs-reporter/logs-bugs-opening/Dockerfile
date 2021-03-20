FROM openjdk:14.0.2
EXPOSE 9292
ADD ./target/logs-bugs-opening-0.0.1.jar app.jar
ENTRYPOINT ["java","-jar","app.jar"]