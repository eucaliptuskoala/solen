# Context

> What's happening now, what changed, what's next.

## Current State
- All 215 backend tests passing, 0 failures
- Frontend build: 695KB (down from 853KB after dead code cleanup)
- 65 issues documented in `.opencode/reviews.md` (8 critical, 8 high, 17 medium, 30 low)

## Recent Changes
- Fixed 4 failing backend tests (WebConfig, ResendConfig, TokenService, PracticeControllerTest)
- Removed dead frontend packages, APIs, CSS, and variables
- Completed full project review (backend security, backend architecture, frontend)

## Next Steps
- **P0**: Fix UserController auth gaps + DTO mass assignment
- **P0**: Fix EmailVerificationStrategy uninitialized Resend field
- **P1**: Add `@Transactional` to write use cases
- **P1**: Fix CreateUserUseCaseImpl isAdmin bypass
- **P2**: Move controller DTOs out of business layer
- **P2**: Frontend auth state reactivity + error handling
