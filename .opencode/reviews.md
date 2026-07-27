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

- No rate limiting on `/auth/sign_in` and `/users`
- No `@Size` constraints on `content`, `name`, `description`
- No password strength validation
- `isAdmin` field leaked in `UserDto` response
- No security response headers (`X-Content-Type-Options`, `X-Frame-Options`, etc.)

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
| 5 | `CheckInEntity.java` | 8 | Layer violation — JPA entity imports `domain.checkin.Mood` enum directly |
| 6 | `PracticeEntity.java` | — | Layer violation — imports `domain.Streak` |
| 7 | `EmailToken.java` | — | Layer violation — imports `domain.email.TokenFlag` |
| 8 | `ICreatePracticeUseCase.java` | 3 | Business layer imports `controller.dto.practice.CreatePracticeRequest` |
| 9 | `EmailVerificationStrategy.java` | 13 | Uninitialized `private Resend resend;` — no constructor injection, NPE at runtime |
| 10 | `VerifyTokenUseCaseImpl.java` | 14 | Stub that always returns `true` — dead code |
| 11 | `EmailController.java` | 25 | Null check logic inverted — passes null email to execute |
| 12 | Naming | — | Inconsistent: `ISendEmailUseCaseImpl` (has Impl in interface name), `CreateUserUseCase` (no I prefix), `usercases` vs `practicecases` |
| 13 | `PracticeConverter.java` | 23 | Creates new entity objects for nested relations — JPA merge will create duplicates |
| 14 | `GetPracticesByUserUseCaseImpl.java` | 20 | Read use case has write side effects (streak validation saves) |

### SUGGESTION

- No `@Transactional(readOnly = true)` on read use cases
- Domain objects use `@Data` (mutable equals/hashCode)
- `CheckInLikeEnricher` in business layer mutates controller DTOs
- `PracticeBasedRecommendation` loads all public check-ins into memory
- Repositories return nullable instead of `Optional`

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
| **P0** | UserController auth gaps + DTO mass assignment | Small |
| **P0** | EmailVerificationStrategy uninitialized Resend | Small |
| **P1** | Add `@Transactional` to write use cases | Medium |
| **P1** | Fix `CreateUserUseCaseImpl` isAdmin bypass | Small |
| **P2** | Move controller DTOs out of business layer | Medium |
| **P2** | Frontend: wrap auth state in React context | Medium |
| **P2** | Frontend: add proper error handling with toasts | Medium |

---

*Last updated: 2026-07-25*
