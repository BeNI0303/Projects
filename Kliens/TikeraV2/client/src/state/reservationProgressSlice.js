import { createSlice } from '@reduxjs/toolkit';

const initialState = {
  selectedSeats: [],
  selectedMovie: null,
  selectedDay: null,
  selectedId: null,
  selectedTime: null,
  tickets: { student: 0, adult: 0, senior: 0 },
  ticketPrices: { student: 2000, adult: 2500, senior: 1800 },
  isModalOpen: false,
  totalPrice: 0,
  movies: [],
  bookings: [],
  user: null,
  screenings: [],
  isAdmin: false,
  isLoggedIn: false,
  currentWeek: 1,
};

const reservationProgressSlice = createSlice({
  name: 'reservationProgress',
  initialState,
  reducers: {
    setSelectedSeats: (state, action) => { state.selectedSeats = action.payload; },
    setSelectedMovie: (state, action) => { state.selectedMovie = action.payload; },
    setSelectedDay: (state, action) => { state.selectedDay = action.payload; },
    setSelectedId: (state, action) => { state.selectedId = action.payload; },
    setSelectedTime: (state, action) => { state.selectedTime = action.payload; },
    setTickets: (state, action) => { state.tickets = action.payload; },
    setCurrentWeek: (state, action) => { state.currentWeek = action.payload; },
    setIsModalOpen: (state, action) => { state.isModalOpen = action.payload; },
    setTotalPrice: (state, action) => { state.totalPrice = action.payload; },
    setMovies: (state, action) => { state.movies = action.payload; },
    setBookings: (state, action) => { state.bookings = action.payload; },
    setUser: (state, action) => { state.user = action.payload; },
    setScreenings: (state, action) => { state.screenings = action.payload; },
    setIsAdmin: (state, action) => { state.isAdmin = action.payload; },
    setIsLoggedIn: (state, action) => { state.isLoggedIn = action.payload; },
  },
});

export const {
  setSelectedSeats,
  setSelectedMovie,
  setSelectedDay,
  setSelectedId,
  setSelectedTime,
  setTickets,
  setCurrentWeek,
  setIsModalOpen,
  setTotalPrice,
  setMovies,
  setBookings,
  setUser,
  setScreenings,
  setIsAdmin,
  setIsLoggedIn
} = reservationProgressSlice.actions;

export default reservationProgressSlice.reducer;