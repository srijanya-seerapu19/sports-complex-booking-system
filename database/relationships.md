# Database Entity Relationships & Integrity Rules

## 1. Relational Mapping & Cardinality

### A. Students to Bookings (`1 : N`)
- **Relationship**: One student can have zero, one, or many historical bookings over time.
- **Fair Play Business Constraint**: At any single wall-clock moment, a student can possess at most **one** booking whose status is `ACTIVE` and whose `end_time > NOW()`.
- **Foreign Key**: `bookings.student_id` references `students.id`.
- **Cascade Behavior**: `ON DELETE CASCADE` (deleting a student purges associated bookings, preventing orphan references).

### B. Courts to Bookings (`1 : N`)
- **Relationship**: One court can host multiple non-overlapping bookings over time.
- **Physical Collision Rule**: At any single moment, a court can be linked to at most **one** booking where `status = 'ACTIVE'` and `end_time > NOW()`.
- **Foreign Key**: `bookings.court_id` references `courts.id`.
- **Cascade Behavior**: `ON DELETE CASCADE`.

---

## 2. Integrity & Check Constraints

1. **Duration Bounds**:
   ```sql
   CONSTRAINT chk_duration CHECK (duration_minutes BETWEEN 15 AND 60)
   ```
   *Purpose*: Guarantees students cannot book sessions exceeding 60 minutes or shorter than 15 minutes.

2. **Status Enum Emulation**:
   - `ACTIVE`: Session currently running or booked for immediate use.
   - `COMPLETED`: Session reached `end_time` and was automatically completed by `BookingExpirationScheduler`.
   - `CANCELLED`: Session was terminated early by the student or administrator.

3. **Booking Code Uniqueness**:
   ```sql
   CONSTRAINT uq_booking_code UNIQUE (booking_code)
   ```
   *Purpose*: Each booking receives a distinct verification token (e.g. `AU-BADM-9482`) safe for gate verification and barcode generation.
