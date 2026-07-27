# Solen — Frontend Architecture

> Mermaid diagrams documenting the React SPA architecture.

---

## 1. Component Hierarchy — React Component Tree

```mermaid
graph TD
    subgraph "Entry"
        M["main.jsx<br/>StrictMode > BrowserRouter"] --> A["App.jsx<br/>NavBar + Routes"]
    end

    subgraph "Routing"
        A --> NB["NavBar<br/>uses: useLocation, useNavigate, AuthHandler<br/>includes: ThemeToggle (dark/light)"]
        A --> R0["Route / → LandingPage"]
        A --> R1["Route /dashboard → DashboardPage"]
        A --> R2["Route /checkins → CheckInsPage"]
        A --> R3["Route /sign-up → SignUpPage"]
        A --> R4["Route /sign-in → SignInPage"]
        A --> R5["Route /inspire → InspirePage"]
        A --> R6["Route /progress → ProgressPage"]
    end

    subgraph "LandingPage"
        LP["LandingPage"] --> SI["SunIllustration<br/>(decorative animated CSS)"]
        LP --> CTB["CategoryTreeBrowser<br/>props: { onSelect }<br/>state: tree, leafNames, selectedIds"]
        CTB -->|"on mount"| CATAPI["CategoryAPI.getTree()"]
        CTB --> TV["TreeView"]
    end

    subgraph "SignInPage"
        SIP["SignInPage<br/>state: email, password, error<br/>handleSignIn → AuthAPI.signIn"] --> AAPI["AuthAPI.signIn()"]
        SIP --> Input
        SIP --> Label
        SIP --> Button
        SIP --> AS["AuthSun"]
    end

    subgraph "SignUpPage"
        SUP["SignUpPage<br/>state: step(1|2), name, email, password"] -->|"Step 1"| UAPI["UserAPI.createUser()"]
        SUP -->|"Step 1"| AAPI2["AuthAPI.signIn()"]
        SUP -->|"Step 2"| CTB2["CategoryTreeBrowser<br/>onSelect → PracticeAPI.createPractice()"]
        SUP --> AS2["AuthSun"]
    end

    subgraph "DashboardPage"
        DP["DashboardPage<br/>state: practices[], toast, showCreateModal<br/>       newName, newDesc, newCategoryId<br/>       deleteTarget, popupPractice"] --> PH["PageHeader<br/>props: eyebrow, title"]
        DP --> DCF["DailyCheckInForm<br/>props: { practices, onSave }<br/>state: hidden, mood, reflection, selectedPracticeId, isPublic"]
        DP --> PCL["PracticeCardList<br/>props: { practices, onDone, onDelete }"]
        PCL --> PC["PracticeCard<br/>props: { practice{id,name,categoryName,streak,checkedInToday}, onDone, onDelete }"]
        DP --> CPM["CreatePracticeModal<br/>props: { isOpen, onClose, onCreate, categoryId, setCategoryId, name, setName, description, setDescription }"]
        CPM --> TV2["TreeView<br/>props: { nodes, onSelect }"]
        DP --> CP["CheckInPopup<br/>props: { isOpen, practiceId, practiceName, onSave, onClose }<br/>state: mood, content, isPublic, saving, success"]
        DP --> DCD["DeleteConfirmationDialog<br/>props: { isOpen, practiceName, onCancel, onConfirm }"]
        DP --> Toast["Toast notification<br/>(from useToast hook, 2.5s auto-dismiss)"]
    end

    subgraph "CheckInsPage"
        CIP["CheckInsPage<br/>state: entries[], practices[], editEntry<br/>       showEdit, showNew, toast"] --> CT["CheckInTimeline<br/>props: { entries, onEdit, onDelete, onCreate }<br/>state: activeFilter('all'|'this-week'|'this-month')"]
        CT --> CTE["CheckInTimelineEntry<br/>props: { entry{id,date,createdAt,mood,practice,categoryName,name,content,isPublic,likeCount,isLikedByCurrentUser}, onEdit, onDelete }"]
        CTE --> LB1["LikeButton<br/>(inline on each entry)"]
        CIP --> CE["EditCheckInModal<br/>props: { isOpen, entry, onSave, onClose }<br/>state: content, mood, isPublic"]
        CIP --> CP2["CheckInPopup<br/>props: { isOpen, onSave, onClose, availablePractices }"]
    end

    subgraph "InspirePage"
        IP["InspirePage<br/>state: entries[], view('feed'|'card')"] --> ICard["InspireCard<br/>props: { entry{user:{name}, practice:{categoryName}, mood, content, date, likeCount, isLikedByCurrentUser} }"]
        ICard --> LB2["LikeButton<br/>(heart icon with count)"]
        IP --> INav["InspireNavigator<br/>props: { entries[] }<br/>state: index"]
    end

    subgraph "ProgressPage"
        PP["ProgressPage<br/>state: progressPerPractice[], contribution[]<br/>       startDate, endDate, activePreset, customOpen"] --> PLC["PracticeLineChart<br/>props: { data{date,streakValue,moodValue}[], metric('streak'|'mood') }<br/>uses recharts LineChart"]
        PP --> UAC["UserActivityCalendar<br/>props: { data{day, value}[] }<br/>(custom SVG calendar)"]
    end

    subgraph "UI Primitives"
        Button["Button<br/>props: { variant('primary'|'secondary'|'ghost'|'danger'), size('md'|'sm'), disabled, onClick }"]
        Modal["Modal<br/>props: { isOpen, onClose, children, maxWidth }<br/>closes on Escape + backdrop click"]
        Card["Card<br/>props: { className, children }"]
        Input["Input<br/>props: { size('md'|'sm'), className }"]
        Label["Label<br/>props: { htmlFor, children }"]
        Textarea["Textarea<br/>props: { size('md'|'sm'), className }"]
        ToggleSwitch["ToggleSwitch<br/>props: { checked, onChange, id, label }<br/>role='switch'"]
        Badge["Badge<br/>props: { children }<br/>rounded pill"]
        TreeView["TreeView<br/>props: { nodes, selectedIds, onToggle, onSelect, defaultOpenDepth, showIcons }<br/>recursive TreeNode"]
        PageHeader["PageHeader<br/>props: { eyebrow, title, className }"]
        MoodIcon["MoodIcon<br/>props: { mood('AWFUL'|'BAD'|'OKAY'|'GOOD'|'AWESOME'), size }<br/>inline SVG faces"]
        AuthSun["AuthSun<br/>props: { size, className }<br/>decorative sun + orbit"]
        LikeButtonUI["LikeButton<br/>props: { checkInId, isLiked, likeCount, onToggle }<br/>heart icon + count, optimistic update"]
    end

    subgraph "Navigation"
        NavBarUI["NavBar<br/>includes: ThemeToggle<br/>uses: useLocation, useNavigate, AuthHandler"]
        ThemeToggleUI["ThemeToggle<br/>uses: useTheme hook<br/>sun/moon icon toggle"]
    end
```

