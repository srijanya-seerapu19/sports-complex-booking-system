# Quality Assurance & Test Plan: Sports Complex Booking System

## 1. Test Strategy Overview

The testing suite validates all layers of the application across three tiers:
1. **Unit Testing**: Service-level validation of fair-play concurrency rules, duration limits, and password encryption.
2. **Integration Testing**: Spring Boot mock MVC testing verifying REST endpoints, cookie security, and database persistence.
3. **End-to-End (E2E) Scenarios**: Complete user journeys in a browser: register -> explore courts -> filter by sport -> book slot -> view countdown -> view pass -> cancel reservation.

---

## 2. Test Cases Matrix

| Test ID | Module | Scenario / Input | Expected Result | Pass/Fail |
| :--- | :--- | :--- | :--- | :--- |
| **TC-AUTH-01** | Auth | Register new user with valid email & 8+ char password | HTTP 200, student saved, password hashed with BCrypt | PASS |
| **TC-AUTH-02** | Auth | Register with existing email address | HTTP 409 Conflict with descriptive error message | PASS |
| **TC-AUTH-03** | Auth | Login with incorrect password | HTTP 401 Unauthorized; no session cookie created | PASS |
| **TC-AUTH-04** | Auth | Session restoration with valid cookie (`GET /api/auth/me`) | HTTP 200 with student profile data | PASS |
| **TC-COURT-01**| Courts | Fetch all courts (`GET /api/courts`) | HTTP 200; array of courts across 9 sports with availability flags | PASS |
| **TC-COURT-02**| Courts | Filter courts by sport ("Badminton") | Only Badminton courts returned in filtered view | PASS |
| **TC-COURT-03**| Courts | Search courts by query "Main Ground" | Football and Cricket grounds returned | PASS |
| **TC-BOOK-01** | Booking| Book available court for 45 minutes | HTTP 201 Created; unique `bookingCode` generated; court becomes occupied | PASS |
| **TC-BOOK-02** | Booking| Attempt 2nd booking while 1st is active (Concurrency rule) | HTTP 409 Conflict ("You already have an active booking in progress") | PASS |
| **TC-BOOK-03** | Booking| Book an already occupied court | HTTP 409 Conflict; court status refreshed | PASS |
| **TC-BOOK-04** | Booking| Cancel active booking via `DELETE /api/bookings/{id}` | HTTP 200; booking marked `CANCELLED`; court immediately marked `available: true` | PASS |
| **TC-PASS-01** | Pass | View Digital Sports Pass modal | Displays student name, court, booking ID, time window, and barcode | PASS |
| **TC-TIME-01** | Timing | Cross-timezone clock synchronization (UTC to IST) | Countdown accurately reflects duration without offset drift | PASS |
| **TC-RESIL-01**| System | Start server when external database is unreachable | Fallback router switches to in-memory H2 without server crash | PASS |

---

## 3. Automated API Verification Script (PowerShell / Bash)

```bash
# 1. Health check
curl -s http://localhost:8080/api/health

# 2. Register demo student
curl -s -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{"fullName":"Test Runner","email":"runner@anurag.edu.in","password":"Password123!"}'

# 3. Sign in & save session cookie
curl -s -c cookies.txt -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"runner@anurag.edu.in","password":"Password123!"}'

# 4. Check courts lineup
curl -s http://localhost:8080/api/courts

# 5. Book court 1 for 30 minutes
curl -s -b cookies.txt -X POST http://localhost:8080/api/bookings \
  -H "Content-Type: application/json" \
  -d '{"courtId":1,"durationMinutes":30}'

# 6. Retrieve booking history
curl -s -b cookies.txt http://localhost:8080/api/bookings/mine
```
