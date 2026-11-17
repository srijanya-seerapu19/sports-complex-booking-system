import React from 'react'

export default function SportsPassModal({ sportsPass, student, onClose, formatDateTime, sportIcon }) {
  if (!sportsPass) return null

  return (
    <div className="modal-backdrop" onClick={onClose}>
      <div
        className="modal-card sports-pass-modal"
        onClick={(e) => e.stopPropagation()}
        role="dialog"
        aria-modal="true"
      >
        <div className="sports-pass-header">
          <span className="sports-pass-badge">Official Campus Pass</span>
          <h3>Anurag University Athletic Pass</h3>
          <p>Department of Physical Education & Sports</p>
        </div>

        <div className="sports-pass-body">
          <div className="sports-pass-row">
            <div>
              <small>Student Player</small>
              <b>{student || sportsPass.studentEmail || 'Registered Student'}</b>
            </div>
            <div style={{ textAlign: 'right' }}>
              <small>Sport / Activity</small>
              <b>{sportIcon ? sportIcon(sportsPass.sport) : ''} {sportsPass.sport}</b>
            </div>
          </div>

          <div className="sports-pass-row">
            <div>
              <small>Reserved Court</small>
              <b>{sportsPass.name || sportsPass.courtName}</b>
            </div>
            <div style={{ textAlign: 'right' }}>
              <small>Campus Ground</small>
              <b>{sportsPass.place || 'Sports Complex'}</b>
            </div>
          </div>

          <div className="sports-pass-row">
            <div>
              <small>Session Starts</small>
              <b>{formatDateTime(sportsPass.startTime || sportsPass.booking?.started)}</b>
            </div>
            <div style={{ textAlign: 'right' }}>
              <small>Session Ends</small>
              <b>{formatDateTime(sportsPass.endTime || sportsPass.booking?.until)}</b>
            </div>
          </div>

          <div className="barcode-box">
            <div className="barcode-lines" aria-hidden="true">
              {[3, 1, 4, 2, 5, 2, 1, 4, 3, 2, 5, 1, 3, 2, 4, 1, 3, 5, 2, 4, 1, 3, 2, 4].map(
                (w, idx) => (
                  <div
                    key={idx}
                    className="barcode-line"
                    style={{ width: `${w}px` }}
                  />
                ),
              )}
            </div>
            <span className="barcode-code">
              {sportsPass.bookingCode || sportsPass.booking?.bookingCode}
            </span>
            <p style={{ margin: '6px 0 0', fontSize: '11px', color: 'var(--muted)' }}>
              Show this pass at the court reception or gate check.
            </p>
          </div>
        </div>

        <div className="sports-pass-footer">
          <button
            type="button"
            className="cancel-button"
            onClick={onClose}
          >
            Close Pass
          </button>
          <button
            type="button"
            className="confirm-button"
            onClick={() => window.print()}
          >
            🖨 Print / Save
          </button>
        </div>
      </div>
    </div>
  )
}
