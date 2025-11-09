import React, { useContext } from 'react';
import styles from "./SeatSelector.module.css";
import { ReservationProgressContext } from '../state/reservationProgress.jsx';

function SeatSelector({row, seatsPerRow, bookings}) {

  const isSeatBooked = (bookings, row, seat) => {
    return bookings.some(booking => booking.row === row && booking.seat === seat);
  };
  
  const {handleSelectedSeat,selectedSeats} = useContext(ReservationProgressContext);

  let seatElements = [];
  for (let i = 0; i < row; i++) {
    seatElements.push(<span key={`row-${i+1}`} className={styles.rowNumber}>{i+1}</span>)
    for (let j = 0; j < seatsPerRow; j++) {
      if(isSeatBooked(bookings,i+1,j+1)){
        seatElements.push(
          <img key={`booked-${i+1}-${j+1}`} src="/lefoglalt.png" onClick={()=>handleSelectedSeat("foglalt",i+1,j+1)} alt="" className={styles.szek}/>
        );
      }else if(isSeatBooked(selectedSeats,i+1,j+1)){
        seatElements.push(
          <img  key={`selected-${i+1}-${j+1}`} src="/foglal.png" alt="" className={styles.szek}/>
        );
      }else{
        seatElements.push(
          <img  key={`seat-${i+1}-${j+1}`} src="/szabad.png" onClick={()=>handleSelectedSeat("szabad", i+1, j+1)} alt="" className={styles.szek}/>
        );
      }
    }
    seatElements.push(<br key={`row-break-${i+1}`}/>);
  }

  return (    
    <div className={styles.root}>
      <div className={styles.img}>
      </div>
      <div className={styles.desctription}>
        {row <= 0 || seatsPerRow <= 0 ? (
          <p>Nem jeleníthető meg a terem</p>
        ):(
          <div className={styles.buttons}>
            <br/>
            {seatElements}
          </div>
        )}
      </div>
    </div>
  )
}

export default SeatSelector;