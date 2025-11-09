import React, { useContext } from 'react';
import './App.css'
import MovieCard from './view/MovieCard'
import DaySelector from './view/DaySelector';
import { ReservationProgressContext } from './state/reservationProgress.jsx';
import MovieDetails from './view/MovieDetails.jsx';
import Navbar from './view/Navbar.jsx';

function App() {
  const { movies, selectedDay, selectedMovie,
    handleSelectMovie, handleSelectedDay, filterScreeningsByDayAndWeek, filterMovieByDayAndWeek, currentWeek, setCurrentWeek
  } = useContext(ReservationProgressContext);

  const days = ["Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"];

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
      <div>
        <Navbar/>
      </div>
      <div className='grid'>
        <div className='grid-item1'>
          <div className="day-selector">
            {days.length === 0 ? (
              <p>Loading...</p>
            ) : (
              days.map((day, index) => (
                <div key={day} className={day === selectedDay ? "navbar-button selected" : "navbar-button"}>
                  <DaySelector key={`day-selector-${index}`} day={day} onClick={() => handleSelectedDay(day)} />
                </div>
              ))
            )}
          </div>
          <div className='selectedDay'>
            {selectedDay != null ? (
              <p>{daysHU[selectedDay]}</p>
            ) : (
              <p>Nincs kiválasztott nap</p>
            )}
          </div>
          <div className='weekSelection'>
            <div className='weekSelector'>
              <button onClick={() => setCurrentWeek(currentWeek <= 1 ? currentWeek : currentWeek - 1)}>&lt;</button>
              <p>{currentWeek}</p>
              <button onClick={() => setCurrentWeek(currentWeek + 1)}>&gt;</button>
            </div>
          </div>
          <div className="movieCards">
            {filterMovieByDayAndWeek(movies, selectedDay).length <= 0 ? (
              <p>Nincs elérhető</p>
            ) : (
              filterMovieByDayAndWeek(movies, selectedDay).map((movie, index) => (
                <MovieCard key={`${movie.id}-${index}`} url={movie.image_path} title={movie.title} genre={movie.genre} duration={movie.duration} onClick={() => handleSelectMovie(movie)} />
              ))
            )}
          </div>
        </div>
        <div className='movieDetails'>
          <div className='movieHeader'>
            {selectedMovie ? (
              <MovieDetails key={selectedMovie.id} url={selectedMovie.image_path} title={selectedMovie.title} release={selectedMovie.release_year} desctription={selectedMovie.description} times={filterScreeningsByDayAndWeek(selectedMovie.screenings, selectedDay)} onClick={null} />
            ) : (
              <p>Kattints egy filmre a részletekért!</p>
            )}
          </div>
        </div>
      </div>
    </div>
  );
}

export default App
