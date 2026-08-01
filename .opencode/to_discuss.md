# To Discuss — Security & HTTP Concepts

> The points below were fixed/decided during the security hardening pass. I wanted to
> understand each one before we moved on, but didn't want to block the work.
> This file is the agenda for a future walkthrough — one topic at a time, plain language.
>
> Last updated: 2026-08-01

---

## 1. Authentication & Authorization (who you are vs. what you may do)

- [ ] **JWT / stateless auth** — what a token actually is, why no server session, HS256 vs other algos, what the 2h expiry buys us.
      Files: `JwtUtil.java`, `JwtAuthFilter.java`
- [ ] **Secret strength (≥256 bits / 32 bytes)** — why the signing key length matters and what happens if it's short.
- [ ] **`@PreAuthorize` + owner-or-admin pattern** — how method-level checks work, and why `PUT/DELETE /users/{id}` need them.
      File: `UserController.java`
- [ ] **Mass assignment** — how sending extra fields like `isAdmin` in a request body could escalate privileges, and how we removed it.
      Files: `CreateUserRequest.java`, `CreateUserUseCaseImpl.java`
- [ ] **IDOR (Insecure Direct Object Reference)** — passing another user's `practiceId` when creating a check-in; ownership check → 403.
      File: `CreateCheckInUseCaseImpl.java`
- [ ] **User enumeration** — why `GET /users` is admin-only and why unknown email → silent skip, not an error.

## 2. Passwords & crypto

- [ ] **bcrypt via delegating encoder** — one-way hashing, the 72-byte input limit (our max=72), and why we never store plaintext.
      File: `SecurityConfig.java` (`passwordEncoder()`)
- [ ] **Password policy decision** — min 8 / max 72, no complexity rules (deliberate: don't annoy users without a real benefit).

## 3. HTTP / browser behavior

- [ ] **HTTP status codes** — 401 (unauthenticated) vs 403 (forbidden) vs 500 (server bug), and how the error handler maps them.
      File: `GlobalExceptionHandler.java`
- [ ] **CORS** — why the API must explicitly allow the frontend origin, what preflight `OPTIONS` is.
      File: `SecurityConfig.java` (`corsConfigurationSource()`)
- [ ] **CSRF & why we disable it** — session vs token auth, when CSRF protection matters and when it's noise.
- [ ] **Security response headers** — how each one changes browser behavior:
  - [ ] `X-Frame-Options` / clickjacking
  - [ ] `X-Content-Type-Options: nosniff` / MIME sniffing
  - [ ] `Strict-Transport-Security` (HSTS) / SSL stripping
  - [ ] `Referrer-Policy` / leaking our token-in-URL via the `Referer` header
  - [ ] `Content-Security-Policy` (CSP) / what it blocks and why the JSON API uses `default-src 'none'` while the SPA uses a full policy
  - Files: `SecurityConfig.java` (backend), `frontend-web/nginx/default.conf.template` (frontend)
- [ ] **Validation (`@Size`, `@NotBlank`, `@Email`)** — where request bodies are validated and how bad input becomes a 400.

## 4. Attacks we're defending against (map to #1–3)

- [ ] XSS — scripts injected into the app; CSP + escaping as layers
- [ ] Clickjacking — framing our pages invisibly; `X-Frame-Options: DENY`
- [ ] MIME sniffing — serving text that the browser executes as JS; `nosniff`
- [ ] SSL stripping — MITM downgrade to plain HTTP; HSTS
- [ ] IDOR — direct object reference guessing (see #1)
- [ ] Mass assignment / privilege escalation (see #1)
- [ ] Email enumeration + email bombing — timing/cooldown limits
- [ ] SQL injection — why Hibernate/JPA parameters prevent it (and what `show-sql` logging could leak)

## 5. Deferred on purpose (decision to revisit)

- [ ] Rate limiting on `/auth/sign_in` and `POST /users` — deferred until the email flow is finished, then per-IP + DB-backed.
- [ ] CAPTCHA at signup.
- [ ] Email verification flow — currently non-functional end-to-end (`EmailVerificationStrategy` NPE, pre-auth `POST /emails`).

---

### Suggested order
Start with **1 (auth/authz)** → **2 (passwords)** → **3 (HTTP/browser)** → **4 (attacks)** —
each section's file references make them easy to look at side by side during the walkthrough.
