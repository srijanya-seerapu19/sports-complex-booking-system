import React from 'react'

export default function ActiveSession({ myBooking, now, getRemaining, formatClock, onCancel, onViewPass, sportIcon }) {
  if (!myBooking) return null

  const elapsed = now - myBooking.booking.started
  const totalDuration = Math.max(1, myBooking.booking.until - myBooking.booking.started)
  const progressPct = Math.min(100, Math.max(0, Math.round((elapsed / totalDuration) * 100)))

  return (
    <section className="session" id="session">
      <div className="session-copy">
        <div className="session-header-badge">
          <span className="live-dot">●</span>
          <p className="kicker" style={{ margin: 0 }}>YOUR SESSION IS LIVE</p>
        </div>
        <h2>{sportIcon(myBooking.sport)} {myBooking.sport} · {myBooking.name}</h2>
        <p className="session-place">{myBooking.place}</p>
        
        <div className="session-tags">
          <span className="booking-code-tag">ID: {myBooking.booking.bookingCode}</span>
          <button
            type="button"
            className="pass-badge-btn"
            onClick={onViewPass}
          >
            🎫 View Sports Pass
          </button>
        </div>

        <div className="session-times">
          <span>
            <small>STARTED</small>
            <b>{formatClock(myBooking.booking.started)}</b>
          </span>
          <span>
            <small>SCHEDULED END</small>
            <b>{formatClock(myBooking.booking.until)}</b>
          </span>
        </div>
      </div>

      <div className="countdown-wrap">
        <div className="countdown">
          <small>TIME REMAINING</small>
          <strong>{getRemaining(myBooking.booking.until, now)}</strong>
        </div>
        <div className="progress-track" title="Session progress">
          <div
            className="progress-fill"
            style={{ width: `${progressPct}%` }}
          />
        </div>
      </div>

      <button
        type="button"
        className="end-button"
        onClick={onCancel}
      >
        Cancel / Release Court
      </button>
    </section>
  )
}
