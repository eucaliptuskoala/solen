# Context

> What's happening now, what changed, what's next.

## Current State
- All 228 backend tests passing, 0 failures
- Frontend build: 695KB (down from 853KB after dead code cleanup)
- Backend architecture: items 2–5 of the 2026-08-06 batch done (FYP SQL query, id-based equals/hashCode, `Optional` findById, `readOnly` reads); remaining: streak GET-write + email items (user-owned) + deferred package renames + 23 frontend items in `.opencode/reviews.md`
- Security concepts to walk through later documented in `to_discuss.md`

## Recent Changes
- Architecture cleanup (2026-08-06): suggestions #3/#4/#5 + warning #14 area:
  - **FYP filtering in SQL**: `ICheckInRepository.findPublicCheckInsForCategories(categoryIds, userId)` + JPQL (`isPublic`, `category.id in :categoryIds`, `creator.id <> :userId`) in `CheckInJpaRepository`; `CheckInRepository` short-circuits `List.of()` on empty categoryIds. `PracticeBasedRecommendation` no longer loads all public check-ins into memory.
  - **id-based equals/hashCode**: `User`, `Practice`, `CheckIn`, `Category`, `CheckInLike` switched from `@Data` to `@Getter @Setter @Builder @NoArgsConstructor @AllArgsConstructor @EqualsAndHashCode(onlyExplicitlyIncluded = true)` + `@EqualsAndHashCode.Include` on `id`. `EmailToken` deferred (email domain).
  - **`findById` → `Optional`** on `IUserRepository`, `IPracticeRepository`, `ICategoryRepository`, `ICheckInRepository` (impls use `.map(converter::…)`); all callers use `orElseThrow(exception)` / `.isEmpty()` / `.orElse(null)` (category parents). `findByEmail` intentionally left nullable (email territory). All tests migrated (`thenReturn(Optional.of(…))` / `Optional.empty()`, integration tests `isPresent()`/`isEmpty()`).
  - **`@Transactional(readOnly = true)`** on 6 pure-read use cases: `GetUserByIdUseCaseImpl`, `GetCategoryByIdUseCaseImpl`, `GetCategoryTreeUseCaseImpl`, `GetCheckInsForUserUseCaseImpl`, `GetForYouCheckInsUseCaseImpl`, `SignInUseCaseImpl`. **Not** on `GetPracticesByUserUseCaseImpl` — it writes via `StreakValidator.save()`; `readOnly` would silently drop the streak reset.
- Architecture cleanup (2026-08-05): layer violations + naming + converter fixed:
  - `CheckInEntity.mood` `domain.Mood` → `String` (mapped in `CheckInConverter`) — persistence no longer imports domain
  - Create-practice chain now takes scalar params `(Long categoryId, String name, String description, Long userId)` from `PracticeController` through `ICreatePracticeUseCase` → `CreatePracticeUseCaseImpl` → `PracticeCreationStrategyService` → both strategies (no controller DTO in business layer)
  - `CheckInLikeEnricher` moved `business/checkin` → `controller/mappers` (with its test) — no business-layer DTO mutation
  - Naming: `I`-prefix on all use-case interfaces (`ICreateUserUseCase`, `IDeleteUserUseCase`, `IGetUserByIdUseCase`, `IUpdateUserUseCase`, `ISendEmailUseCase`); interface `ISendEmailUseCaseImpl` misnomer fixed; package renames (`usercases`/`practicecases`/etc.) deferred
  - `PracticeConverter.convertToEntity` builds id-only category/creator refs (no detached full-copy risk); `PracticeRepository.save`/`CheckInRepository.save` back-fill category/creator/practice from input domain so responses stay populated
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
- `GetPracticesByUserUseCaseImpl` read-with-writes (streak validation `save()` inside GET) — **user-owned**; after the fix, add `@Transactional(readOnly = true)` there
- Email items — **user-owned** until email features are finalized: `EmailVerificationStrategy` uninitialized `Resend` field (NPE), `VerifyTokenUseCaseImpl` stub (`return true`), `SendEmailUseCaseImpl` stub, `EmailToken` `@Data` + `findByEmail` nullable
- FYP pagination; `RecommendationService.findPublicCheckIns` double-fetch of `findByCreatorId`
- Deferred package rename: `usercases`/`practicecases`/`categorycases`/`emailusecases` → one consistent scheme
- Frontend: wrap auth state in React context for reactivity
- Frontend: replace `console.error` with user-facing toasts
- When email flow is finished: upgrade rate limiting to per-IP + DB-backed, add CAPTCHA at signup, trigger verification email server-side at signup
