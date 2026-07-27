# Architecture

> How it's built, patterns, structure.

## Stack
- **Backend**: Java 17, Spring Boot 3.5, Gradle, PostgreSQL, Flyway, H2 (test)
- **Frontend**: React 19, Vite 7, Tailwind CSS 4, Vitest 4
- **Auth**: JWT (jjwt 0.11.5), Spring Security
- **Email**: Resend (resend-java 3.1.0)

## Backend Structure (Clean Architecture)
```
org.solen
├── controller/          # REST controllers, DTOs
├── business/            # Use cases (one per operation), interfaces
│   ├── signinusecases/
│   ├── checkinusecases/
│   ├── practiceusecases/
│   ├── userusecases/
│   ├── fypusecases/
│   ├── emailusecases/
│   └── categoryusecases/
├── persistence/         # JPA repositories, entities
├── domain/              # Domain objects (Mood, Streak, CheckIn, etc.)
├── configuration/       # Security, CORS, Resend config
└── shared/              # Utils, exceptions
```

## Frontend Structure
```
frontend-web/src/
├── apis/                # Axios clients (one per domain)
├── pages/               # Route-level components
├── components/          # Shared UI components
├── context/             # AuthContext (existing but underused)
├── hooks/               # useTheme, useProgress, etc.
└── utils/               # AuthHandler, helpers
```

## Patterns
- **Strategy pattern**: Habit creation, FYP recommendations, email sending
- **Convention-based routing**: Backend controllers, React Router
- **Test DB**: H2 in-memory, separate Flyway migrations in `db/migration_test/`
