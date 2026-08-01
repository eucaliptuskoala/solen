# Context

> What's happening now, what changed, what's next.

## Current State
- All 225 backend tests passing, 0 failures
- Frontend build: 695KB (down from 853KB after dead code cleanup)
- All backend security issues fixed or deferred; ~33 issues remaining in `.opencode/reviews.md` (architecture + frontend)
- Security concepts to walk through later documented in `to_discuss.md`

## Recent Changes
- Fixed 4 failing backend tests (WebConfig, ResendConfig, TokenService, PracticeControllerTest)
- Removed dead frontend packages, APIs, CSS, and variables
- Completed full project review (backend security, backend architecture, frontend)
- Added `@PreAuthorize` to all UserController endpoints (PUT/DELETE = owner or admin, GET = admin)
- Removed `isAdmin` from `CreateUserRequest`, `UpdateUserRequest`, `UserDto` (mass assignment fix)
- Hardcoded `isAdmin(false)` in `CreateUserUseCaseImpl`
- Created `AdminBootstrapRunner` — auto-creates admin on startup via `ADMIN_PASSWORD` env var
- Created `PATCH /users/{id}/role` endpoint (admin-only) for promoting users
- Added `@Transactional` to 12 write use cases
- Created `.opencode/` project memory directory with context, architecture, decisions, and reviews
- JWT: 2h token lifetime, secret must be ≥256 bits, deleted users get 401 (not 500)
- Check-in creation IDOR fixed — `createWithDetails`/`create` take `userId`, verify practice ownership (403)
- `GlobalExceptionHandler`: catch-all `Exception` → generic 500 + logs; new `ForbiddenAccessException`→403 and `UnauthorizedAccessException`→401
- Email endpoint: unknown user + per-address cooldown (`EmailRateLimiter`, 60s default) both silently skip send (no enumeration, no bombing); `EmailController` resolves target email
- `UserInfoProvider`: null-check on `Authentication` → 401
- `spring.jpa.show-sql=false` in production config
- `@Size` validation: user name ≤100, password 8–72, practice name ≤100 + description ≤1000, category name ≤100, check-in content ≤1000
- Security headers: backend `SecurityConfig` (X-Frame-Options DENY, HSTS, Referrer-Policy, CSP `default-src 'none'`); frontend `nginx/default.conf.template` (CSP `default-src 'self'` + `connect-src` from `API_ORIGIN`, HSTS, Referrer-Policy, nosniff, frame-options, SPA fallback)
- Created `to_discuss.md` — agenda for reviewing all security/HTTP/attack concepts
- Rate limiting on `/auth/sign_in` + `POST /users` — **deferred** by decision (revisit after email flow)

## Next Steps
- Fix `EmailVerificationStrategy` uninitialized `Resend` field (NPE at runtime) — highest priority remaining
- Move controller DTOs out of business layer (e.g., `ICreatePracticeUseCase` imports `CreatePracticeRequest`)
- Frontend: wrap auth state in React context for reactivity
- Frontend: replace `console.error` with user-facing toasts
- When email flow is finished: upgrade rate limiting to per-IP + DB-backed, add CAPTCHA at signup, trigger verification email server-side at signup
