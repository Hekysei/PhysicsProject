# Kirchhoff Physics App

Веб-приложение на Spring Boot для моделирования электрической цепи по законам Кирхгофа. Пользователь вводит параметры источника и сопротивлений, после чего приложение рассчитывает токи, напряжения и проверяет выполнение законов Кирхгофа.

Проект подготовлен как самостоятельная feature-зона, которую можно перенести в более крупное приложение с несколькими физическими симуляторами.

## Возможности

- расчёт параметров цепи по введённым значениям `V1` и `R1-R6`;
- проверка первого и второго законов Кирхгофа;
- моделирование погрешностей входных данных;
- веб-интерфейс на Thymeleaf;
- Swagger UI для просмотра HTTP API.

## Стек

- Java 21
- Spring Boot 4
- Spring MVC
- Thymeleaf
- Spring Security
- Springdoc OpenAPI
- Maven Wrapper

## Требования

- установленная Java 21+
- права на запуск `./mvnw`

## Сборка

```bash
./mvnw clean package
```

## Запуск

Во время разработки:

```bash
./mvnw spring-boot:run
```

Запуск собранного приложения:

```bash
java -jar target/kirchhoff-physics-app.jar
```

## Основные URL

- приложение: `http://localhost:8080/kirchhoff/`
- Swagger UI: `http://localhost:8080/kirchhoff/swagger-ui/index.html`

## Конфигурация

Основные настройки находятся в `src/main/resources/application.properties`:

- `server.port=8080`
- `server.servlet.context-path=/kirchhoff`
- `spring.application.name=KirchhoffPhysics`
- `app.security.users-file=data/kirchhoff-physics-project-users.txt`

## Примечания

- данные пользователей хранятся в файле, путь задаётся через `app.security.users-file`;
- по умолчанию runtime-данные создаются в каталоге `data/`.
- для переезда в общий репозиторий см. документы в `docs/`.
