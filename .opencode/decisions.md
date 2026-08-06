# Decisions

> Log of architectural/design decisions with rationale.

## 2026-07-25 — Dead code cleanup (frontend)
- Removed 5 unused npm packages (`@nivo/calendar`, `@nivo/core`, `@nivo/heatmap`, `baseline-browser-mapping`, `vite-plugin-svgr`)
- Removed 5 dead API functions, dead CSS tokens, commented-out code
- Changed `moodToValue` from exported to internal function
- **Why**: Reduce bundle size (853KB → 695KB), remove dead code risk

## 2026-07-25 — Fix ResendConfig @Value injection
- Changed from constructor injection (`@AllArgsConstructor`) to field injection (`@Autowired` + `@Value`)
- Added `RESEND_TOKEN=test-token` to test properties
- **Why**: `@Value` annotations require field injection when using Lombok `@AllArgsConstructor`

## 2026-07-25 — Fix TokenService @Value injection
- Removed `@AllArgsConstructor`, added explicit constructor with `@Value("${frontend.url}")`
- **Why**: Same `@Value` injection issue; removed `System.getenv()` in favor of Spring config

## 2026-07-25 — Delete WebConfig.java
- Removed dead CORS config that was superseded by SecurityConfig
- **Why**: Duplicate CORS handling caused confusion; SecurityConfig has the authoritative config

## 2026-07-25 — Fix PracticeControllerTest
- Added `.description("A morning run")` to satisfy `@NotBlank` validation
- **Why**: Test was incomplete after `description` field was added to `CreateCheckInRequest`

## 2026-08-01 — Remaining backend-security LOW items
- `@Size` validation: user name ≤100, password 8–72, practice name ≤100 + description ≤1000, category name ≤100, check-in content ≤1000
- **Password policy**: min 8, max 72 chars (bcrypt limit), no complexity rules
  - **Why**: length limits are what actually matter; complexity rules annoy users with little security gain
- **Rate limiting on `/auth/sign_in` + `POST /users`: deferred**
  - **Why**: revisit after the email flow is finished; then per-IP + DB-backed + CAPTCHA at signup
- Security headers applied in **two places** (user chose "Both A + B"):
  - Backend `SecurityConfig`: X-Frame-Options DENY, HSTS, Referrer-Policy STRICT_ORIGIN_WHEN_CROSS_ORIGIN, CSP `default-src 'none'` (JSON API → nothing to load)
  - Frontend `nginx/default.conf.template` (new): CSP `default-src 'self'` + `connect-src` from `API_ORIGIN` env, HSTS, Referrer-Policy, nosniff, frame-options, plus SPA `try_files` fallback
  - **Why**: CSP that protects users belongs on the browser-facing nginx, not the JSON API; also fixed the stock-nginx missing SPA route fallback
- Created `to_discuss.md` — agenda to walk the user through each security/HTTP/attack concept

## 2026-08-05 — Backend architecture cleanup (items 1, 4, 5)
- **Layer violations fixed**:
  - `CheckInEntity.mood` changed from domain `Mood` enum to `String`; `CheckInConverter` maps `Mood ↔ name()`.
    - **Why**: JPA persistence entities must not depend on the domain layer; string keeps the enum source-of-truth in domain.
  - Create-practice chain switched from `CreatePracticeRequest` to scalar params `(categoryId, name, description, userId)`; `PracticeController` unpacks the DTO.
    - **Why**: business layer must not import controller DTOs; scalars are the smallest change (rejected a `CreatePracticeCommand` record to keep it minimal).
  - `CheckInLikeEnricher` moved to `controller.mappers` (with its test).
    - **Why**: it mutates a controller DTO (`CheckInDto`) — presentation-layer concern.
- **Naming**: `I`-prefix on all use-case interfaces (`ICreateUserUseCase`, `IDeleteUserUseCase`, `IGetUserByIdUseCase`, `IUpdateUserUseCase`, `ISendEmailUseCase`); interface `ISendEmailUseCaseImpl` renamed.
  - **Why**: consistent convention — every interface `I…`, every impl `…Impl` (user chose "I-prefix everywhere").
  - Package renames (`usercases`/`practicecases`/`categorycases`/`emailusecases`) **deferred** — many imports, minimal-diff preference.
