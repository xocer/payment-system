# Стадия сборки
FROM eclipse-temurin:24-jdk as builder

WORKDIR /app

COPY . /app

# Кэш слоёв зависимостей (важно!)
RUN ./gradlew dependencies --no-daemon

# Собираем jar
RUN ./gradlew bootJar --no-daemon

# Финальный образ
FROM eclipse-temurin:24-jdk

WORKDIR /app

COPY --from=builder /app/build/libs/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
