FROM gradle:7.6.1-jdk17-alpine AS build

# Set the working directory.
WORKDIR /app

# Copy the Gradle configuration files to the Docker image.
# This includes the `build.gradle.kts` file and `gradle` directory with the `gradle-wrapper.properties` file.
COPY build.gradle.kts gradle.properties settings.gradle ./
COPY gradle gradle

# Copy the source code to the Docker image.
COPY src src

# Build the application using Gradle.
# This will download all necessary dependencies and create an executable JAR file.
RUN gradle bootJar --no-daemon

# Use the official OpenJDK image for a lean production stage of our multi-stage build.
# https://hub.docker.com/_/openjdk
# Use an Alpine-based image for smallest size.
FROM openjdk:17-alpine

# Set the working directory.
WORKDIR /app

# Copy the jar file from the build stage to the production stage.
COPY --from=build /app/build/libs/*.jar email-service.jar

# Run the application.
CMD ["java", "-jar", "/app/build/libs/email-service.jar"]