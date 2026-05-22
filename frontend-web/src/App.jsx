import { Routes, Route, Navigate } from 'react-router-dom'
import NavBar from './components/nav/NavBar'
import AuthHandler from './apis/AuthHandler'

import LandingPage from './pages/LandingPage'
import DashboardPage from './pages/DashboardPage'
import CheckInsPage from './pages/CheckInsPage'
import SignUpPage from './pages/SignUpPage'
import SignInPage from './pages/SignInPage'
import InspirePage from './pages/InspirePage'
import ProgressPage from './pages/ProgressPage'

function RedirectIfAuthenticated({ children }) {
  return AuthHandler.tokenExists() ? <Navigate to="/dashboard" replace /> : children
}

function App() {
  return (
    <>
      <NavBar />
      <Routes>
        <Route path="/" element={<RedirectIfAuthenticated><LandingPage /></RedirectIfAuthenticated>} />
        <Route path="/dashboard" element={<DashboardPage />} />
        <Route path="/checkins" element={<CheckInsPage />} />
        <Route path="/sign-up" element={<RedirectIfAuthenticated><SignUpPage /></RedirectIfAuthenticated>} />
        <Route path="/sign-in" element={<RedirectIfAuthenticated><SignInPage /></RedirectIfAuthenticated>} />
        <Route path="/inspire" element={<InspirePage />} />
        <Route path="/progress" element={<ProgressPage />} />
      </Routes>
    </>
  )
}

export default App
