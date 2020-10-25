#
# Build stage
#
FROM maven:3.6.3-openjdk-8 AS build

COPY src /usr/src/app/src
COPY pom.xml /usr/src/app

RUN mvn -f /usr/src/app/pom.xml clean package

#
# Package stage
#
FROM gcr.io/distroless/java-debian10

# Set the name of the jar
ENV APP_FILE ERDMS-0.0.1-SNAPSHOT.jar

# Open the port, inside docker network
EXPOSE 8085

# Copy JAR
COPY --from=build /usr/src/app/target/ERDMS-0.0.1-SNAPSHOT.jar /usr/app/ERDMS-0.0.1-SNAPSHOT.jar

# Launch the Spring Boot application

ENTRYPOINT ["java", "-jar", "/usr/app/ERDMS-0.0.1-SNAPSHOT.jar"]