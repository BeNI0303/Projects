import { createContext, useContext, useState, useEffect } from "react";
import { useDispatch, useSelector } from "react-redux";
import {
  setSelectedMovie,
  setMovies,
  setSelectedDay,
  setSelectedTime,
  setSelectedId,
  setIsModalOpen,
  setCurrentWeek,
  setIsLoggedIn,
  setUser,
  setIsAdmin,
  setTickets,
  setTotalPrice,
  setSelectedSeats,
} from "./reservationProgressSlice";
import { useToast } from "../view/ToastProvider";

export const ReservationProgressContext = createContext();

function ReservationProgressProvider({ children }) {
  const dispatch = useDispatch();
  const toast = useToast();

  const movies = useSelector(state => state.reservationProgress.movies);
  const loading = useSelector(state => state.reservationProgress.loading);
  const selectedMovie = useSelector(state => state.reservationProgress.selectedMovie);
  const selectedDay = useSelector(state => state.reservationProgress.selectedDay);
  const selectedTime = useSelector(state => state.reservationProgress.selectedTime);
  const selectedId = useSelector(state => state.reservationProgress.selectedId);
  const isModalOpen = useSelector(state => state.reservationProgress.isModalOpen);
  const currentWeek = useSelector(state => state.reservationProgress.currentWeek);
  const isLoggedIn = useSelector(state => state.reservationProgress.isLoggedIn);
  const user = useSelector(state => state.reservationProgress.user);
  const isAdmin = useSelector(state => state.reservationProgress.isAdmin);
  const tickets = useSelector(state => state.reservationProgress.tickets);
  const totalPrice = useSelector(state => state.reservationProgress.totalPrice);
  const selectedSeats = useSelector(state => state.reservationProgress.selectedSeats);

  const weekdays = {
    Monday: 1,
    Tuesday: 2,
    Wednesday: 3,
    Thursday: 4,
    Friday: 5,
    Saturday: 6,
    Sunday: 7
  };


  const ticketPrices = {
    student: 2000,
    adult: 2500,
    senior: 1800,
  };

  const totalTickets = Object.values(tickets).reduce((sum, count) => sum + count, 0);

  useEffect(() => {
    const fetchMovies = async () => {
      try {
        const response = await fetch(`${import.meta.env.VITE_API_URL}/movies`);
        if (!response.ok) {
          throw new Error(`HTTP error! status: ${response.status}`);
        }
        const data = await response.json();

        dispatch(setMovies(data.data));
      } catch (error) {
        console.error("Hiba a filmek lekérésekor:", error);
      }
    };
    fetchMovies();
  }, [dispatch]);

  useEffect(() => {
    const token = localStorage.getItem("token");
    const userStr = localStorage.getItem("user");
    const isAdminStr = localStorage.getItem("isAdmin");
    if (token && isAdminStr) {
      dispatch(setIsAdmin(true));
    }
    if (token && userStr) {
      dispatch(setIsLoggedIn(true));
      dispatch(setUser(JSON.parse(userStr)));
    }
  }, [dispatch]);

  const handleSelectMovie = (movie) => {
    dispatch(setSelectedMovie(movie));
    dispatch(setSelectedSeats([]));
    dispatch(setTickets({ student: 0, adult: 0, senior: 0 }));
    dispatch(setSelectedTime(null));
    dispatch(setSelectedId(null))
  };

  const handleSelectedDay = (day) => {
    dispatch(setSelectedDay(day));
    dispatch(setSelectedSeats([]));
    dispatch(setTickets({ student: 0, adult: 0, senior: 0 }));
    dispatch(setSelectedTime(null));
    dispatch(setSelectedId(null))
  };

  const handleSelectedTime = (time, id) => {
    dispatch(setSelectedId(id));
    dispatch(setSelectedTime(time));
    dispatch(setSelectedSeats([]));
    dispatch(setTickets({ student: 0, adult: 0, senior: 0 }));
  };



  const completeReservation = async () => {
    try {
      const token = localStorage.getItem("token");
      if (!token) {
        toast("Kérlek jelentkezz be a foglalás véglegesítéséhez!", "error", 2000,() => null)
        return false;
      }
      if (isAdmin) {
        toast("Adminisztrátorként nem tudsz foglalni!", "error", 2000,() => null)
        return false;
      }

      const response = await fetch(`${import.meta.env.VITE_API_URL}/bookings`, {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
          "Authorization": `Bearer ${token}`
        },
        body: JSON.stringify({
          screening_id: selectedId,
          seats: selectedSeats.map(seat => ({
            row: seat.row,
            number: seat.seat
          })),
          ticket_types: [
            { type: "normal", quantity: tickets['adult'] },
            { type: "student", quantity: tickets['student'] },
            { type: "senior", quantity: tickets['senior'] }
          ].filter(t => t.quantity > 0),
        })
      });

      if (!response.ok) {
        const data = await response.json();
        toast("Foglalás sikertelen: " + (data.message || JSON.stringify(data)), "error", 2000,() => null)
        return false;
      }

      dispatch(setSelectedSeats([]));
      dispatch(setTickets({ student: 0, adult: 0, senior: 0 }));
      dispatch(setTotalPrice(0));
      dispatch(setIsModalOpen(false));
      toast("Foglalás sikeresen véglegesítve!", "success", 2000,() => null)
      return true;
    } catch (error) {
      toast("Hiba történt a foglalás véglegesítésekor: " + error.message, "error", 2000,() => null)
      return false;
    }
  };

  const handleSelectedSeat = (foglalte, row, seat) => {
    if (foglalte === "foglalt") {
      window.alert("Ez a szék már foglalt ide nem tudsz foglalni!");
    } else if (foglalte === "szabad") {
      if ((tickets.student + tickets.adult + tickets.senior) > selectedSeats.length) {
        let newSeat = {
          "row": row,
          "seat": seat
        }
        dispatch(setSelectedSeats([...selectedSeats, newSeat]));
      } else {
        window.alert("Kiválasztott jegyszámnál többet nem tudsz foglalni!");
      }
    }
  };

  const handleLogout = () => {
    dispatch(setIsAdmin(false));
    dispatch(setIsLoggedIn(false));
    dispatch(setUser(null));
    localStorage.removeItem("token");
    localStorage.removeItem("user");
    localStorage.removeItem("isAdmin");
    toast("Sikeres kijelentkezés!", "success", 2000,() => window.location.href = "/")
  };

  const handleLogin = (user) => {
    dispatch(setIsLoggedIn(true));
    dispatch(setUser(user));
    localStorage.setItem("user", JSON.stringify(user));
  };

  const updateTicket = (number, ticketType) => {
    if (number <= 0) {
      dispatch(setSelectedSeats([]));
    }
    let newTickets = { ...tickets };
    let newTotalPrice = totalPrice;
    if (ticketType === "student") {
      if (tickets.student <= 0 && number <= 0) return;
      newTotalPrice += number * ticketPrices.student;
      newTickets.student += number;
    } else if (ticketType === "adult") {
      if (tickets.adult <= 0 && number <= 0) return;
      newTotalPrice += number * ticketPrices.adult;
      newTickets.adult += number;
    } else {
      if (tickets.senior <= 0 && number <= 0) return;
      newTotalPrice += number * ticketPrices.senior;
      newTickets.senior += number;
    }
    dispatch(setTotalPrice(newTotalPrice));
    dispatch(setTickets(newTickets));
  };

  const createMovie = async (movieData) => {
    const isDuplicate = movies.some(
      m => m.title === movieData.title && m.release_year === movieData.release_year
    );
    if (isDuplicate) {
      toast("Ez a film már létezik!", "error", 2000,() => null)
      return false;
    }
    try {
      const token = localStorage.getItem("token");
      const response = await fetch(`${import.meta.env.VITE_API_URL}/movies`, {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
          "Authorization": `Bearer ${token}`
        },
        body: JSON.stringify(movieData)
      });
      if (!response.ok) {
        const data = await response.json();
        toast("Film hozzáadása sikertelen: " + (data.message || JSON.stringify(data)), "error", 2000,() => null)
        return false;
      }
      const moviesResponse = await fetch(`${import.meta.env.VITE_API_URL}/movies`);
      const moviesData = await moviesResponse.json();
      dispatch(setMovies(moviesData.data));
      toast("Film sikeresen hozzáadva!", "success", 2000,() => window.location.href = "/")
      return true;
    } catch (err) {
      toast("Hiba történt a film hozzáadásakor: " + err.message, "error", 2000,() => null)
      return false;
    }
  };

  const updateMovie = async (movieData, movieId) => {
    try {
      const token = localStorage.getItem("token");
      const response = await fetch(`${import.meta.env.VITE_API_URL}/movies/${movieId}`, {
        method: "PUT",
        headers: {
          "Content-Type": "application/json",
          "Authorization": `Bearer ${token}`
        },
        body: JSON.stringify(movieData)
      });
      if (!response.ok) {
        const data = await response.json();
        toast("Film frissítése sikertelen: " + (data.message || JSON.stringify(data)), "success", 2000,() => null)
        return false;
      }
      const moviesResponse = await fetch(`${import.meta.env.VITE_API_URL}/movies`);
      const moviesData = await moviesResponse.json();
      dispatch(setMovies(moviesData.data));
      toast("Film sikeresen frissítve!", "success", 2000,() => null)
      return true;
    } catch (err) {
      toast("Hiba történt a film frissítésekor: " + err.message, "error", 2000,() => null)
      return false;
    }
  };

  const deleteMovie = async (movieId) => {
    try {
      const token = localStorage.getItem("token");
      const response = await fetch(`${import.meta.env.VITE_API_URL}/movies/${movieId}`, {
        method: "DELETE",
        headers: {
          "Content-Type": "application/json",
          "Authorization": `Bearer ${token}`
        }
      });
      if (!response.ok) {
        const data = await response.json();
        toast("Film törlése sikertelen: " + (data.message || JSON.stringify(data)), "error", 2000,() => null)
        return false;;
      }
      const moviesResponse = await fetch(`${import.meta.env.VITE_API_URL}/movies`);
      const moviesData = await moviesResponse.json();
      dispatch(setMovies(moviesData.data));
      toast("Film sikeresen törölve!", "success", 2000,() => null)
      return true;
    } catch (err) {
      toast("Hiba történt a film törlésekor: " + err.message, "error", 2000,() => null)
      return false;
    }
  };

  const createScreening = async (screeningData) => {
    try {
      const token = localStorage.getItem("token");
      const response = await fetch(`${import.meta.env.VITE_API_URL}/screenings`, {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
          "Authorization": `Bearer ${token}`
        },
        body: JSON.stringify(screeningData)
      });
      if (!response.ok) {
        const data = await response.json();
        toast("Vetítés hozzáadása sikertelen: " + (data.message || JSON.stringify(data)), "error", 2000,() => null)
        return false;
      }
      const moviesResponse = await fetch(`${import.meta.env.VITE_API_URL}/movies`);
      const moviesData = await moviesResponse.json();
      dispatch(setMovies(moviesData.data));
      toast("Vetítés sikeresen hozzáadva!", "success", 2000,() => null)
      return true;
    } catch (err) {
      toast("Hiba történt a vetítés hozzáadásakor: " + err.message, "success", 2000,() => null)
      return false;
    }
  };

  const updateScreening = async (screeningData, screeningId) => {
  try {
    const token = localStorage.getItem("token");
    const response = await fetch(`${import.meta.env.VITE_API_URL}/screenings/${screeningId}`, {
      method: "PUT",
      headers: {
        "Content-Type": "application/json",
        "Authorization": `Bearer ${token}`
      },
      body: JSON.stringify(screeningData)
    });
    if (!response.ok) {
      const data = await response.json();
      toast("Vetítés frissítése sikertelen: " + (data.message || JSON.stringify(data)), "error", 2000, () => null);
      return false;
    }
    const moviesResponse = await fetch(`${import.meta.env.VITE_API_URL}/movies`);
    const moviesData = await moviesResponse.json();
    dispatch(setMovies(moviesData.data));
    toast("Vetítés sikeresen frissítve!", "success", 2000, () => null);
    return true;
  } catch (err) {
    toast("Hiba történt a vetítés frissítésekor: " + err.message, "error", 2000, () => null);
    return false;
  }
};

