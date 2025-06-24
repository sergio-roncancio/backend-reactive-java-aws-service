FROM eclipse-temurin:21-jre-alpine

RUN addgroup -S appgroup && adduser -S appuser -G appgroup

USER appuser

EXPOSE 8080

ENTRYPOINT ["java", "-XX:+UseContainerSupport", "-Xms512m", "-Xmx512m", "-jar", "transfers-0.0.1-SNAPSHOT.jar"]