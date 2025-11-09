import { createContext, useContext, useState } from "react";
import moviesData from '../assets/movies.json';

export const ReservationProgressContext = createContext();

function ReservationProgressProvider({ children }) {
  const [movies] = useState(moviesData);
  const [selectedMovie, setSelectedMovie] = useState(null);
  const [selectedDay, setSelectedDay] = useState(null);
  const [selectedTime, setSelectedTime] = useState(null);
  const [selectedId, setSelectedID] = useState(null);
  const [isModalOpen, setIsModalOpen] = useState(false);

  const [tickets, setTickets] = useState({
    student: 0,
    adult: 0,
    senior: 0,
  });
  const [totalPrice, setTotalPrice] = useState(0);
  const [selectedSeats, setSelectedSeats] = useState([]);

  const ticketPrices = {
    student: 2000,
    adult: 2500,
    senior: 1800,
  };

  const totalTickets = Object.values(tickets).reduce((sum, count) => sum + count, 0);


  const handleSelectMovie = (movie) => {
    setSelectedMovie(movie);
  };

  const handleSelectedDay = (day) => {
    setSelectedDay(day);
  };

  const handleSelectedTime = (time, id) => {
    setSelectedID(id)
    setSelectedTime(time)
  };



  const completeReservation = () => {
    movies.map((movie) => {
      if (movie.id = selectedMovie.id) {
        movie.screenings.map((screening) => {
          if (screening.id === selectedId) {
            screening.bookings = [...screening.bookings, ...selectedSeats]
          }
        })
      }
    })
    setIsModalOpen(true);
    setSelectedSeats([]);
    setTickets({
      student: 0,
      adult: 0,
      senior: 0
    })
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
        setSelectedSeats((prevSelectedSeats) => [...prevSelectedSeats, newSeat]);
      } else {
        window.alert("Kiválasztott jegyszámnál többet nem tudsz foglalni!");
      }
    }
  };

  const updateTicket = (number, ticketType) => {
    if(number <= 0){
      setSelectedSeats([]);
    }
    if (ticketType == "student") {
      if (tickets.student <= 0 && number <= 0) {
        return
      }
      setTotalPrice(totalPrice + number * ticketPrices.student)
      setTickets({
        student: tickets.student + number,
        adult: tickets.adult,
        senior: tickets.senior
      })
    } else if (ticketType == "adult") {
      if (tickets.adult <= 0 && number <= 0) {
        return
      }
      setTotalPrice(totalPrice + number * ticketPrices.adult)
      setTickets({
        student: tickets.student,
        adult: tickets.adult + number,
        senior: tickets.senior
      })
    } else {
      if (tickets.senior <= 0 && number <= 0) {
        return
      }
      setTotalPrice(totalPrice + number * ticketPrices.senior)
      setTickets({
        student: tickets.student,
        adult: tickets.adult,
        senior: tickets.senior + number
      })
    }
  }

  const filterScreeningsByDay = (screenings, selectedDay) => {
    if (selectedDay === null) {
      return [];
    }
    return screenings.filter(screening => screening.weekday.toLowerCase() === selectedDay.toLowerCase())
  };

  const filterMovieByDay = (movies, selectedDay) => {
    if (selectedDay === null) {
      return [];
    }

    return movies.filter(movie => movie.screenings.some(screening => screening.weekday.toLowerCase() === selectedDay.toLowerCase()))
  };

  const filterSeatsByTime = (selectedMovie) => {
    if (selectedId === null || selectedMovie === null) {
      return [];
    }

    return selectedMovie.screenings.filter(screening => screening.id === selectedId)
  };

  const context = {
    movies, selectedDay, selectedMovie, selectedTime, selectedSeats, selectedId, tickets, totalPrice, totalTickets, ticketPrices, isModalOpen
    , handleSelectMovie, handleSelectedDay, handleSelectedTime, filterScreeningsByDay, filterMovieByDay, filterSeatsByTime, updateTicket, handleSelectedSeat, completeReservation, setIsModalOpen
  };
  return <ReservationProgressContext.Provider value={context}>{children}</ReservationProgressContext.Provider>
}

export default ReservationProgressProvider;

export const useGameProgress = () => useContext(ReservationProgressContext);