- **`PracticeConverter.convertToEntity`** builds id-only `CategoryEntity`/`UserEntity` references (null-guarded) instead of full detached copies; `convertToDomain` unchanged (still uses converters).
  - `PracticeRepository.save` / `CheckInRepository.save` back-fill `category`/`creator`/`practice` from the input domain object.
  - **Why**: write path only needs the FK; full detached copies copied sensitive fields (email, password hash) and would create duplicate rows if any cascade were ever added. Back-fill (Approach B) chosen over `getReferenceById` proxies — 2 plain lines, no new deps, no lazy-load/`EntityNotFoundException` semantics. User asked whether this was a real bug: it's latent, not visible today (no cascade configured).

## 2026-08-06 — Backend architecture cleanup (items 2–5)
- **FYP category filtering + own-exclusion moved into SQL** via new `ICheckInRepository.findPublicCheckInsForCategories(List<Long> categoryIds, Long userId)`; JPQL in `CheckInJpaRepository` (`isPublic = true`, `practice.category.id in :categoryIds`, `practice.creator.id <> :userId`); `CheckInRepository` returns `List.of()` when categoryIds is empty (matches prior empty-result, no DB hit).
  - **Why**: fixes the full public-check-in scan; delegate as much to the DB as the query naturally allows. Cold-start `DefaultRecommendationStrategy` intentionally still scans all public (inherent to no-signal users).
- **Domain `@Data` → id-based equals/hashCode** on `User`, `Practice`, `CheckIn`, `Category`, `CheckInLike`: `@Getter @Setter @Builder @NoArgsConstructor @AllArgsConstructor` + `@EqualsAndHashCode(onlyExplicitlyIncluded = true)` + `@EqualsAndHashCode.Include` on `id`.
  - **Why**: mutable `@Data` breaks equals/hashCode when fields change; id-only equals is the standard stable identity. Also removes the `Category` parent/children recursion hazard. Setters kept (streak mutation, updates, back-fill). No behavior change — nothing depends on domain equals/hashCode. `EmailToken` **deferred** (email territory).
- **`findById` → `Optional`** on `IUserRepository`/`IPracticeRepository`/`ICategoryRepository`/`ICheckInRepository`; impls `.map(converter::…)` (CheckIn: `Optional.ofNullable(jpaResult).map(…)`); callers use `orElseThrow(domainException)`, `.isEmpty()`, or `.orElse(null)` for nullable category parents; all tests migrated to `Optional.of(…)`/`Optional.empty()`/`isPresent()`/`isEmpty()`.
  - **Why**: force explicit absent-handling; precedent: `findByCheckInIdAndUserId` already returned Optional. `findByEmail` **left nullable** — it feeds the user's email use cases (`SignInUseCaseImpl`, `UserDetailsServiceImpl`), so changing it would touch email territory (user-owned).
- **`@Transactional(readOnly = true)`** on 6 pure-read use cases: `GetUserByIdUseCaseImpl`, `GetCategoryByIdUseCaseImpl`, `GetCategoryTreeUseCaseImpl`, `GetCheckInsForUserUseCaseImpl`, `GetForYouCheckInsUseCaseImpl`, `SignInUseCaseImpl`.
  - **Not** on `GetPracticesByUserUseCaseImpl` (warning #14): it writes via `StreakValidator` (`repository.save(practice)`); `readOnly=true` would silently drop the streak reset in manual-flush mode. Add it there only together with the user's streak fix.
- **Deferred to user (email + streak)**: `EmailVerificationStrategy` uninitialized Resend, `VerifyTokenUseCaseImpl` stub, `SendEmailUseCaseImpl` stub, `EmailToken` `@Data`, `findByEmail` nullable, streak GET-write redesign. User explicitly wants to write those himself.
- **Noted for later**: FYP pagination; `RecommendationService.findPublicCheckIns` fetches practices via `findByCreatorId` then `PracticeBasedRecommendation` fetches them again (double-fetch); package renames.
