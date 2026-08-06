# Code Reviews — Solen

> Generated from review session. Reviewers: backend security, backend architecture, frontend (3 agents).

---

## Review Summary

| Area | Critical | High | Medium | Low | Total |
|------|----------|------|--------|-----|-------|
| Backend Security | 4 | 4 | 4 | 10 | 22 |
| Backend Architecture | 4 | 0 | 6 | 8 | 18 |
| Frontend | 0 | 4 | 7 | 12 | 23 |
| **Total** | **8** | **8** | **17** | **30** | **63** |

> **Fixed (2026-08-01):** BS-1..BS-12 plus all LOW backend-security items — auth, mass assignment,
> JWT 256-bit + 2h expiry, deleted-user 401, check-in IDOR → 403, exception-handler catch-all,
> email enumeration + rate limit, UserInfoProvider NPE → 401, show-sql=false, `@Size` DTO validation,
> password 8–72 policy, `isAdmin` removed from `UserDto`, security headers (backend `SecurityConfig`
> + frontend `nginx/default.conf.template` with CSP/HSTS/Referrer-Policy/nosniff/frame-options).
> **Fixed (2026-08-05):** architecture items 5 (CheckInEntity imports `domain.Mood` → `String` + converter),
> 8 (controller DTO out of business layer → scalar params), 12 (naming: `I`-prefix on all use-case
> interfaces, `ISendEmailUseCaseImpl` → `ISendEmailUseCase`), 13 (`PracticeConverter` id-only nested refs,
> `save()` back-fill); `CheckInLikeEnricher` moved to `controller.mappers`. Items 6, 7, 11 verified stale
> (no `domain.Streak`/`TokenFlag` imports; `EmailController` null-check correct).
> **Fixed (2026-08-06):** architecture suggestions #3 (`readOnly` on reads), #4 (`@Data` → id-based
> equals/hashCode), #5 (repos → `Optional`) and the FYP in-memory filtering → SQL JPQL (see WARNING #15).
> Warning #14 (streak GET-write) and warnings #9/#10 + `EmailToken` (email) are **user-owned** — deferred
> until the user finalizes email features and the streak fix themselves.
> **Deferred (documented in `to_discuss.md`):** rate limiting on `/auth/sign_in` + `POST /users`.
> Remaining: frontend (23 items) + user-owned backend items.

---

## Backend — Security

### CRITICAL

| # | File | Line | Finding |
|---|------|------|---------|
| 1 | `UserController.java` | 56 | No `@PreAuthorize` on `PUT /users/{id}` — any authenticated user can modify any other user |
| 2 | `UserController.java` | 38 | No `@PreAuthorize` on `DELETE /users/{id}` — any authenticated user can delete any user |
| 3 | `CreateUserRequest.java` | 26 | `isAdmin` field accepted on unauthenticated `POST /users` — mass assignment allows anyone to register as admin |
| 4 | `UserController.java` | 44 | `GET /users` returns all user emails to any authenticated user — user enumeration |

### HIGH

| # | File | Line | Finding |
|---|------|------|---------|
| 5 | `JwtUtil.java` | 17 | No validation that JWT secret is >= 256 bits |
| 6 | `JwtUtil.java` | 27 | 24h token lifetime with no refresh or revocation mechanism |
| 7 | `JwtAuthFilter.java` | 34 | Deleted user causes 500 error instead of 401 |
| 8 | `CheckInController.java` | 54 | Owner not explicitly passed to `createWithDetails` — verify intent |

### MEDIUM

| # | File | Line | Finding |
|---|------|------|---------|
| 9 | `GlobalExceptionHandler.java` | — | Exception messages may leak internal details |
| 10 | `EmailController.java` | 30 | Can send emails to arbitrary addresses — email bombing risk |
| 11 | `UserInfoProvider.java` | 17 | NPE if authentication is null |
| 12 | `application.properties` | 6 | `show-sql=true` in production config — leaks SQL in logs |

### LOW

