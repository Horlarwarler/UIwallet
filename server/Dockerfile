# ====== Stage 1: Cache Gradle Dependencies ======
FROM gradle:latest AS cache

# Set Gradle cache directory
RUN mkdir -p /home/gradle/cache_home
ENV GRADLE_USER_HOME /home/gradle/cache_home

# Copy necessary Gradle-related files (Project-level)
COPY build.gradle.kts settings.gradle.kts gradle.properties /home/gradle/project/
COPY gradle /home/gradle/project/gradle

# Copy only the shared module's required files
COPY shared/build.gradle.kts /home/gradle/project/shared/
COPY shared/src/commonMain /home/gradle/project/shared/src/commonMain

# Copy only the server module's required files
COPY server/build.gradle.kts /home/gradle/project/server/
COPY server/src /home/gradle/project/server/src

# Move into project directory
WORKDIR /home/gradle/project

# Fetch dependencies only
RUN gradle dependencies --no-daemon

# ====== Stage 2: Build the Server ======
FROM gradle:latest AS build

# Restore Gradle cache
COPY --from=cache /home/gradle/cache_home /home/gradle/.gradle

# Copy only necessary files again to ensure everything needed is present
COPY build.gradle.kts settings.gradle.kts gradle.properties /home/gradle/project/
COPY gradle /home/gradle/project/gradle
COPY shared/build.gradle.kts /home/gradle/project/shared/
COPY shared/src/commonMain /home/gradle/project/shared/src/commonMain
COPY server/build.gradle.kts /home/gradle/project/server/
COPY server/src /home/gradle/project/server/src

# Change to project root to resolve dependencies properly
WORKDIR /home/gradle/project

# Build the fat JAR for the server module
RUN gradle :server:shadowJar --no-daemon

# ====== Stage 3: Create the Runtime Image ======
FROM amazoncorretto:22 AS runtime

# Expose the server's port
EXPOSE 8080

# Create the app directory
RUN mkdir /app

# Copy the built JAR from the build stage
COPY --from=build /home/gradle/project/server/build/libs/*.jar /app/ktor-docker-sample.jar

# Set entrypoint to run the server JAR
ENTRYPOINT ["java", "-jar", "/app/ktor-docker-sample.jar"]
