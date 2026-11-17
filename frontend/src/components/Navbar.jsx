import React from 'react'

export default function Navbar({ student, onLoginClick, onRegisterClick, onLogoutClick }) {
  return (
    <header className="header">
      <div className="breadcrumb">
        <span>Anurag University</span>
        <span className="sep">/</span>
        <span className="current">Dashboard</span>
      </div>

      <div className="account-bar">
        {student ? (
          <>
            <div className="user-badge">
              <span className="user-dot">●</span>
              <span className="user-name">{student}</span>
            </div>
            <button className="signout-button" onClick={onLogoutClick}>
              Sign out
            </button>
          </>
        ) : (
          <>
            <button className="login-button" onClick={onLoginClick}>
              Student Login
            </button>
            <button className="login-button" onClick={onRegisterClick}>
              Register
            </button>
          </>
        )}
      </div>
    </header>
  )
}
