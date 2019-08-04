FROM maven:3.5.2-jdk-8-alpine as CREATED_JAR
COPY pom.xml /tmp/
COPY src /tmp/src/
WORKDIR /tmp/
RUN mvn clean package -DskipTests

FROM openjdk:8-jre-alpine
COPY --from=CREATED_JAR /tmp/target/search-api-0.1.0-SNAPSHOT.jar /tmp/search-api.jar
WORKDIR /tmp/
ENV JAVA_OPTS=""
ENTRYPOINT exec java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar search-api.jar