---

## 2. API & Data Flow Layer

```mermaid
graph LR
    subgraph "API Client"
        AC["AxiosConfig<br/>axios.create()<br/>interceptor: reads AuthHandler.getToken()<br/>→ Authorization: Bearer <token>"]
        AH["AuthHandler (localStorage)<br/>saveToken(token)<br/>getToken()<br/>clearToken()<br/>getUserId() → jwtDecode().userId<br/>getName() → jwtDecode().name<br/>tokenExists()"]
    end

    subgraph "API Modules"
        AAPI["AuthAPI<br/>signIn(req) → POST /auth/sign_in<br/>Uses raw axios (no auth)"]
        UAPI["UserAPI<br/>createUser(user) → POST /users<br/>getAllUsers() → GET /users<br/>getUserById(id) → GET /users/{id}<br/>deleteUser(id) → DELETE /users/{id}<br/>Uses raw axios (no auth)"]
        PAPI["PracticeAPI<br/>createPractice(p) → POST /practices<br/>getPracticesByUser() → GET /practices/my<br/>updateStreak(id) → PUT /practices/{id}<br/>deletePractice(id) → DELETE /practices/{id}"]
        CIAPI["CheckInAPI<br/>getAll(from?,to?) → GET /checkins?from=&to=<br/>create(req) → POST /checkins/checkin<br/>update(id,req) → PUT /checkins/{id}<br/>delete(id) → DELETE /checkins/{id}<br/>getFyp() → GET /checkins/fyp<br/>toggleLike(id) → POST /checkins/{id}/like"]
        CATAPI["CategoryAPI<br/>getTree() → GET /categories<br/>getById(id) → GET /categories/{id}"]
    end

    subgraph "Theme"
        UT["useTheme hook<br/>- theme: 'light' | 'dark'<br/>- toggleTheme()<br/>- persists to localStorage<br/>- toggles html.dark class"]
    end

    subgraph "Data Consumers"
        DP["DashboardPage"]
        CIP["CheckInsPage"]
        IP["InspirePage"]
        PP["ProgressPage"]
        SUP["SignUpPage"]
        SIP["SignInPage"]
        LP["LandingPage"]
    end

    AH -->|getToken| AC
    AC -->|JWT Bearer| PAPI
    AC -->|JWT Bearer| CIAPI
    AC -->|JWT Bearer| CATAPI

    PAPI -->|used by| DP
    PAPI -->|used by| CIP
    CIAPI -->|used by| DP
    CIAPI -->|used by| CIP
    CIAPI -->|used by| IP
    CIAPI -->|used by| PP
    CATAPI -->|used by| DP
    CATAPI -->|used by| SUP
    CATAPI -->|used by| LP
    AAPI -->|used by| SIP
    AAPI -->|used by| SUP
    UAPI -->|used by| SUP

    AH -->|getUserId/getName/tokenExists| DP
    AH -->|getUserId| SUP
    AH -->|saveToken/clearToken| SIP
```

