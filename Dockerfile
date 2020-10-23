FROM openjdk:15

COPY target/vertex*.jar /opt/vertex/vertex.jar
WORKDIR /opt/vertex
EXPOSE 8080
CMD ["java", "-jar",  "vertex.jar"]