FROM openjdk:17-alpine

# Set the Gradle version
ARG GRADLE_VERSION=7.6.1

# Download and install Gradle
RUN apk update && apk add --no-cache wget unzip \
    && wget "https://services.gradle.org/distributions/gradle-${GRADLE_VERSION}-bin.zip" \
    && unzip "gradle-${GRADLE_VERSION}-bin.zip" \
    && rm "gradle-${GRADLE_VERSION}-bin.zip"

# Set the PATH environment variable to include Gradle
ENV PATH="/gradle-${GRADLE_VERSION}/bin:${PATH}"

# Copy the kotlin-email-service directory to the /app directory
COPY . /app

WORKDIR /app

# Set the default command to run the application
CMD ["java", "-jar", "/app/build/libs/*.jar"]

