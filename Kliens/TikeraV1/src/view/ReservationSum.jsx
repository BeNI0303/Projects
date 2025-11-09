import React, { useContext } from 'react';
import styles from "./ReservationSum.module.css";
import { ReservationProgressContext } from '../state/reservationProgress.jsx';
import Modal from './Modal.jsx';

function ReservationSum() {
  const {selectedSeats, selectedMovie, selectedDay, tickets,ticketPrices, completeReservation, isModalOpen, setIsModalOpen
  } = useContext(ReservationProgressContext);

  let ticketsRender = []

  if(tickets.student > 0){
    ticketsRender.push(<p key="student-ticket">{tickets.student}x Diák <span className={styles.ticketPrices}>{tickets.student*ticketPrices.student} Ft</span></p>)
  }
  if(tickets.adult > 0){
    ticketsRender.push(<p key="adult-ticket">{tickets.adult}x Felnőtt <span className={styles.ticketPrices}>{tickets.adult*ticketPrices.adult} Ft</span></p>)
  }
  if(tickets.senior > 0){
    ticketsRender.push(<p key="senior-ticket">{tickets.senior}x Nyugdíjas <span className={styles.ticketPrices}>{tickets.senior*ticketPrices.senior} Ft</span></p>)
  }

  return (
    <div className={styles.bookingSummary}>
      <div className={styles.movieDeatils}>
        <h2 className={styles.title}>{selectedMovie.title}</h2>
        <p>{selectedDay}</p>
        <div>
          {ticketsRender}
        </div>
        <hr className={styles.hr}/>
        <div>
          <p>Helyek</p>
          <p>
            {selectedSeats.map((seat, index) =>
              `${seat.row}. sor ${seat.seat}. szék${index < selectedSeats.length - 1 ? ", " : ""}`
            )}
          </p>
        </div>
        <hr className={styles.hr}/>
      </div>
      <div className={styles.button}>
        <button className={styles.finalizeButton} onClick={() => completeReservation()}>Foglalás véglegesítése</button>
        <Modal isOpen={isModalOpen} onClose={() => setIsModalOpen(false)}>
          <h3>Sikeres foglalás!</h3>
          <p>A foglalásodat véglegesítettük.</p>
        </Modal>
      </div>
    </div>
  );
}

export default ReservationSum;
