
FROM gradle:8.5-jdk17 AS builder

WORKDIR /home/gradle/project
COPY . .


RUN ./gradlew :gateway-service:bootJar --no-daemon -x test


FROM eclipse-temurin:17-jre-alpine
WORKDIR /app


COPY --from=builder /home/gradle/project/gateway-service/build/libs/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]