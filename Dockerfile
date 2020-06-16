FROM openjdk:11
WORKDIR '/aggregation'
ARG ARTIFACT_NAME=wiosna-demo-0.0.1-SNAPSHOT.jar
ARG ARTIFACT_PATH=target/${ARTIFACT_NAME}
COPY $ARTIFACT_PATH .
EXPOSE 8081
ENV SERVER_PORT=8081
ENTRYPOINT ["java", "-jar", "wiosna-demo-0.0.1-SNAPSHOT.jar"]