- No rate limiting on `/auth/sign_in` and `/users` — **deferred** (see `to_discuss.md`; revisit after email flow)
- No `@Size` constraints on `content`, `name`, `description` — ✅ (names ≤100, description/content ≤1000, password 8–72)
- No password strength validation — ✅ (min 8 / max 72, deliberately no complexity rules)
- `isAdmin` field leaked in `UserDto` response — ✅ (removed from DTOs)
- No security response headers — ✅ backend `SecurityConfig` (X-Frame-Options DENY, HSTS, Referrer-Policy STRICT_ORIGIN_WHEN_CROSS_ORIGIN, CSP `default-src 'none'`) + frontend `nginx/default.conf.template` (CSP `default-src 'self'`, HSTS, Referrer-Policy, nosniff, frame-options; SPA fallback)

---

## Backend — Architecture

### CRITICAL

| # | File | Line | Finding |
|---|------|------|---------|
| 1 | `CreateCheckInUseCaseImpl.java` | — | Missing `@Transactional` — race conditions on concurrent check-in writes |
| 2 | `DeletePracticeUseCaseImpl.java` | — | Missing `@Transactional` — race conditions on concurrent practice deletes |
| 3 | `DeleteCheckInUseCaseImpl.java` | — | Missing `@Transactional` — race conditions on concurrent check-in deletes |
| 4 | `CreateUserUseCaseImpl.java` | — | Missing `@Transactional` — race conditions on concurrent user creation |

### WARNING

| # | File | Line | Finding |
|---|------|------|---------|
| 5 | `CheckInEntity.java` | 8 | Layer violation — JPA entity imports `domain.checkin.Mood` enum directly — ✅ (`mood` → `String`, mapped in `CheckInConverter`) |
| 6 | `PracticeEntity.java` | — | ~~Layer violation — imports `domain.Streak`~~ — stale, no such import |
| 7 | `EmailToken.java` | — | ~~Layer violation — imports `domain.email.TokenFlag`~~ — stale, no such import |
| 8 | `ICreatePracticeUseCase.java` | 3 | Business layer imports `controller.dto.practice.CreatePracticeRequest` — ✅ (scalar params `categoryId/name/description/userId` through the whole chain) |
| 9 | `EmailVerificationStrategy.java` | 13 | Uninitialized `private Resend resend;` — no constructor injection, NPE at runtime |
| 10 | `VerifyTokenUseCaseImpl.java` | 14 | Stub that always returns `true` — dead code |
| 11 | `EmailController.java` | 25 | ~~Null check logic inverted~~ — stale, `request.getEmail() != null ? … : userInfoProvider.getUserEmail()` |
| 12 | Naming | — | Inconsistent: `ISendEmailUseCaseImpl` (has Impl in interface name), `CreateUserUseCase` (no I prefix), `usercases` vs `practicecases` — ✅ interface names fixed (`I`-prefix everywhere); package renames deferred |
| 13 | `PracticeConverter.java` | 23 | Creates new entity objects for nested relations — JPA merge will create duplicates — ✅ (id-only refs + `save()` back-fill) |
| 14 | `GetPracticesByUserUseCaseImpl.java` | 20 | Read use case has write side effects (streak validation saves) — ⏳ **user-owned** (streak fix); add `@Transactional(readOnly=true)` once fixed |
| 15 | `PracticeBasedRecommendation.java` | — | Loads all public check-ins into memory — ✅ (`findPublicCheckInsForCategories` JPQL filters by category + excludes own in SQL) |

### SUGGESTION

