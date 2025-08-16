FROM amazoncorretto:21-alpine-jdk
COPY target/ecs-demo.jar ecs-demo.jar
EXPOSE 8080
ENTRYPOINT [ "java", "-jar", "ecs-demo.jar" ]