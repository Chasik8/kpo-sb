
FROM gradle:8.5-jdk17 AS builder

WORKDIR /home/gradle/project

COPY . .


RUN ./gradlew :analysis-service:bootJar --no-daemon -x test


FROM eclipse-temurin:17-jre-alpine
WORKDIR /app


COPY --from=builder /home/gradle/project/analysis-service/build/libs/*.jar app.jar

EXPOSE 8082

ENTRYPOINT ["java", "-jar", "app.jar"]