---

## 3. Authentication Sequence

```mermaid
sequenceDiagram
    participant U as User
    participant SIP as SignInPage
    participant AAPI as AuthAPI
    participant AH as AuthHandler
    participant BE as Spring Boot Backend
    participant JWTu as JwtUtil

    U->>SIP: Enter email + password
    SIP->>AAPI: signIn({email, password})
    AAPI->>BE: POST /auth/sign_in
    BE->>BE: AuthController → ISignInUseCase
    BE->>JWTu: generateToken(email, userId, name)
    JWTu-->>BE: "eyJhbGciOiJIUzI1NiJ9..."
    BE-->>AAPI: { token: "eyJ..." }
    AAPI-->>SIP: response.data
    SIP->>AH: saveToken(token)
    AH->>AH: localStorage.setItem("jwt", token)
    SIP->>SIP: navigate("/dashboard")

    Note over SIP,BE: Every subsequent authenticated request

    SIP->>AC: PracticeAPI.getPracticesByUser()
    AC->>AH: getToken()
    AH-->>AC: "eyJ..."
    AC->>AC: config.headers.Authorization = "Bearer eyJ..."
    AC->>BE: GET /practices/my (with Bearer)
    BE->>BE: JwtAuthFilter.doFilterInternal()
    BE->>BE: JwtUtil.extractEmail(token) → email
    BE->>BE: UserDetailsService.loadUserByUsername(email)
    BE->>BE: JwtUtil.validateToken(token) → true
    BE->>BE: set SecurityContext
    BE-->>AC: 200 OK + PracticeDto[]
    AC-->>SIP: response.data

    Note over SIP,BE: Sign out
    U->>SIP: Click "Sign Out"
    SIP->>AH: clearToken()
    AH->>AH: localStorage.removeItem("jwt")
    SIP->>SIP: navigate("/")
```

---

## 4. Check-In Data Flow (Dashboard)

```mermaid
sequenceDiagram
    participant U as User
    participant DP as DashboardPage
    participant PCard as PracticeCard
    participant CPopup as CheckInPopup
    participant CIAPI as CheckInAPI
    participant PAPI as PracticeAPI
    participant BE as Backend

    U->>PCard: Click checkmark ✓
    PCard->>DP: onDone(practiceId)
    DP->>CPopup: open with practiceId
    CPopup-->>U: Show mood picker + content form

    U->>CPopup: Select mood "GOOD", type reflection
    U->>CPopup: Toggle isPublic
    U->>CPopup: Click "Save"
    CPopup->>CIAPI: create({practiceId, mood:"GOOD", content: "...", public: true})
    CIAPI->>BE: POST /checkins/checkin
    BE->>BE: CreateCheckInUseCase
    BE->>BE: StreakValidator.validateStreak(practice)
    BE->>BE: practice.streak++, practice.lastUpdatedStreak = now
    BE->>BE: checkInRepository.save(CheckIn{streakValue=practice.streak})
    BE-->>CIAPI: 201 + CheckInDto
    CIAPI-->>CPopup: response.data
    CPopup-->>U: Show success animation
    CPopup->>DP: onSave() callback

    DP->>DP: showToast("Check-in saved!")
    DP->>PAPI: getPracticesByUser() [refetch]
    PAPI->>BE: GET /practices/my
    BE-->>PAPI: PracticeDto[] (updated checkedInToday flags)
    PAPI-->>DP: response
    DP->>DP: setPractices(updated)
    DP->>PCard: Re-render with updated streak + checkedInToday=true
```

---

## 5. Check-In Like Toggle Flow

