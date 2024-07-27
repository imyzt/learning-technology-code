FROM adoptopenjdk:8-openj9
WORKDIR /app
ADD target/crash-demo-0.0.1-SNAPSHOT.jar /app/java.jar
ENV TZ Asia/Shanghai
EXPOSE 8080
CMD ["java", "-jar", "/app/java.jar"]
