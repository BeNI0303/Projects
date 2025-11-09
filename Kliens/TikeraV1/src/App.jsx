import React, { useContext } from 'react';
import './App.css'
import MovieCard from './view/MovieCard'
import DaySelector from './view/DaySelector';
import { ReservationProgressContext } from './state/reservationProgress.jsx';
import MovieDetails from './view/MovieDetails.jsx';

function App() {
  const {movies, selectedDay, selectedMovie, selectedTime, selectedTickets, setSelectedSeats, 
    handleSelectMovie, handleSelectedDay, handleSelectedTime, filterScreeningsByDay, filterMovieByDay, filterSeatsByTime
  } = useContext(ReservationProgressContext);

  const days = ["Monday","Tuesday","Wednesday","Thursday","Friday","Saturday","Sunday"];
    
  const daysHU = {
    Monday: "Hétfő",
    Tuesday: "Kedd",
    Wednesday: "Szerda",
    Thursday: "Csütörtök",
    Friday: "Péntek",
    Saturday: "Szombat",
    Sunday: "Vasárnap"
  };

  return (
    <div className='container'>
      <div className="navbar">
        {days.length === 0 ? (
            <p>Loading...</p>
          ) : (
            days.map((day, index) => (
              <div key={day} className={day === selectedDay ? "navbar-button selected" : "navbar-button"}>
                <DaySelector key={`day-selector-${index}`} day={day} onClick={() => handleSelectedDay(day)}/>
              </div>
            ))
          )}
      </div>
      <div className='selectedDay'>
        {selectedDay != null ? (
          <p>{daysHU[selectedDay]}</p>
        ):(
          <p>Nincs kiválasztott nap</p>
        )}
      </div>
      <div className='grid'>
        <div className="movieCards">
          {filterMovieByDay(movies,selectedDay).length <= 0 ? ( 
            <p>Nincs elérhető</p>
          ) : ( 
            filterMovieByDay(movies,selectedDay).map((movie, index) =>(
              <MovieCard key={`${movie.id}-${index}`} url={movie.image} title={movie.title} genre={movie.genre} duration={movie.duration} onClick={() => handleSelectMovie(movie)} />
            ))
          )
          }
        </div>
        <div className='movieDetails'>
          <div className='movieHeader'>
            {selectedMovie ? (
              <MovieDetails key={selectedMovie.id} url={selectedMovie.image} title={selectedMovie.title} release={selectedMovie.release_year} desctription={selectedMovie.description} times={filterScreeningsByDay(selectedMovie.screenings, selectedDay)} onClick={null}/>
            ) : (
              <p>Kattints egy filmre a részletekért!</p>
            )}
          </div>
        </div>
        <div>
        </div>
      </div>
    </div>
  );
}

export default App