const deleteScreening = async (screeningId) => {
  try {
    const token = localStorage.getItem("token");
    const response = await fetch(`${import.meta.env.VITE_API_URL}/screenings/${screeningId}`, {
      method: "DELETE",
      headers: {
        "Content-Type": "application/json",
        "Authorization": `Bearer ${token}`
      }
    });
    if (!response.ok) {
      const data = await response.json();
      toast("Vetítés törlése sikertelen: " + (data.message || JSON.stringify(data)), "error", 2000, () => null);
      return false;
    }
    const moviesResponse = await fetch(`${import.meta.env.VITE_API_URL}/movies`);
    const moviesData = await moviesResponse.json();
    dispatch(setMovies(moviesData.data));
    toast("Vetítés sikeresen törölve!", "success", 2000, () => null);
    return true;
  } catch (err) {
    toast("Hiba történt a vetítés törlésekor: " + err.message, "error", 2000, () => null);
    return false;
  }
};

  const filterScreeningsByDayAndWeek = (screenings, selectedDay) => {
    if (selectedDay === null) {
      return [];
    }
    return screenings.filter(screening => screening.week_day === weekdays[selectedDay] && screening.week_number === currentWeek);
  };

  const filterMovieByDayAndWeek = (movies, selectedDay) => {
    if (selectedDay === null) {
      return [];
    }

    return movies.filter(movie => movie.screenings.some(screening => ((screening.week_day == selectedDay || screening.week_day === weekdays[selectedDay]) && screening.week_number === currentWeek)));
  };

  const filterSeatsByTime = (selectedMovie) => {
    if (selectedId === null || selectedMovie === null) {
      return [];
    }
    return selectedMovie.screenings.filter(screening => screening.id === selectedId);
  };

  const context = {
    movies,
    loading,
    selectedDay,
    selectedMovie,
    selectedTime,
    selectedSeats,
    selectedId,
    tickets,
    totalPrice,
    totalTickets,
    ticketPrices,
    isModalOpen,
    currentWeek,
    isLoggedIn,
    user,
    isAdmin,
    handleSelectMovie,
    handleSelectedDay,
    handleSelectedTime,
    filterScreeningsByDayAndWeek,
    filterMovieByDayAndWeek,
    filterSeatsByTime,
    updateTicket,
    handleSelectedSeat,
    completeReservation,
    setIsModalOpen: (val) => dispatch(setIsModalOpen(val)),
    setCurrentWeek: (val) => dispatch(setCurrentWeek(val)),
    handleLogin,
    handleLogout,
    setIsAdmin: (val) => dispatch(setIsAdmin(val)),
    createMovie,
    createScreening,
    updateMovie,
    deleteMovie,
    updateScreening,
    deleteScreening
  };
  return <ReservationProgressContext.Provider value={context}>{children}</ReservationProgressContext.Provider>
}

export default ReservationProgressProvider;

export const useGameProgress = () => useContext(ReservationProgressContext);