# Use an official OpenJDK runtime as a parent image
FROM openjdk:17-jdk-slim

# Set the working directory in the container
WORKDIR /app

# Add the Maven wrapper and the pom.xml file
COPY .mvn/ .mvn
COPY mvnw pom.xml ./

# Ensure the Maven wrapper has execute permissions
RUN chmod +x mvnw

# Install dependencies (this will create a layer in the Docker image)
RUN ./mvnw dependency:go-offline

# Copy the source code into the container
COPY src ./src

# Print environment variables for debugging
RUN echo "DATABASE_HOST=$DATABASE_HOST"
RUN echo "DATABASE_PORT=$DATABASE_PORT"
RUN echo "DATABASE_NAME=$DATABASE_NAME"
RUN echo "DATABASE_USERNAME=$DATABASE_USERNAME"
RUN echo "DATABASE_PASSWORD=$DATABASE_PASSWORD"
RUN echo "JWT_SECRET_KEY=$JWT_SECRET_KEY"

# Package the application
RUN ./mvnw clean package -DskipTests

# Print the contents of the target directory to debug
RUN ls -l target/

# Make port 9090 available to the world outside this container
EXPOSE 9090

# Run the JAR file
ENTRYPOINT ["java", "-jar", "target/task_management_backend-0.0.1-SNAPSHOT.jar"]
