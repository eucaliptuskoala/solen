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
