# Context

> What's happening now, what changed, what's next.

## Current State
- All 217 backend tests passing, 0 failures
- Frontend build: 695KB (down from 853KB after dead code cleanup)
- 57 issues remaining in `.opencode/reviews.md`

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

## Next Steps
- Move controller DTOs out of business layer (e.g., `ICreatePracticeUseCase` imports `CreatePracticeRequest`)
- Frontend: wrap auth state in React context for reactivity
- Frontend: replace `console.error` with user-facing toasts
- Fix `EmailVerificationStrategy` uninitialized `Resend` field
