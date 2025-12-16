# Use stable Java 17 image (Render supports this)
FROM eclipse-temurin:17-jdk

# Create working directory inside container
WORKDIR /app

# Copy the built jar from target folder into container
# IMPORTANT: mvn clean package must be run before deploy
COPY target/*.jar app.jar

# Expose Spring Boot port
EXPOSE 8080

# Run the Spring Boot application
ENTRYPOINT ["java","-jar","/app/app.jar"]
