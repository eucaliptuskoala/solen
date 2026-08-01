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