- No `@Transactional(readOnly = true)` on read use cases — ✅ (6 pure-read use cases; NOT `GetPracticesByUserUseCaseImpl` — writes via `StreakValidator.save()`)
- Domain objects use `@Data` (mutable equals/hashCode) — ✅ (`User`, `Practice`, `CheckIn`, `Category`, `CheckInLike` → id-based `@EqualsAndHashCode`; `EmailToken` ⏳ user-owned)
- `CheckInLikeEnricher` in business layer mutates controller DTOs — ✅ (moved to `controller.mappers`)
- `PracticeBasedRecommendation` loads all public check-ins into memory — ✅ (SQL, see WARNING #15)
- Repositories return nullable instead of `Optional` — ✅ (`findById` → `Optional` on all 4 repos; `findByEmail` left nullable — email territory)

---

## Frontend

### HIGH

| # | File | Line | Finding |
|---|------|------|---------|
| 1 | `App.jsx` | 14 | Auth state not reactive — `tokenExists()` at render time, won't update on login/logout without remount |
| 2 | `ProgressPage.jsx` | 40–51 | `mockData` used as catch fallback — API failure silently shows fake data with no user indication |
| 3 | `CheckInsPage.jsx` | 56–60 | `handleNewCheckIn` has no try/catch — errors propagate unhandled, `setShowNew(false)` / `showToast` skipped |
| 4 | `SignUpPage.jsx` | 49–53 | `Promise.all` swallows practice creation errors silently — user sees no feedback |

### MEDIUM

| # | File | Line | Finding |
|---|------|------|---------|
| 5–8 | `DashboardPage.jsx` | 39,82,94 | `console.error` used as sole error handling (10+ instances across pages) — no user-facing toasts |
| 9 | `CheckInsPage.jsx` | 48 | `window.confirm` blocks main thread — should use `DeleteConfirmationDialog` modal |
| 10 | `ProgressPage.jsx` | 79–88 | `applyCustomRange` triggers double fetch (useEffect + direct call) |
| 11 | `NavBar.jsx` | 58–60 | Auth state not reactive — won't update if token cleared in another tab |
| 12 | `CheckInsPage.jsx` | 20,26,44,53 | 4 instances of `console.error` with no user feedback |
| 13 | `InspirePage.jsx` | 14 | Same console.error pattern |
| 14 | `DashboardPage.jsx` | 14–19 | `getGreeting()` called at render time — won't update if component stays mounted past midnight |

### LOW

- Missing `aria-label` / `aria-expanded` on interactive elements (NavBar hamburger, Progress toggle)
- No env var validation (`VITE_BASE_API_URL` silently becomes `"undefined"`)
- `hidden` via CSS class leaves DOM mounted when steps change
- "Forgot password?" is `<a href="#">` instead of `<button>`
- `CheckInAPI.delete` uses reserved word `delete` as property name
- Hardcoded English strings — no i18n support
- SVG icons at module scope won't re-render for dynamic theming

---

## Fix Priority

| Priority | Scope | Effort |
|----------|-------|--------|
| **P0** | ~~UserController auth gaps + DTO mass assignment~~ ✅ | Small |
| **P0** | EmailVerificationStrategy uninitialized Resend | Small |
| **P1** | ~~Add `@Transactional` to write use cases~~ ✅ | Medium |
| **P1** | ~~Fix `CreateUserUseCaseImpl` isAdmin bypass~~ ✅ | Small |
| **P1** | ~~Backend security medium/high: check-in IDOR, exception leak, email bombing, NPE, show-sql~~ ✅ | Medium |
| **P2** | ~~Security headers: backend `SecurityConfig` + frontend `nginx/default.conf.template`~~ ✅ | Small |
| **P2** | ~~Move controller DTOs out of business layer (create-practice chain → scalar params; `CheckInLikeEnricher` → `controller.mappers`)~~ ✅ | Medium |
| **P2** | ~~Naming: `I`-prefix all use-case interfaces, fix `ISendEmailUseCaseImpl`~~ ✅ | Small |
| **P2** | ~~`PracticeConverter` id-only nested refs + `save()` back-fill~~ ✅ | Small |
| **P2** | ~~FYP filtering in SQL (no full public-check-in scan)~~ ✅ | Medium |
| **P2** | ~~`@Data` → id-based equals/hashCode (User, Practice, CheckIn, Category, CheckInLike)~~ ✅ | Small |
| **P2** | ~~`findById` → `Optional` on all repos + caller migration~~ ✅ | Medium |
| **P2** | ~~`@Transactional(readOnly = true)` on 6 pure-read use cases~~ ✅ | Small |
| **P2** | Frontend: wrap auth state in React context | Medium |
| **P2** | Frontend: add proper error handling with toasts | Medium |

---

*Last updated: 2026-08-06*
