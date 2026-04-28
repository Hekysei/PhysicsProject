# Migration Handoff For Next Agent

## Goal

Перенести текущий проект в общий Spring Boot репозиторий, где симулятор Кирхгофа станет одной feature-зоной внутри общего приложения, а не отдельным корневым сервисом.

В целевой системе должен остаться только один общий `@SpringBootApplication`. Бизнес-логика симулятора переносится как feature-модуль, а не как отдельное приложение.
Авторизация этого проекта тоже считается частью самой feature, а не общей платформенной auth-системы.

## Current Project Snapshot

- Maven coordinates: `com.example.isib:kirchhoff-physics-app:0.0.1-SNAPSHOT`
- Java: `21`
- Boot entrypoint: `src/main/java/com/example/isib/KirchhoffApplication.java`
- Main config: `src/main/resources/application.properties`
- UI: Thymeleaf templates in `src/main/resources/templates`
- REST API: `src/main/java/com/example/isib/api`
- Domain logic: `src/main/java/com/example/isib/model`
- File-based auth: `src/main/java/com/example/isib/auth/FileUserAccountService.java`
- Runtime users file: `data/kirchhoff-physics-project-users.txt`

## Copy As Is First

Начинать перенос лучше с самой устойчивой части проекта:

- `src/main/java/com/example/isib/model/KirchhoffCircuitModel.java`
- `src/main/java/com/example/isib/model/KirchhoffCircuitData.java`
- `src/main/java/com/example/isib/model/KirchhoffCircuitResults.java`
- `src/main/java/com/example/isib/model/KirchhoffSimulationOutcome.java`
- `src/main/java/com/example/isib/model/KirchhoffErrorRate.java`
- `src/test/java/com/example/isib/model/KirchhoffCircuitModelTest.java`

Это ядро симулятора. Оно почти не зависит от web/security-слоя и должно переезжать раньше остального.

## Integration Assumptions

Для этого handoff предполагается, что разработчики уже:

- привели имена файлов к нужному виду;
- скорректировали site/address и внешние URL;
- устранили косметические naming-конфликты на уровне ресурсов.

Поэтому следующий агент должен сосредоточиться не на переименовании, а на правильной интеграции feature в общее приложение.
Дополнительный приоритет: структура переноса должна быть такой, чтобы следующий новый проект добавлялся по той же схеме почти без архитектурных решений с нуля.

## Move With Namespace Discipline

Даже если имена уже нормализованы, при переносе всё равно нужно сохранить изоляцию feature:

- root package `com.example.isib` заменить на пакет целевого репозитория;
- `KirchhoffApplication` не переносить как отдельный launcher;
- переносить `model` как собственную бизнес-логику feature;
- переносить UI/API контроллеры только как адаптеры к этой бизнес-логике;
- auth-код переносить как часть самой feature, если он действительно нужен после объединения;
- templates и fragments держать внутри feature namespace;
- static resources держать внутри feature namespace;
- не возвращать зависимость от standalone routes и standalone deploy assumptions.

## Keep The Migration Repeatable

Следующий агент должен переносить проект так, чтобы этот же процесс потом можно было повторить для любого нового симулятора:

- одна и та же структура feature-пакета;
- одинаковое разделение на `model`/`auth`/`ui`/`api`;
- минимальные изменения в shared-коде;
- отсутствие уникальных исключений для одного конкретного проекта.

## Replace Instead Of Copying

Эти части не стоит переносить как есть без feature-адаптации:

- `src/main/java/com/example/isib/KirchhoffSecurityConfiguration.java`
  Оставить внутри feature и адаптировать так, чтобы не влиять на другие feature-модули.
- `src/main/java/com/example/isib/auth/FileUserAccountService.java`
  Оставить как feature-specific auth/persistence или заменить локальной реализацией для этой feature.
- `server.servlet.context-path=/kirchhoff` из `src/main/resources/application.properties`
  Убрать и перенести namespacing на уровень controller mappings.
- `app.security.users-file`
  Не оставлять как глобальную настройку приложения; перенести в feature-specific configuration.

## Merge Risks

### Security

- текущая конфигурация объявляет app-wide `SecurityFilterChain`;
- публичные маршруты жёстко привязаны к `"/login"` и `"/register"`;
- success redirect после логина идёт на `"/"`.

При прямом копировании это почти наверняка заденет другие feature-модули, даже если авторизация остаётся отдельной для каждого проекта.

### Routes

- даже если адреса уже были переименованы, feature не должна зависеть от глобального `context-path`;
- UI и API маршруты должны жить в общей схеме маршрутизации целевого приложения;
- login/register flow нужно переносить только как часть auth этой конкретной feature.

### Config

- `spring.application.name=KirchhoffPhysics` подходит только для standalone-режима;
- `server.port=8080` и `server.servlet.context-path=/kirchhoff` должны принадлежать общему приложению, а не feature-модулю.

### Storage

- users file создаётся и обновляется локально на файловой системе;
- сервис записи использует создание директорий, временный файл и атомарный move;
- это неудобно для контейнерного деплоя и должно быть изолировано внутри feature, чтобы не смешиваться с auth-хранилищем других проектов.

## Recommended Migration Order

1. Перенести domain classes и model tests.
2. Подключить controller mappings как адаптеры поверх уже перенесённой business logic, без глобального `context-path`.
3. Перенести templates и static assets в отдельный namespace feature.
4. Адаптировать REST API под общую API-схему приложения, не ломая уже переименованные адреса.
5. Адаптировать auth-логику этой feature так, чтобы она не влияла на другие feature-модули.
6. Переносить login/register flow только если он действительно остаётся нужен после объединения.

Если на каком-то шаге для нового проекта требуется серьёзно переделывать shared-слой, это признак слишком сложной интеграционной схемы.

## What To Verify After Each Step

- model tests проходят;
- feature routes не конфликтуют с другими зонами приложения;
- static assets и templates не перекрывают одноимённые ресурсы других feature-модулей;
- security rules не делают чужие маршруты публичными или недоступными;
- feature-specific storage/auth не смешивается с другими проектами и не зависит от глобального `data/` контракта.

## Suggested First Refactor In Target Repo

Если следующий агент переносит код вручную, первым безопасным шагом будет:

1. создать feature package вроде `...features.kirchhoff`;
2. перенести туда `model`, auth и API/UI адаптеры;
3. убрать зависимость от standalone launcher и standalone properties;
4. уже после этого изолировать auth-правила feature от остальных модулей приложения.

Эта последовательность должна стать шаблоном для всех следующих проектов.