```mermaid
sequenceDiagram
    participant U as User
    participant LB as LikeButton
    participant CIAPI as CheckInAPI
    participant BE as Backend

    U->>LB: Click heart icon
    Note over LB: Optimistic update: immediately toggle UI state
    LB->>LB: setLiked(!isLiked), setCount(count +/- 1)
    LB->>CIAPI: toggleLike(checkInId)
    CIAPI->>BE: POST /checkins/{id}/like
    BE->>BE: ToggleCheckInLikeUseCase
    BE-->>CIAPI: { liked: true, likeCount: 5 }
    CIAPI-->>LB: Response
    Note over LB: Confirm with server state
    LB->>LB: setLiked(response.liked), setCount(response.likeCount)

    Note over LB: On error: revert optimistic update
    LB-->>U: UI reflects actual state
```

---

## 6. Progress & Inspire Data Flow

```mermaid
flowchart TD
    subgraph "Progress Page"
        PP[ProgressPage] -->|"mount or date range change"| CIAPI["CheckInAPI.getAll(from, to)"]
        CIAPI -->|"GET /checkins?from=X&to=Y"| BE[Backend]
        BE -->|"return CheckInDto[]"| CIAPI
        CIAPI -->|"response"| PP
        
        PP -->|"groupCheckInsByPractice()"| G1["Map<practiceName, CheckIn[]>"]
        PP -->|"buildActivityData()"| G2["Array<{day, value}>"]
        
        G1 --> PLC["PracticeLineChart<br/>props: data, metric('streak'|'mood')<br/>recharts LineChart"]
        G2 --> UAC["UserActivityCalendar<br/>props: data<br/>SVG contribution grid"]
    end

    subgraph "Inspire Page"
        IP[InspirePage] -->|"mount"| CIAPI2["CheckInAPI.getFyp()"]
        CIAPI2 -->|"GET /checkins/fyp"| BE2[Backend]
        BE2 -->|"GetForYouCheckInsUseCase"| BE2
        BE2 -->|"RecommendationService<br/>(PracticeBased or Default strategy)"| BE2
        BE2 -->|"CheckInLikeEnricher<br/>(adds likeCount + isLikedByCurrentUser)"| BE2
        BE2 -->|"return CheckInDto[] (public only, enriched)"| CIAPI2
        CIAPI2 -->|"response"| IP
        
        IP -->|"view='feed'"| ICard["InspireCard[]<br/>grid layout<br/>each with LikeButton"]
        IP -->|"view='card'"| INav["InspireNavigator<br/>single card + prev/next"]
    end

    subgraph "Utilities"
        U1["dates.js<br/>formatDate, formatShort, isThisWeek, isThisMonth, today, daysAgo, formatTime"]
        U2["checkins.js<br/>moodToValue('AWFUL'→0, 'BAD'→1, ...)<br/>groupCheckInsByPractice(checkIns)<br/>buildActivityData(checkIns)"]
    end

    G1 -.-> U2
    G2 -.-> U2
    PP -.-> U1
    CIP[CheckInsPage] -.-> U1
```

---

## 7. Theme (Dark Mode) System

```mermaid
flowchart TD
    subgraph "Theme Toggle"
        TT["ThemeToggle.jsx<br/>(in NavBar)"]
        TT --> UT["useTheme.js hook<br/>- reads localStorage('solen-theme')<br/>- toggles html.dark class<br/>- persists choice"]
    end

    subgraph "CSS Layer"
        IC["index.css<br/>@import 'tailwindcss'<br/>@custom-variant dark (&:is(.dark *))<br/>@theme { ... solen tokens }<br/>html.dark {<br/>  --solen-bg: #1a1a2e<br/>  --solen-fg: #e0e0e0<br/>  ... dark overrides ...<br/>}"]
    end

    subgraph "Runtime"
        UT -->|"toggles html.dark"| HTML["html element"]
        HTML -->|"CSS cascade"| TAILWIND["Tailwind utilities<br/>bg-solen-bg → dark:bg-..."]
    end
```

---

## 8. E2E Testing (Playwright)

```mermaid
graph LR
    subgraph "E2E Setup"
        PW["playwright.config.js<br/>baseURL: localhost:5173<br/>chromium + firefox + webkit"]
    end

    subgraph "Test Files"
        LOGIN["e2e/login.spec.js<br/>1. Navigate to /sign-in<br/>2. Fill email + password<br/>3. Submit form<br/>4. Assert redirect to /dashboard<br/>5. Assert navbar shows user menu"]
    end

    PW --> LOGIN
```
