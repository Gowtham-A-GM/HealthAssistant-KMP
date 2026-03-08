# Health Assistant — Official App Documentation

**Version:** 1.0  
**Platform:** Android (KMP-ready)  
**Architecture:** MVVM · Kotlin Multiplatform · Jetpack Compose Multiplatform

---

## Table of Contents

1. [App Overview](#1-app-overview)
2. [Tech Stack](#2-tech-stack)
3. [KMP Architecture & Platform Expansion](#3-kmp-architecture--platform-expansion)
4. [Project Structure](#4-project-structure)
5. [Screen Flows](#5-screen-flows)
6. [Authentication Flow](#6-authentication-flow)
7. [Assessment Flow (Detailed)](#7-assessment-flow-detailed)
8. [AI Chat Feature](#8-ai-chat-feature)
9. [Server Communication — API Contract](#9-server-communication--api-contract)
10. [Local Database (SQLDelight)](#10-local-database-sqldelight)
11. [Cloud Data Synchronization](#11-cloud-data-synchronization)
12. [Data Layer Architecture](#12-data-layer-architecture)

---

## 1. App Overview

**Health Assistant** is a Kotlin Multiplatform (KMP) mobile application that provides users with an AI-powered health assessment experience. Users can describe their symptoms through conversational question-and-answer flows, select body regions on an interactive visual body map, or upload images for analysis. The app processes responses through a cloud-hosted backend, generates probable diagnoses with severity ratings, and offers an AI chat interface for follow-up consultation.

### Core Features

| Feature | Description |
|---|---|
| Symptom Assessment | Step-by-step guided question flow driven by the server |
| Visual Body Map | Interactive anatomical diagram for pointing to the affected area |
| Image Upload | Photo attachment support for visual symptom documentation |
| AI Report Generation | Probabilistic diagnosis with possible causes, severity level, and advice |
| Cause Deep-Dive | Detailed view of each possible cause with what-you-can-do-now guidance |
| AI Chat | Follow-up conversation with an AI assistant anchored to a specific report |
| Health History | Persistent list of all past assessment reports |
| Health Profile | General and medical profile stored locally and synced to the server |
| Bootstrap Sync | On login, all server-side user data is pulled to the local database |

---

## 2. Tech Stack

### Core Language & Framework

| Technology | Version | Role |
|---|---|---|
| Kotlin | 2.3.0 | Primary language (multiplatform) |
| Kotlin Multiplatform (KMP) | 2.3.0 | Shared business logic across platforms |
| Jetpack Compose Multiplatform | Latest stable | Declarative UI framework |
| Material3 | Latest | Design system and component library |

### Networking

| Technology | Role |
|---|---|
| **Ktor Client** (`io.ktor:ktor-client-*`) | HTTP client for all API calls |
| `ktor-client-content-negotiation` | Automatic JSON serialization/deserialization |
| `ktor-client-logging` | Full request/response logging (DEBUG builds) |
| `ktor-client-android` | Android-specific Ktor engine |

**Ktor Configuration (NetworkClient.kt):**
- Request timeout: **120 seconds**
- Connect timeout: **30 seconds**
- Socket timeout: **120 seconds**
- JSON: lenient mode, `ignoreUnknownKeys = true`
- Global `DefaultRequest` plugin automatically injects `Authorization: Bearer <token>` on every request

### Serialization

| Technology | Role |
|---|---|
| `kotlinx.serialization` | JSON encode/decode for all DTOs |

### Local Database

| Technology | Role |
|---|---|
| **SQLDelight** (`app.cash.sqldelight`) | Type-safe SQL local persistence |
| `android-driver` | Android runtime SQLite driver |

### State Management

| Technology | Role |
|---|---|
| `ViewModel` (AndroidX) | Lifecycle-aware state holder |
| `StateFlow` / `MutableStateFlow` | Reactive UI state stream |
| Sealed `Event` classes | Uni-directional event model |
| Sealed `State` data classes | UI snapshot model |

### Authentication

| Technology | Role |
|---|---|
| JWT Bearer Token | Server-issued token on login/signup |
| `TokenManager` (in-memory singleton) | Holds the token for the session lifetime |

### Speech & Accessibility

| Technology | Role |
|---|---|
| `SpeechToTextManager` (expect/actual) | Platform-specific speech recognition |
| `TextToSpeechManager` (expect/actual) | Platform-specific text-to-speech |

These are declared as `expect class` in `commonMain` and implemented as `actual class` in each platform's source set (`androidMain`, `iosMain`, etc.).

### Logging

| Technology | Role |
|---|---|
| `AppLogger` (custom wrapper) | Unified logging facade, wraps platform loggers |

---

## 3. KMP Architecture & Platform Expansion

### Current State

As of this version, the app targets **Android only**. The full UI, business logic, networking, and local database layers are implemented and working on Android.

### Multiplatform Readiness

The project is structured as a **Kotlin Multiplatform** project from the ground up. This means expanding to additional platforms is a matter of providing platform-specific implementations — not rewriting the app.

```
composeApp/
  src/
    commonMain/       ← 100% shared: ViewModels, Repositories, 
    │                    DTOs, Domain Models, SQLDelight schema,
    │                    Compose UI, Navigation
    androidMain/      ← Android: actual STT/TTS, SqliteDriver, 
    │                    SpeechRecognizer
    iosMain/          ← iOS: actual STT/TTS, NativeSqliteDriver (stub)
    jvmMain/          ← Desktop JVM: actual implementations
```

### What Is Already Shared (commonMain)

- All **UI screens** (Compose Multiplatform)
- All **ViewModels** and **state management**
- All **API calls** (Ktor is multiplatform)
- All **serialization** (kotlinx.serialization is multiplatform)
- All **SQLDelight SQL schemas** (the `.sq` files are multiplatform)
- All **domain models** and **repository interfaces**
- All **navigation logic**

### Platform Expansion Path

| Platform | What To Add |
|---|---|
| **iOS** | Provide `actual` SpeechToText / TTS using AVFoundation; use `NativeSqliteDriver`; build via Xcode / KMM Xcode plugin |
| **Desktop (JVM)** | Provide `actual` STT/TTS using OS APIs; use `JdbcSqliteDriver`; package with Compose Desktop |
| **Web (Wasm/JS)** | Provide `actual` implementations using Web Speech API; use `WebWorkerDriver` for SQLDelight or IndexedDB |

### Key Expand Points

1. `SpeechToTextManager` — `expect class` in commonMain, `actual class` needed per platform
2. `TextToSpeechManager` — same pattern
3. SQLDelight driver — `AndroidSqliteDriver` in `androidMain`; swap per platform in respective source sets
4. Ktor engine — `OkHttp` or `Android` engine in androidMain; `Darwin` for iOS; `Java` for Desktop

Because navigation and all screen logic live in `commonMain`, **zero screen code needs to change** when expanding platforms.

---

## 4. Project Structure

```
composeApp/src/commonMain/kotlin/com/example/healthassistant/
│
├── App.kt                          ← Root composable, navigation host
├── core/
│   ├── auth/
│   │   └── TokenManager.kt         ← In-memory JWT token storage
│   ├── network/
│   │   ├── AppConfig.kt            ← BASE_URL constant
│   │   └── NetworkClient.kt        ← Ktor HttpClient configuration
│   └── util/
│       └── AppLogger.kt            ← Logging wrapper
│
├── domain/
│   └── model/
│       ├── assessment/             ← Question, Report, PossibleCause, 
│       │                              CauseDetail, PatientInfo,
│       │                              AssessmentSession, ResponseOption
│       ├── auth/                   ← User
│       ├── chat/                   ← ChatMessage
│       └── profile/                ← ProfileQuestion
│
├── data/
│   ├── local/                      ← SQLDelight generated DAOs
│   │   └── db/                     ← health.db reference
│   └── remote/
│       ├── assessment/
│       │   ├── dto/                ← All assessment DTOs
│       │   ├── AssessmentApi.kt    ← Interface
│       │   └── AssessmentApiImpl.kt
│       ├── auth/
│       │   ├── dto/                ← AuthRequestDto, AuthResponseDto
│       │   ├── AuthApi.kt
│       │   └── AuthApiImpl.kt
│       ├── chat/
│       │   ├── dto/                ← ChatStartRequestDto, etc.
│       │   ├── ChatApi.kt
│       │   └── ChatApiImpl.kt
│       ├── bootstrap/
│       │   ├── dto/                ← BootstrapResponseDto
│       │   ├── BootstrapApi.kt
│       │   └── BootstrapApiImpl.kt
│       └── profile/
│           ├── dto/
│           ├── ProfileApi.kt
│           └── ProfileApiImpl.kt
│
├── presentation/
│   ├── screens/
│   │   ├── login/                  ← LoginScreen, LoginViewModel
│   │   ├── signup/                 ← SignupScreen, SignupViewModel
│   │   ├── onboarding/             ← OnboardingProfileScreen, 
│   │   │                              OnboardingMedicalScreen
│   │   ├── home/                   ← HomeScreen, HomeViewModel
│   │   ├── assessment/             ← AssessmentScreen, AssessmentViewModel
│   │   │   └── body/               ← BodyMap, BodySelector, 
│   │   │                              BodyPartBottomSheet, BodyRegionData
│   │   ├── chat/                   ← ChatScreen, ChatViewModel
│   │   ├── history/                ← HistoryScreen
│   │   ├── report/                 ← AssessmentReportScreen
│   │   ├── causedetail/            ← CauseDetailScreen
│   │   ├── settings/               ← SettingsScreen
│   │   ├── editprofile/            ← EditProfileScreen
│   │   └── news/                   ← NewsScreen
│   └── components/                 ← Shared UI components
│
└── AppScreen.kt                    ← Navigation route definitions
```

---

## 5. Screen Flows

### 5.1 Navigation Routes

All routes are defined as a sealed class `AppScreen`:

```
AppScreen
├── Login
├── Signup
├── OnboardingProfile
├── OnboardingMedical
├── Home
├── Settings
├── EditProfile
├── EditMedical
├── Assessment
├── AssessmentReport
├── AssessmentCauseDetail(cause: PossibleCause)
├── History
├── HistoryDetail
├── CauseDetail(title: String)
├── Chat(reportId: String?)
└── News
```

### 5.2 First-Time User Flow

```
App Launch
    │
    ▼
[Login Screen]
    │ No account?
    ▼
[Signup Screen]  ──► POST /auth/signup ──► token saved in TokenManager
    │
    ▼
[Onboarding: General Profile]  ──► POST /user/profile/onboarding
    │
    ▼
[Onboarding: Medical Profile]  ──► POST /user/medical/onboarding
    │
    ▼
[Home Screen]
```

### 5.3 Returning User Flow

```
App Launch
    │
    ▼
[Login Screen]  ──► POST /auth/login ──► token saved
    │
    ▼
GET /user/bootstrap  (syncs all user data to local DB)
    │
    ▼
[Home Screen]
```

### 5.4 Home Screen

The Home Screen is the central hub. From here the user can navigate to:

```
[Home Screen]
    ├──► [Assessment Screen]          ← "Start Assessment" action
    ├──► [Chat Screen]                ← "Ask AI" action (no specific report)
    ├──► [History Screen]             ← Past reports list
    ├──► [News Screen]                ← Health news feed
    └──► [Settings Screen]            ← Profile & preferences
```

### 5.5 Assessment Flow

```
[Assessment Screen]
    │
    ├── GET /assessment/start ──► Receives first Question + session_id
    │
    ├── [Question Loop]
    │       │
    │       ├── Text Input (response_type: "text")
    │       │       └── POST /assessment/answer
    │       │
    │       ├── Single Select (response_type: "single_select")
    │       │       └── POST /assessment/answer
    │       │
    │       ├── Multi Select (response_type: "multi_select")
    │       │       └── POST /assessment/answer
    │       │
    │       ├── Visual Body Map (response_type: "visual" or similar)
    │       │       └── User taps body region → selects symptom
    │       │           └── POST /assessment/answer (value = symptom label)
    │       │
    │       └── Image Upload (response_type includes image)
    │               └── POST /assessment/answer (multipart/form-data)
    │
    ├── Server returns status: "completed" → session done
    │
    ├── POST /assessment/report ──► Receives ReportDto
    │       └── Report saved to local SQLDelight DB
    │
    └── Navigate to [AssessmentReport Screen]

[AssessmentReport Screen]
    ├── Displays: Summary, Urgency Level, Possible Causes list
    ├── Tap cause ──► [AssessmentCauseDetail Screen]
    └── "Ask AI" ──► [Chat Screen] with this report's ID
```

### 5.6 Chat Flow

```
[Chat Screen]
    │
    ├── POST /chat/start  (with optional report_id)
    │       └── Receives: session_id, opening message
    │
    ├── [Message Loop]
    │       ├── User types message
    │       ├── POST /chat/message  (session_id + message)
    │       └── Receives: AI response, displayed in chat UI
    │
    └── Exit / End ──► POST /chat/end
            └── Message history stored in local chat_messages table
```

### 5.7 History & Reports

```
[History Screen]
    └── Reads reports from local SQLDelight DB (ordered by date DESC)
            └── Tap report ──► [HistoryDetail / CauseDetail screen]
```

### 5.8 Settings Flow

```
[Settings Screen]
    ├──► [EditProfile Screen]   ──► POST /user/profile/onboarding (update)
    └──► [EditMedical Screen]   ──► POST /user/medical/onboarding (update)
```

---

## 6. Authentication Flow

### Signup

1. User enters email + password on **SignupScreen**
2. App sends `POST /auth/signup` with `{ email, password }`
3. Server returns `AuthResponseDto { success, message, token }`
4. On success, `TokenManager.saveToken(token)` stores the JWT in memory
5. All subsequent Ktor requests automatically include `Authorization: Bearer <token>`

### Login

1. Same flow as signup but via `POST /auth/login`
2. On success, bootstrap sync (`GET /user/bootstrap`) is triggered to pull server data into local DB

### Token Lifecycle

- Token is stored **in-memory only** (not persisted to disk)
- Token is cleared on logout via `TokenManager.clearToken()`
- On each Ktor request, `TokenManager.getToken()` is called dynamically (via the `DefaultRequest` plugin)
- If the app process is killed, the user must log in again

---

## 7. Assessment Flow (Detailed)

### 7.1 Session Initialization

```kotlin
GET /assessment/start
Response: StartAssessmentResponseDto {
    session_id: String,
    question: QuestionDto,
    stored_answers: List<StoredAnswerItemDto>
}
```

The repository stores `session_id` and `storedAnswersMap` in memory. The first question is presented to the user.

### 7.2 Question Types

| `response_type` | UI Component | Answer Format |
|---|---|---|
| `"text"` | Free text input | `{ type: "text", value: "..." }` |
| `"single_select"` | Radio / option list | `{ type: "single_select", selected_option_id, selected_option_label }` |
| `"multi_select"` | Checkbox list | `{ type: "multi_select", selected_option_ids: [...], selected_option_labels: [...] }` |
| `"visual"` (body map) | Interactive body map | `{ type: <response_type>, value: "symptom label" }` |
| Image-type | Camera / gallery picker | Multipart form data (see §7.4) |

### 7.3 Answer Submission (JSON)

For text and selection-type answers:

```
POST /assessment/answer
Content-Type: application/json

{
  "session_id": "abc123",
  "question_id": "q_001",
  "question_text": "Where is your pain?",
  "answer_json": {
    "type": "single_select",
    "value": null,
    "selected_option_id": "opt_02",
    "selected_option_label": "Chest",
    "selected_option_ids": null,
    "selected_option_labels": null
  }
}
```

### 7.4 Answer Submission (Image / Multipart)

For questions requiring image input:

```
POST /assessment/answer
Content-Type: multipart/form-data

Fields:
  session_id      : String
  question_id     : String
  question_text   : String
  answer_json     : String (JSON-encoded AnswerDto)
  image           : ByteArray (image file, max 50MB before compression)
```

If the image exceeds 50 MB, the repository truncates it to fit before upload.

### 7.5 Server Response Per Answer

```json
{
  "status": "next",
  "question": {
    "question_id": "q_002",
    "text": "How long have you had this symptom?",
    "response_type": "single_select",
    "response_options": [ ... ],
    "is_compulsory": true
  }
}
```

When `status = "completed"`, no further question is returned and the session is done.

### 7.6 Report Generation

After `status = "completed"`:

```
POST /assessment/report
{ "session_id": "abc123" }
```

The server returns a `ReportDto` which is:
1. Mapped to the domain `Report` model
2. Saved to the local `reports` SQLDelight table as serialized JSON
3. Presented on the `AssessmentReport` screen

### 7.7 Visual Body Map

The body map screen shows a full anatomical SVG-style renderable figure drawn entirely in Jetpack Compose Canvas using `Path` objects defined in `BodyRegionData`. Each path corresponds to an anatomical region (head, chest, left arm, etc.).

When a region is tapped:
1. `BodySelector` detects the touch and identifies which path was hit
2. A bottom sheet opens listing sub-parts and symptom options for that region
3. User selects a symptom label
4. `AssessmentViewModel` fires `AssessmentEvent.VisualSymptomSelected`
5. The answer is submitted as `AnswerDto(type = question.responseType, value = symptomLabel)`

---

## 8. AI Chat Feature

The chat feature allows users to have a free-form conversation with an AI health assistant, optionally contextualized to a specific assessment report.

### Entry Points

- From **Home Screen** (general health question, `reportId = null`)
- From **AssessmentReport Screen** (anchored to a specific report, `reportId` passed)

### Chat Lifecycle

| Step | API Call | Payload |
|---|---|---|
| Start session | `POST /chat/start` | `{ main_report_id?: String, entry_point: String }` |
| Send message | `POST /chat/message` | `{ session_id: String, message: String }` |
| End session | `POST /chat/end` | `{ session_id: String }` |

### Local Persistence

All messages are stored in the `chat_messages` SQLDelight table:

```sql
chat_messages (
    id         TEXT PRIMARY KEY,
    session_id TEXT,
    role       TEXT,   -- "user" or "assistant"
    content    TEXT,
    timestamp  INTEGER
)
```

Messages are retrieved by `session_id`, so users can review the conversation history.

---

## 9. Server Communication — API Contract

### Base URL

```
http://16.16.121.165:8000
```
This is an **AWS EC2**-hosted Python/FastAPI backend.

### 9.1 Authentication Endpoints

| Method | Endpoint | Request Body | Response |
|---|---|---|---|
| `POST` | `/auth/signup` | `{ "email": String, "password": String }` | `{ "success": Boolean, "message": String, "token": String? }` |
| `POST` | `/auth/login` | `{ "email": String, "password": String }` | `{ "success": Boolean, "message": String, "token": String? }` |

### 9.2 Assessment Endpoints

| Method | Endpoint | Auth | Request | Response |
|---|---|---|---|---|
| `GET` | `/assessment/start` | ✅ Bearer | — | `StartAssessmentResponseDto` |
| `POST` | `/assessment/answer` | ✅ Bearer | `SubmitAnswerRequestDto` OR multipart | `SubmitAnswerResponseDto` |
| `POST` | `/assessment/report` | ✅ Bearer | `{ "session_id": String }` | `ReportDto` |
| `POST` | `/assessment/end` | ✅ Bearer | `{ "session_id": String }` | — |
| `GET` | `/assessment/reports` | ✅ Bearer | — | `List<ReportDto>` |

#### StartAssessmentResponseDto

```json
{
  "session_id": "string",
  "question": {
    "question_id": "string",
    "text": "string",
    "response_type": "text | single_select | multi_select | ...",
    "response_options": [
      { "id": "string", "label": "string" }
    ],
    "is_compulsory": true
  },
  "stored_answers": [
    {
      "question_id": "string",
      "question_text": "string",
      "answer": { ... }
    }
  ]
}
```

#### SubmitAnswerRequestDto

```json
{
  "session_id": "string",
  "question_id": "string",
  "question_text": "string",
  "answer_json": {
    "type": "string",
    "value": "string | null",
    "selected_option_id": "string | null",
    "selected_option_label": "string | null",
    "selected_option_ids": ["string"] | null,
    "selected_option_labels": ["string"] | null
  }
}
```

#### SubmitAnswerResponseDto

```json
{
  "status": "next | completed",
  "question": { ... QuestionDto ... }
}
```

#### ReportDto

```json
{
  "report_id": "string",
  "assessment_topic": "string",
  "generated_at": "string",
  "summary": ["string"],
  "possible_causes": [
    {
      "id": "string",
      "title": "string",
      "short_description": "string",
      "subtitle": "string | null",
      "severity": "string",
      "probability": 0.85,
      "detail": {
        "about_this": ["string"],
        "percentage": 85,
        "common_description": "string",
        "what_you_can_do_now": ["string"],
        "warning": "string | null"
      }
    }
  ],
  "advice": ["string"],
  "urgency_level": "string",
  "patient_info": {
    "name": "string",
    "age": "string",
    "gender": "string"
  }
}
```

### 9.3 Chat Endpoints

| Method | Endpoint | Auth | Request Body | Response |
|---|---|---|---|---|
| `POST` | `/chat/start` | ✅ Bearer | `{ "main_report_id": String?, "entry_point": String }` | `{ "session_id": String, "message": String }` |
| `POST` | `/chat/message` | ✅ Bearer | `{ "session_id": String, "message": String }` | `{ "message": String }` |
| `POST` | `/chat/end` | ✅ Bearer | `{ "session_id": String }` | — |

### 9.4 User / Profile Endpoints

| Method | Endpoint | Auth | Request Body | Response |
|---|---|---|---|---|
| `GET` | `/user/bootstrap` | ✅ Bearer | — | `BootstrapResponseDto` |
| `POST` | `/user/profile/onboarding` | ✅ Bearer | `List<QuestionAnswerDto>` | — |
| `POST` | `/user/medical/onboarding` | ✅ Bearer | `List<QuestionAnswerDto>` | — |

#### BootstrapResponseDto

```json
{
  "reports": [ ...ReportDto... ],
  "profile": [
    { "question_id": "string", "question_text": "string", "answer": { ... } }
  ],
  "medical": [
    { "question_id": "string", "question_text": "string", "answer": { ... } }
  ]
}
```

### 9.5 Global Request Headers

Every API call (except `/auth/*`) includes:

```
Authorization: Bearer <jwt_token>
Content-Type: application/json
```

Injected automatically via Ktor's `DefaultRequest` plugin using `TokenManager.getToken()`.

---

## 10. Local Database (SQLDelight)

**Database file:** `health.db`  
**Android driver:** `AndroidSqliteDriver`  
**Location on device:** Internal app storage (private to the app)

The schema is defined in `.sq` files in `commonMain/sqldelight/db/`, making it fully multiplatform.

### 10.1 Table: `reports`

Stores downloaded/generated assessment reports as serialized JSON blobs.

```sql
CREATE TABLE reports (
    id          TEXT NOT NULL PRIMARY KEY,
    report_json TEXT NOT NULL,
    date        TEXT NOT NULL
);
```

| Column | Type | Description |
|---|---|---|
| `id` | TEXT PK | Unique report ID from server |
| `report_json` | TEXT | Full `ReportDto` serialized as JSON string |
| `date` | TEXT | ISO date string for display and ordering |

**Queries:**
- `insertOrReplace` — upsert a report (used on bootstrap sync and after new assessment)
- `getAll` — fetch all reports ordered by `date DESC`
- `getById` — fetch single report by ID
- `deleteAll` — clear all reports (used in logout/reset)

### 10.2 Table: `chat_messages`

Stores chat message history per session.

```sql
CREATE TABLE chat_messages (
    id         TEXT NOT NULL PRIMARY KEY,
    session_id TEXT NOT NULL,
    role       TEXT NOT NULL,
    content    TEXT NOT NULL,
    timestamp  INTEGER NOT NULL
);
```

| Column | Type | Description |
|---|---|---|
| `id` | TEXT PK | Unique message ID |
| `session_id` | TEXT | Links messages to a chat session |
| `role` | TEXT | `"user"` or `"assistant"` |
| `content` | TEXT | Message text |
| `timestamp` | INTEGER | Unix timestamp in ms |

**Queries:**
- `insertMessage` — store a new message
- `getMessagesBySession(session_id)` — load conversation thread
- `deleteBySession(session_id)` — remove a session's history

### 10.3 Table: `GeneralProfile`

Stores general health profile question-answer pairs (e.g., name, age, lifestyle).

```sql
CREATE TABLE GeneralProfile (
    question_id   TEXT NOT NULL PRIMARY KEY,
    question_text TEXT NOT NULL,
    answer_json   TEXT NOT NULL
);
```

**Queries:** `insertOrReplace`, `getAll`, `deleteAll`

### 10.4 Table: `MedicalProfile`

Stores medical history question-answer pairs (e.g., chronic conditions, allergies, medications).

```sql
CREATE TABLE MedicalProfile (
    question_id   TEXT NOT NULL PRIMARY KEY,
    question_text TEXT NOT NULL,
    answer_json   TEXT NOT NULL
);
```

**Queries:** `insertOrReplace`, `getAll`, `deleteAll`

### 10.5 Table: `profile_answers`

General-purpose profile answer store (used by onboarding and edit profile flows).

```sql
CREATE TABLE profile_answers (
    question_id   TEXT NOT NULL PRIMARY KEY,
    question_text TEXT NOT NULL,
    answer_json   TEXT NOT NULL
);
```

**Queries:** `insertOrReplace`, `getById`, `deleteAll`, `selectAll`

### 10.6 Table: `AssessmentContext`

Stores the running context of an in-progress assessment (questions + answers so far). Enables resuming an assessment or passing context to the AI chat.

```sql
CREATE TABLE AssessmentContext (
    question_id          TEXT NOT NULL PRIMARY KEY,
    question_text        TEXT NOT NULL,
    response_type        TEXT NOT NULL,
    response_options_json TEXT NOT NULL,
    answer_json          TEXT NOT NULL
);
```

| Column | Type | Description |
|---|---|---|
| `question_id` | TEXT PK | Server-assigned question ID |
| `question_text` | TEXT | The displayed question string |
| `response_type` | TEXT | `"text"`, `"single_select"`, etc. |
| `response_options_json` | TEXT | JSON-encoded list of options |
| `answer_json` | TEXT | User's given answer as JSON |

**Queries:** `selectAll`, `insertContext`, `clearAll`

---

## 11. Cloud Data Synchronization

### 11.1 Bootstrap Sync (On Login)

After a successful login, the app calls `GET /user/bootstrap` to pull the user's server-side state into the local database. This ensures the app is functional and up-to-date even on a fresh install or new device.

```
Login Success
    │
    ▼
GET /user/bootstrap
    │
    ├── response.reports     → insertOrReplace each into local `reports` table
    ├── response.profile     → insertOrReplace each into local `GeneralProfile` table
    └── response.medical     → insertOrReplace each into local `MedicalProfile` table
```

After bootstrap, all screens read from the **local SQLDelight database** — no additional network calls needed for already-synced data.

### 11.2 New Report Sync (After Assessment)

When an assessment is completed:

1. `POST /assessment/report` is called
2. Server returns the `ReportDto`
3. The repository immediately saves it to the local `reports` table via `insertOrReplace`
4. The History screen (which reads from local DB) will reflect the new report instantly without any additional network call

### 11.3 Profile Updates

When the user edits their profile:

1. Updated answers are submitted to `POST /user/profile/onboarding` or `POST /user/medical/onboarding`
2. The same answers are saved to the corresponding local SQLDelight table
3. This keeps local and remote state in sync immediately after any edit

### 11.4 Data Flow Diagram

```
┌─────────────────────────────────┐
│         Remote Server           │
│   (AWS EC2 · Python/FastAPI)    │
│                                 │
│  /auth/*                        │
│  /assessment/*                  │
│  /chat/*                        │
│  /user/*                        │
└────────────┬────────────────────┘
             │ HTTPS · Ktor Client
             │ Bearer JWT Token
             ▼
┌─────────────────────────────────┐
│        Repository Layer         │
│  (AssessmentRepositoryImpl,     │
│   ChatRepositoryImpl, etc.)     │
│                                 │
│  ┌─────────────────────────┐    │
│  │  Remote API (Ktor)      │    │
│  └──────────┬──────────────┘    │
│             │ upsert on fetch   │
│  ┌──────────▼──────────────┐    │
│  │  Local DB (SQLDelight)  │    │
│  │  health.db              │    │
│  └──────────┬──────────────┘    │
└─────────────┼───────────────────┘
              │ StateFlow
              ▼
┌─────────────────────────────────┐
│         ViewModel Layer         │
│  (StateFlow → UI State)         │
└─────────────┬───────────────────┘
              │ Compose State
              ▼
┌─────────────────────────────────┐
│         UI Layer                │
│  (Jetpack Compose screens)      │
└─────────────────────────────────┘
```

---

## 12. Data Layer Architecture

### 12.1 Repository Pattern

Each feature domain has:
- An **interface** (in `domain/`) defining the contract
- An **implementation** (in `data/repository/`) that wires together remote API + local DB

```
AssessmentRepository (interface)
    └── AssessmentRepositoryImpl
            ├── AssessmentApiImpl     (remote Ktor calls)
            └── AssessmentLocalImpl   (SQLDelight queries)
```

### 12.2 DTO → Domain Mapping

All API responses (DTOs) are mapped to clean domain models before reaching the ViewModel:

```
Server JSON
    → kotlinx.serialization → DTO (data class with @Serializable)
    → .toDomain() extension function
    → Domain Model (used in ViewModels / UI)
```

This ensures the UI layer is never directly coupled to the server's data contract.

### 12.3 MVVM Unidirectional Data Flow

```
User Action (tap, type, etc.)
    │
    ▼
ViewModel.onEvent(AssessmentEvent.*)
    │
    ▼
Repository.suspendFunction()
    │ (coroutine, IO dispatcher)
    ▼
API call / DB query
    │
    ▼
_state.update { ... }
    │
    ▼
Compose recomposition (UI updates)
```

### 12.4 Error Handling

- Network errors are caught in the repository layer with `try/catch`
- Error states are surfaced in the `AssessmentState` (and other state classes) as a `errorMessage: String?` field
- ViewModels post errors to the state, which the UI reads to show snackbars or dialogs

---

*Documentation generated for Health Assistant v1.0. For questions about the backend API, refer to the Python/FastAPI server codebase hosted on AWS EC2.*
