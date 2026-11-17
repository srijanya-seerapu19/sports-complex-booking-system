import React from 'react'

export default function ConfirmModal({ modal, onClose, sportIcon }) {
  if (!modal) return null

  return (
    <div className="modal-backdrop" onClick={onClose}>
      <div
        className="modal-card confirm-modal"
        onClick={(e) => e.stopPropagation()}
        role="dialog"
        aria-modal="true"
      >
        <button
          className="modal-close"
          type="button"
          onClick={onClose}
          aria-label="Close modal"
        >
          ×
        </button>

        <span className="modal-tag">Confirmation</span>
        <h3>{modal.title}</h3>
        <p className="modal-copy">{modal.message}</p>

        {modal.court && (
          <div className="confirm-court-badge">
            <span className="confirm-court-icon">
              {sportIcon ? sportIcon(modal.court.sport) : '🏟'}
            </span>
            <div>
              <strong>{modal.court.name}</strong>
              <small>{modal.court.place || modal.court.location || modal.court.sport}</small>
            </div>
            {modal.duration && (
              <span className="confirm-duration-tag">
                {modal.duration} mins
              </span>
            )}
          </div>
        )}

        <div className="confirm-actions">
          <button
            type="button"
            className="cancel-button"
            onClick={onClose}
          >
            {modal.cancelText || 'Cancel'}
          </button>
          <button
            type="button"
            className={`confirm-button ${modal.isDanger ? 'danger-button' : ''}`}
            onClick={modal.onConfirm}
          >
            {modal.confirmText || 'Confirm'}
          </button>
        </div>
      </div>
    </div>
  )
}
