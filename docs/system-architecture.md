# System Architecture: Sports Complex Booking System

## 1. Architectural Overview

The application follows a modern **three-tier decoupled architecture** designed for high throughput, sub-second availability checks, and robust failover.

```mermaid
flowchart TD
    subgraph ClientLayer["Frontend Client Tier (React 19 + Vite 8)"]
        UI["Modern UI / Responsive Dashboard"]
        State["React State & Polling Engine (10s)"]
        Timer["UTC-Synchronized Clock Worker (1s)"]
        Pass["Digital Pass & Modal Controller"]
    end

    subgraph APILayer["Backend REST Tier (Spring Boot 3 + Java 21)"]
        Security["Spring Security 6 (BCrypt + Cookie Auth)"]
        Scheduler["Background Booking Expiration Scheduler"]
        
        subgraph Controllers["Spring REST Controllers"]
            AuthCtrl["AuthController (/api/auth)"]
            CourtCtrl["CourtController (/api/courts)"]
            BookingCtrl["BookingController (/api/bookings)"]
            HealthCtrl["HealthController (/api/health)"]
        end

        subgraph Services["Domain Service Tier"]
            AuthService["StudentDetailsService"]
            BookingService["BookingService (Concurrency & Slot Rules)"]
        end
    end

    subgraph DataLayer["Persistence Tier"]
        JPA["Spring Data JPA (Hibernate 6)"]
        Router["Resilient DataSourceConfig Router"]
        MySQL[("Production MySQL / TiDB Cloud (Port 3306)")]
        H2[("Fail-Safe In-Memory H2 Engine (MySQL Mode)")]
    end

    UI --> State
    State --> Timer
    State --> Pass
    State -- "HTTPS / JSON REST" --> Security
    Security --> Controllers
    Controllers --> Services
    Services --> JPA
    Scheduler --> Services
    JPA --> Router
    Router -- "Primary Target" --> MySQL
    Router -- "Fallback if DB Unreachable" --> H2
```

---

## 2. Request Lifecycle & Concurrency Flow

When a user initiates a booking reservation, the system validates slot availability and student concurrency before committing:

```mermaid
sequenceDiagram
    autonumber
    actor User as Student
    participant UI as React Frontend
    participant API as Spring Boot BookingService
    participant DB as MySQL Database

    User->>UI: Clicks "Book Court" (45 min)
    UI->>UI: Opens Branded Confirmation Modal
    User->>UI: Clicks "Confirm Reservation"
    UI->>API: POST /api/bookings { courtId, durationMinutes }
    
    critical Transaction Isolation
        API->>DB: Check student active booking lock
        alt Student already has ACTIVE booking
            DB-->>API: Active booking found
            API-->>UI: HTTP 409 Conflict ("Active session in progress")
            UI->>User: Displays animated error Toast
        else No active booking
            API->>DB: Check court active booking lock
            alt Court occupied
                DB-->>API: Court occupied
                API-->>UI: HTTP 409 Conflict ("Court already in use")
                UI->>User: Refreshes lineup & shows Toast
            else Court available
                API->>API: Generate unique bookingCode (AU-SPORT-XXXX)
                API->>DB: INSERT INTO bookings (ACTIVE, startTime, endTime)
                DB-->>API: Commit Successful
                API-->>UI: HTTP 201 Created (BookingResponse JSON)
                UI->>User: Show success Toast + Live Countdown & Progress Bar
            end
        end
    end
```

---

## 3. Resilience Architecture: Zero-Downtime Data Routing

Cloud deployments frequently suffer from transient database disconnects or trial cluster expirations. The backend includes a custom `DataSourceConfig` that prevents server crashes:

```mermaid
flowchart LR
    Start["Application Startup"] --> Probe["Probe Target DB (6s Timeout)"]
    Probe -- "Connection OK" --> Primary["Activate Primary MySQL / TiDB"]
    Probe -- "Timeout / Auth Failure" --> Fallback["Activate Embedded H2 (MySQL Mode)"]
    Primary --> Serve["Server Listens on Port 8080"]
    Fallback --> Seed["Auto-Seed Starter Courts"] --> Serve
```

---

## 4. Component Directory Organization

```
com.sportscomplex/
├── controller/        # REST endpoints and request validation records
│   ├── AuthController.java
│   ├── BookingController.java
│   ├── CourtController.java
│   └── HealthController.java
├── service/           # Business logic, fair-play rules, status transitions
│   ├── BookingService.java
│   └── StudentDetailsService.java
├── repository/        # Spring Data JPA interfaces with custom JPQL queries
│   ├── BookingRepository.java
│   ├── CourtRepository.java
│   └── StudentRepository.java
├── model/             # JPA Entities mapped to relational schema
│   ├── Booking.java
│   ├── Court.java
│   └── Student.java
├── config/            # Cross-cutting security, CORS, and resilient DataSource
│   ├── DataSourceConfig.java
│   ├── SecurityConfig.java
│   └── WebConfig.java
└── SportsComplexApplication.java  # Bootstrap with default UTC Timezone
```
