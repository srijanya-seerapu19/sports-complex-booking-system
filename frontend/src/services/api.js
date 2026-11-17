import API from '../config'

export async function readResponse(response) {
  const text = await response.text()
  if (!text) return null
  try {
    return JSON.parse(text)
  } catch {
    return { message: text }
  }
}

export const authApi = {
  login: async (email, password) => {
    const res = await fetch(`${API}/auth/login`, {
      method: 'POST',
      credentials: 'include',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ email, password }),
    })
    const data = await readResponse(res)
    if (!res.ok) throw new Error(data?.message || 'Login failed')
    return data
  },

  register: async (fullName, email, password) => {
    const res = await fetch(`${API}/auth/register`, {
      method: 'POST',
      credentials: 'include',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ fullName, email, password }),
    })
    const data = await readResponse(res)
    if (!res.ok) throw new Error(data?.message || 'Registration failed')
    return data
  },

  me: async () => {
    const res = await fetch(`${API}/auth/me`, { credentials: 'include' })
    if (!res.ok) return null
    return readResponse(res)
  },

  logout: async () => {
    return fetch(`${API}/auth/logout`, {
      method: 'POST',
      credentials: 'include',
    })
  },
}

export const courtApi = {
  getAll: async () => {
    const res = await fetch(`${API}/courts`)
    const data = await readResponse(res)
    if (!res.ok) throw new Error(data?.message || 'Failed to fetch courts')
    return data
  },
}

export const bookingApi = {
  create: async (courtId, durationMinutes) => {
    const res = await fetch(`${API}/bookings`, {
      method: 'POST',
      credentials: 'include',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ courtId, durationMinutes }),
    })
    const data = await readResponse(res)
    if (!res.ok) {
      if (res.status === 409) {
        throw new Error(data?.message || 'Active session conflict. Only 1 active booking allowed.')
      }
      throw new Error(data?.message || 'Could not create booking')
    }
    return data
  },

  getMyBookings: async () => {
    const res = await fetch(`${API}/bookings/mine`, { credentials: 'include' })
    const data = await readResponse(res)
    if (!res.ok) throw new Error(data?.message || 'Failed to fetch bookings')
    return data
  },

  cancel: async (bookingId) => {
    const res = await fetch(`${API}/bookings/${bookingId}`, {
      method: 'DELETE',
      credentials: 'include',
    })
    const data = await readResponse(res)
    if (!res.ok) throw new Error(data?.message || 'Could not cancel booking')
    return data
  },
}

export default { auth: authApi, courts: courtApi, bookings: bookingApi }
