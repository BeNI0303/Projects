import React, { useContext } from "react";
import styles from "./MovieDetails.module.css";
import SeatSelector from './SeatSelector.jsx';
import { ReservationProgressContext } from '../state/reservationProgress.jsx';
import TicketSelector from "./TicketSelector.jsx";
import ReservationSum from "./ReservationSum.jsx";

function MovieDetails({ url, title, release, desctription, times, onClick }) {
  const {selectedDay, selectedMovie, selectedTime, selectedId, handleSelectedTime, filterSeatsByTime
  } = useContext(ReservationProgressContext);
  times.sort((a, b) => {
    const timeA = new Date(`1970-01-01T${a.start_time}:00`);
    const timeB = new Date(`1970-01-01T${b.start_time}:00`);
    return timeA - timeB;
  });
  return (
    <div className={styles.container}>
      <div className={styles.root}>
        <div className={styles.img}>
          <img className={styles.image} width="200" height="200" src={url} alt={title} />
        </div>
        <div className={styles.desctription}>
          <h2>{title}</h2>
          <p className={styles.bolder}>{release}</p>
          <p>{desctription}</p>
          {times.length === 0 ? (
            <p>Nincs elérhető időpont</p>
          ) : (
            <>
              <p>{times[0].date}</p>
              <div className={styles.buttons}>
              {times.map((time) => (
                <div key={time.id} onClick={() => handleSelectedTime(time.start_time, time.id)} className={styles.button}>{time.start_time}</div>
              ))}
            </div>
            </>
          )}
        </div>
      </div>
      <div className={styles.ticketselector}>
            <div className={styles.tickets}>
              <TicketSelector />
            </div>
            <div className={styles.seats}>
              {selectedMovie && filterSeatsByTime(selectedMovie).length > 0 && selectedId ? (
                <SeatSelector key={selectedMovie.id} row={filterSeatsByTime(selectedMovie, selectedTime, selectedDay)[0].room.rows} seatsPerRow={filterSeatsByTime(selectedMovie, selectedTime, selectedDay)[0].room.seatsPerRow} bookings={filterSeatsByTime(selectedMovie, selectedTime, selectedDay)[0].bookings} />
              ) : (
                <p>Kattints egy időpontra a helyfoglalásért</p>
              )}
            </div>
      </div>
      <div>
        <ReservationSum/>
      </div>
    </div>

  )
}

export default MovieDetails;