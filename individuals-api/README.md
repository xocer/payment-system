# Individuals-api

## Описание

Individuals API — микросервис в монорепозитории, выполняющий роль оркестратора аутентификации.
Он взаимодействует с **Keycloak** через REST API и предоставляет клиентам внешние точки входа для:

- регистрации,
- логина,
- обновления токена,
- получения информации о текущем пользователе.


## Эндпоинты

🔗 [OpenAPI Spec: IndividualsApi](openapi/individuals-api.yaml)  

### IndividualsApi (`localhost:8080`)

#### Post `/v1/auth/register`
Регистрация нового пользователя

```bash
curl -X POST "http://localhost:8080/v1/auth/register" \
  -H "Content-Type: application/json" \
  -d '{
    "username": "petrov@mail.com",
    "email": "petrov@mail.com",
    "firstName": "Misha",
    "lastName": "Petrov",
    "password": "mypassword"
}'
```

#### Post `/v1/auth/login`
Аутентификация пользователя по email и паролю

```bash
curl -X POST "http://localhost:8080/v1/auth/login" \
  -H "Content-Type: application/json" \
  -d '{
    "email": "petrov@mail.com",
    "password": "mypassword"
}'
```

#### Post `/v1/auth/refresh-token`
Обновление access/refresh токена

```bash
curl -X POST "http://localhost:8080/v1/auth/refresh-token" \
  -H "Content-Type: application/json" \
  -d '{
    "refresh_token": "<refresh_token>"
}'
```

#### Get `/v1/auth/me`
Получение информации о текущем пользователе

```bash
curl -X GET "http://localhost:8080/v1/auth/me" \
  -H "Authorization: Bearer <access_token>"
```

---

## Быстрый старт через Docker Compose
В build.gradle.kts в разделе openApiGenerator нужно выставить `inputSpec.set("openapi/individuals-api.yaml")` и 
закомментировать `inputSpec.set("$rootDir/individuals-api/openapi/individuals-api.yaml")`
```bash
docker compose up --build -d
```

Это запустит:
- PostgreSQL (`localhost:5432`)
- Keycloak (`localhost:8980`)
- IndividualsApi (`localhost:8080`)
- Prometheus (`localhost:9090`)
- Grafana (`localhost:3000`)

## Проверка

Получение токена от `eventapp`:
```bash
curl -X POST   http://localhost:9090/realms/proselyte/protocol/openid-connect/token \
  -H 'Content-Type: application/x-www-form-urlencoded' \
  -d 'grant_type=client_credentials&client_id=eventapp&client_secret=<your-secret>'
```

---

## Стек технологий
- Java 24
- Spring Boot 3.5.0 (WebFlux, Security, Actuator)
- Keycloak 26
- Docker / Docker Compose
- Gradle
- Prometheus
- Micrometer
- Loki
- OpenAPI 3.0
- JUnit 5
- Mockito
- Testcontainers
- Grafana

## Автор
[Viktor Grishin](https://github.com/xocer)
