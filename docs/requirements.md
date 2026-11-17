# System Requirements Specification (SRS)

## 1. Functional Requirements (FR)

### Module 1: User & Authentication Management
- **FR-1.1**: User registration with university email (`@anurag.edu.in` or general email), full name, and minimum 8-character password.
- **FR-1.2**: Secure authentication using BCrypt password hashing and HTTP-only session cookies.
- **FR-1.3**: Automatic session restoration via `GET /api/auth/me` upon browser reload.
- **FR-1.4**: Graceful sign-out invalidating the session on both client and server.

### Module 2: Sports Facility & Lineup Discovery
- **FR-2.1**: Browse all facility courts categorized across 9 sports (Badminton, Basketball, Cricket, Football, Tennis, Volleyball, Table Tennis, Carrom, Chess).
- **FR-2.2**: Multi-criteria filtering:
  - Filter by Sport category pills.
  - Text search by court name, facility ground, or sports title.
  - Toggle switch for "Available courts only".
- **FR-2.3**: Real-time status badge indicators:
  - `Available` (Green badge)
  - `Occupied` (Red/Amber badge with live remaining time)
  - `Unavailable / Maintenance` (Muted gray badge)

### Module 3: Slot Reservation & Concurrency Control
- **FR-3.1**: Customizable duration selector pills (15, 30, 45, or 60 minutes).
- **FR-3.2**: Fair Play Rule: A student can hold only **one active booking** at any given moment. Concurrent attempts return HTTP 409 Conflict.
- **FR-3.3**: Branded confirmation modal displaying sport icon, court title, duration, and participant name prior to commit.
- **FR-3.4**: Unique booking code generation (e.g. `AU-BADM-9482`).

### Module 4: Live Session & Real-Time Countdown
- **FR-4.1**: Prominent top dashboard banner when a booking is active showing court name, ground, start time, and scheduled end time.
- **FR-4.2**: Synchronized UTC-to-local live countdown timer updating every second (`MM:SS`).
- **FR-4.3**: Dynamic visual session progress bar indicating elapsed percentage.

### Module 5: Cancellation & Early Court Release
- **FR-5.1**: Immediate early-release button on both the active session banner and within the Booking History card.
- **FR-5.2**: Confirmation prompt before cancellation to prevent accidental termination.
- **FR-5.3**: Instant state update: releases court lock, marks booking status as `CANCELLED`, and resets student concurrency lock.

### Module 6: Digital Sports Pass & Gate Verification
- **FR-6.1**: Dedicated Digital Sports Pass modal with student credentials, court location, valid time window, and simulated barcode for gate marshal inspection.
- **FR-6.2**: 1-click Print/Save view for physical backup or offline inspection.

---

## 2. Non-Functional Requirements (NFR)

| Category | Requirement | Target Metric |
| :--- | :--- | :--- |
| **Performance** | API response time under normal university load | < 120 ms for availability queries |
| **Availability** | Server uptime with resilient DataSource fallback | 99.9% uptime (fail-safe embedded H2 mode if primary DB drops) |
| **Scalability** | Cloud-ready connection pooling | HikariCP pool with optimized keep-alive & auto-recovery |
| **Security** | Credential protection & session isolation | BCrypt strength 10, HttpOnly SameSite cookies, CORS origin whitelist |
| **Usability** | Mobile-responsive design | Seamless experience on smartphones (iOS Safari, Android Chrome), tablets, and desktops |
| **Accessibility** | Clean semantic HTML & ARIA roles | WCAG 2.1 AA compliant alerts, modal traps, and contrast ratios |

---

## 3. User Stories
- **As a Student**, I want to see if the Badminton court is free right now from my phone, so that I don't waste time walking to the sports complex.
- **As a Student**, I want to book a court for 45 minutes with one click and receive a digital pass, so that I have proof of reservation at the sports reception.
- **As a Student**, I want to end my session early if we finish our match, so that fellow students can use the court immediately.
- **As a Sports Marshal**, I want students to present a verified booking code, so that court scheduling is orderly and verified.
