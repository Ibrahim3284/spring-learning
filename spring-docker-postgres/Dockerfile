FROM openjdk:26-trixie
ADD target/spring-docker-postgres.jar spring-docker-postgres.jar
ENTRYPOINT ["java", "-jar", "/spring-docker-postgres.jar"]