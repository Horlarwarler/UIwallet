# Use a lightweight Java runtime environment
FROM amazoncorretto:22 AS runtime

# Set working directory
WORKDIR /app

# Expose the server port
EXPOSE 8080:8080

# Copy the manually built fat JAR into the container
COPY build/libs/*.jar /app/server-all.jar

# Run the application
ENTRYPOINT ["java", "-jar", "/app/server-all.jar"]


