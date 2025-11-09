import React, { useContext } from 'react';
import styles from "./TicketSelector.module.css";
import { ReservationProgressContext } from '../state/reservationProgress.jsx';

function TicketSelector() {
  const {tickets, updateTicket, selectedSeats, totalPrice, totalTickets} = useContext(ReservationProgressContext);

    return (
      <div className={styles.ticketcontainer}>
        <div className={styles.tickettypes}>
          <div className={styles.ticket}>
            <p>Diák (2000 Ft)</p>
            <button onClick={() =>updateTicket(-1,"student")}>-</button>
            <span>{tickets.student}</span>
            <button onClick={() =>updateTicket(1,"student")}>+</button>
          </div>
          <div className={styles.ticket}>
            <p>Felnőtt (2500 Ft)</p>
            <button onClick={() =>updateTicket(-1,"adult")}>-</button>
            <span>{tickets.adult}</span>
            <button onClick={() =>updateTicket(1,"adult")}>+</button>
          </div>
          <div className={styles.ticket}>
            <p>Nyugdíjas (1800 Ft)</p>
            <button onClick={() =>updateTicket(-1,"senior")}>-</button>
            <span>{tickets.senior}</span>
            <button onClick={() =>updateTicket(1,"senior")}>+</button>
          </div>
        </div>
        <div>
          <p>Összesen: <span>{totalPrice}</span> Ft</p>
        </div>
        <div>
          <p>Válaszd ki az ülőhelyeket! <span>{selectedSeats.length}</span>/{totalTickets} kiválasztva</p>
          <div>
          </div>
        </div>
      </div>
    )
}

export default TicketSelector;