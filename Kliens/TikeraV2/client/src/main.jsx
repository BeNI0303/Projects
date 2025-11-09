import { StrictMode } from 'react'
import { createRoot } from 'react-dom/client'
import { BrowserRouter, Routes, Route } from 'react-router-dom'
import { Provider } from 'react-redux'
import { store } from './state/store'
import './index.css'
import App from './App.jsx'
import ReservationProgressProvider from './state/reservationProgress.jsx'
import Login from './view/Login.jsx'
import Register from './view/Register.jsx'
import MovieCreate from './view/MovieCreate.jsx'
import ScreeningCreate from './view/ScreeningCreate.jsx'
import MovieUpdate from './view/MovieUpdate.jsx'
import ScreeningUpdate from './view/ScreeningUpdate.jsx'
import Bookings from './view/Bookings.jsx'
import { ToastProvider } from "./view/ToastProvider.jsx"

createRoot(document.getElementById('root')).render(
  <StrictMode>
    <Provider store={store}>
      <ToastProvider>
        <ReservationProgressProvider>
          <BrowserRouter>
            <Routes>
              <Route path="/" element={<App />} />
              <Route path="/login" element={<Login />} />
              <Route path="/register" element={<Register />} />
              <Route path="/moviecreate" element={<MovieCreate />} />
              <Route path="/screeningcreate" element={<ScreeningCreate />} />
              <Route path="/movieupdate" element={<MovieUpdate />} />
              <Route path="/screeningupdate" element={<ScreeningUpdate />} />
              <Route path="/bookings" element={<Bookings />} />
            </Routes>
          </BrowserRouter>
        </ReservationProgressProvider>
      </ToastProvider>
    </Provider>
  </StrictMode>,
)