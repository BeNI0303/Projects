import { StrictMode } from 'react'
import { createRoot } from 'react-dom/client'
import './index.css'
import App from './App.jsx'
import ReservationProgressProvider from './state/reservationProgress.jsx'

createRoot(document.getElementById('root')).render(
  <StrictMode>
    <ReservationProgressProvider>
      <App/>
    </ReservationProgressProvider>
  </StrictMode>,
)
