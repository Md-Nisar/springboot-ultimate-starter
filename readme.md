# 🌟 Spring Boot Ultimate Starter

A **multi-module**, scalable, enterprise-ready Spring Boot project template with everything you need to build real-world, production-grade backend systems.

---

## 📦 Modules

| Module            | Description |
|-------------------|-------------|
| `core`            | Shared DTOs, enums, constants, utils, and global exceptions |
| `config`          | Centralized app configuration (Swagger, CORS, Security)     |
| `data-access`     | Base JPA entities, repositories, audit fields, naming config |
| `auth`            | JWT authentication, token refresh, roles, security filter   |
| `user`            | User registration, roles, profile, verification             |
| `employee`        | Example business module (CRUD + service + repository)       |
| `mail-service`    | Email service using SMTP + Thymeleaf templates              |
| `scheduler`       | Background cron jobs and task scheduling                    |
| `integration`     | Redis, Kafka, RabbitMQ, Feign/WebClient                     |
| `payment`         | (Planned) Stripe, PayMaya integration                       |
| `web-api`         | Main entry point exposing REST endpoints                    |

---

## 🔐 Features

- JWT Authentication (access + refresh token)
- Role-based authorization (RBAC)
- Centralized exception handling with `@ControllerAdvice`
- DTO-based request/response models
- Swagger/OpenAPI 3 docs
- Email sending (SMTP + Thymeleaf)
- Scheduled jobs
- Redis Pub/Sub ready
- Kafka, RabbitMQ placeholder support
- Clean multi-module Maven structure
- Audit fields (`createdAt`, `updatedAt`) with JPA
- Dynamic environment config via YAML profiles
- Docker & GitHub CI/CD ready (optional)

---

## 📁 Feature Module Pattern

Each business feature follows this structure:



