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

### Postman коллекция
Коллекция запросов для тестирования API доступна в [postman/individuals_api.json](postman/individuals_api.json).
Импортируйте файл в Postman и выполните запросы к individuals-api.

Так же можно выполнить запрос через терминал:

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
```bash
docker compose up --build -d
```

Это запустит:
- PostgreSQL (`localhost:5432`)
- Keycloak (`localhost:8980`)
- IndividualsApi (`localhost:8080`)
- Prometheus (`localhost:9090`)
- Grafana (`localhost:3000`)

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
