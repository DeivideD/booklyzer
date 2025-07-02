# Build stage
FROM eclipse-temurin:17-jdk-jammy as builder
WORKDIR /workspace/app

COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .
COPY src src

RUN ./mvnw clean package -DskipTests

FROM eclipse-temurin:17-jdk-jammy
WORKDIR /app

COPY --from=builder /workspace/app/target/*.war app.war

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.war"]