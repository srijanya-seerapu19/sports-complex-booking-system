# REST API Documentation: Sports Complex Booking System

Base URL: `http://localhost:8080/api` (Local Dev) | `https://sports-court-availability-backend.onrender.com/api` (Production)

All endpoints accept and return `application/json` unless otherwise specified. Authentication is managed via HTTP-only session cookies (`JSESSIONID`).

---

## 1. Authentication Endpoints (`/api/auth`)

### 1.1 Register Student Account
Creates a new student account and returns profile credentials.

- **URL**: `/api/auth/register`
- **Method**: `POST`
- **Request Body**:
```json
{
  "fullName": "Akhil Sharma",
  "email": "akhil@anurag.edu.in",
  "password": "Password123!"
}
```
- **Responses**:
  - `200 OK`: Account created successfully.
  - `400 Bad Request`: Validation failure (short password, missing fields).
  - `409 Conflict`: Email is already registered.

---

### 1.2 Sign In (Login)
Authenticates credentials and establishes a secure session cookie.

- **URL**: `/api/auth/login`
- **Method**: `POST`
- **Request Body**:
```json
{
  "email": "akhil@anurag.edu.in",
  "password": "Password123!"
}
```
- **Responses**:
  - `200 OK`:
  ```json
  {
    "id": 1,
    "email": "akhil@anurag.edu.in",
    "fullName": "Akhil Sharma",
    "role": "STUDENT"
  }
  ```
  - `401 Unauthorized`: Invalid email or password.

---

### 1.3 Get Current Authenticated Profile
Restores the user session upon browser refresh.

- **URL**: `/api/auth/me`
- **Method**: `GET`
- **Headers**: Cookie (`JSESSIONID`)
- **Responses**:
  - `200 OK`: Profile details of current user.
  - `401 Unauthorized`: No active session.

---

### 1.4 Sign Out (Logout)
Invalidates the current session.

- **URL**: `/api/auth/logout`
- **Method**: `POST`
- **Responses**:
  - `200 OK`: Signed out successfully.

---

## 2. Facility & Court Endpoints (`/api/courts`)

### 2.1 Get All Courts with Live Availability Status
Returns all facilities, their active state, and details of any active session currently in progress.

- **URL**: `/api/courts`
- **Method**: `GET`
- **Responses**:
  - `200 OK`:
  ```json
  [
    {
      "id": 1,
      "name": "Badminton Court 1",
      "sport": "Badminton",
      "location": "Sports Complex",
      "active": true,
      "available": true,
      "booking": null
    },
    {
      "id": 2,
      "name": "Football Ground",
      "sport": "Football",
      "location": "Main Ground",
      "active": true,
      "available": false,
      "booking": {
        "bookingCode": "AU-FOOT-8192",
        "startTime": "2026-11-17T14:30:00",
        "endTime": "2026-11-17T15:30:00",
        "durationMinutes": 60
      }
    }
  ]
  ```

---

## 3. Booking Endpoints (`/api/bookings`)

### 3.1 Create Court Reservation
Reserves an available court for the authenticated student.

- **URL**: `/api/bookings`
- **Method**: `POST`
- **Headers**: Cookie (`JSESSIONID`), `Content-Type: application/json`
- **Request Body**:
```json
{
  "courtId": 1,
  "durationMinutes": 45
}
```
- **Responses**:
  - `201 Created`:
  ```json
  {
    "id": 14,
    "bookingCode": "AU-BADM-9482",
    "courtId": 1,
    "courtName": "Badminton Court 1",
    "sport": "Badminton",
    "studentEmail": "akhil@anurag.edu.in",
    "startTime": "2026-11-17T14:40:00",
    "endTime": "2026-11-17T15:25:00",
    "durationMinutes": 45,
    "status": "ACTIVE"
  }
  ```
  - `401 Unauthorized`: Not signed in.
  - `409 Conflict`: 
    - Student already has an active session (*"You already have an active booking"*).
    - Court is already occupied (*"This court is currently occupied"*).

---

### 3.2 Get Current User's Booking History
Fetches all historical and current bookings belonging to the authenticated student.

- **URL**: `/api/bookings/mine`
- **Method**: `GET`
- **Headers**: Cookie (`JSESSIONID`)
- **Responses**:
  - `200 OK`: Array of bookings ordered by `startTime DESC`.

---

### 3.3 Cancel / Release Active Booking
Cancels an active reservation, releases the court lock immediately, and marks status as `CANCELLED`.

- **URL**: `/api/bookings/{bookingId}`
- **Method**: `DELETE`
- **Headers**: Cookie (`JSESSIONID`)
- **Responses**:
  - `200 OK`: Updated booking record with `status: "CANCELLED"`.
  - `404 Not Found`: Booking ID not found or does not belong to student.
  - `409 Conflict`: Booking is already finished or cancelled.

---

## 4. Health & Status Endpoints (`/api/health`)

### 4.1 System Health Probe
Returns service status and active database engine.

- **URL**: `/api/health`
- **Method**: `GET`
- **Responses**:
  - `200 OK`:
  ```json
  {
    "status": "UP",
    "database": "MySQL 8.0.46",
    "timestamp": "2026-11-17T15:00:00Z"
  }
  ```
