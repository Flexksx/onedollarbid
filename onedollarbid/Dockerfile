# Stage 1: Build
FROM maven:3.9.6-amazoncorretto-21 AS build
WORKDIR /app

# Copy pom.xml and download dependencies
COPY pom.xml .
RUN mvn dependency:go-offline

# Copy source code
COPY src ./src

# **Logging Steps**
# List the contents of the src directory to verify files are copied
RUN echo "=== Listing src directory ===" && ls -R ./src

# Optionally, display the contents of the Main.java to confirm changes
RUN echo "=== Displaying Main.java ===" && cat ./src/main/java/onedollarbid/Main.java || echo "Main.java not found."

# Build the application
RUN mvn package -DskipTests

# Stage 2: Run
FROM amazoncorretto:21-alpine
WORKDIR /app

# Install curl for health checks
RUN apk add --no-cache curl

# Copy the built JAR from the build stage
COPY --from=build /app/target/*.jar app.jar

# **Logging Step Before Running the Application**
RUN echo "=== Built JAR Contents ===" && ls -l app.jar

# Optional: Verify the JAR file
RUN echo "=== Verifying JAR File ===" && java -jar app.jar --help || echo "JAR verification skipped."

# Entry point to run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
