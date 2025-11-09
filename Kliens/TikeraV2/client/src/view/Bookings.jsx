import { use, useEffect, useState } from "react";
import styles from "./Bookings.module.css";
import Navbar from './Navbar.jsx';

function Bookings() {
  const [bookings, setBookings] = useState([]);
  const [loading, setLoading] = useState(true);

  let ticketsRender = []

  const ticketPrices = {
    student: 2000,
    adult: 2500,
    senior: 1800,
  };

  const daysHU = {
    1: "Hétfő",
    2: "Kedd",
    3: "Szerda",
    4: "Csütörtök",
    5: "Péntek",
    6: "Szombat",
    7: "Vasárnap"
  };

  useEffect(() => {
    const token = localStorage.getItem("token");
    fetch(`${import.meta.env.VITE_API_URL}/bookings`, {
      headers: {
        "Authorization": `Bearer ${token}`
      }
    })
      .then(res => res.json())
      .then(data => {
        console.log(data.data);

        setBookings(data.data || []);
        setLoading(false);
      })
      .catch(() => setLoading(false));
  }, []);

  for (const booking in bookings) {
    for (const ticket in booking.ticket_types) {
      if (ticket.type == "student") {
        ticketsRender.push(<p key="student-ticket">{ticket.quantity}x Diák <span className={styles.ticketPrices}>{ticket.quantity * ticketPrices.student} Ft</span></p>)
      }
      if (ticket.type == "adult") {
        ticketsRender.push(<p key="adult-ticket">{ticket.quantity}x Felnőtt <span className={styles.ticketPrices}>{ticket.quantity * ticketPrices.adult} Ft</span></p>)
      }
      if (ticket.type == "senior") {
        ticketsRender.push(<p key="senior-ticket">{ticket.quantity}x Nyugdíjas <span className={styles.ticketPrices}>{ticket.quantity * ticketPrices.senior} Ft</span></p>)
      }
    }
  }


  const now = new Date();
  const upcoming = bookings.filter(b => new Date(b.screening.date) > now);
  const past = bookings.filter(b => new Date(b.screening.date) <= now);

  return (
    <>
      <Navbar />
      <div className={styles.container}>
        <h2>Közelgő foglalásaid</h2>
        {loading ? <p>Betöltés...</p> : (
          upcoming.length === 0 ? <p>Nincs közelgő foglalásod.</p> :
            <ul className={styles.horizontalScrollList}>
              {upcoming.map(b => {
                const dateObj = new Date(b.screening.start_time);
                return (
                  <div key={b.id} className={styles.bookingSummary}>
                    <div className={styles.movieDeatils}>
                      <h2 className={styles.title}>{b.screening.movie.title}</h2>
                      <p><b>{b.screening.date}</b> - {daysHU[b.screening.week_day]}</p>
                      <div>
                        {b.ticket_types.map((ticket) =>
                          <>
                            <div>{ticket.type == "student" ?
                              (<p key={ticket.type}>{ticket.quantity}x Diák <span className={styles.ticketPrices}>{ticket.quantity * ticketPrices.student} Ft</span></p>) :
                              (null)}
                            </div>
                            <div>
                              {ticket.type == "adult" ?
                                (<p key="adult-ticket">{ticket.quantity}x Felnőtt <span className={styles.ticketPrices}>{ticket.quantity * ticketPrices.adult} Ft</span></p>) :
                                (null)}
                            </div>
                            <div>
                              {ticket.type == "senior" ?
                                (<p key="senior-ticket">{ticket.quantity}x Nyugdíjas <span className={styles.ticketPrices}>{ticket.quantity * ticketPrices.senior} Ft</span></p>) :
                                (null)}
                            </div>
                          </>
                        )}
                      </div>
                      <hr className={styles.hr} />
                      <div>
                        <p>Helyek</p>
                        <p>
                          {b.seats.map((seat, index) =>
                            `${seat.row}. sor ${seat.seat}. szék${index < b.seats.length - 1 ? ", " : ""}`
                          )}
                        </p>
                      </div>
                      <hr className={styles.hr} />
                    </div>
                    <div className={styles.image}>
                      <img src={b.screening.movie.image_path} alt="" />
                    </div>
                  </div>
                );
              })}
            </ul>
        )}

        <h2>Korábbi foglalásaid</h2>
        {loading ? <p>Betöltés...</p> : (
          past.length === 0 ? <p>Nincs közelgő foglalásod.</p> :
            <ul className={styles.horizontalScrollList}>
              {past.map(b => {
                const dateObj = new Date(b.screening.start_time);
                return (
                  <div key={b.id} className={styles.bookingSummary}>
                    <div className={styles.movieDeatils}>
                      <h2 className={styles.title}>{b.screening.movie.title}</h2>
                      <p>{daysHU[b.screening.week_day]}</p>
                      <div>
                        {b.ticket_types.map((ticket) =>
                          <>
                            <div>{ticket.type == "student" ?
                              (<p key={ticket.type}>{ticket.quantity}x Diák <span className={styles.ticketPrices}>{ticket.quantity * ticketPrices.student} Ft</span></p>) :
                              (null)}
                            </div>
                            <div>
                              {ticket.type == "adult" ?
                                (<p key="adult-ticket">{ticket.quantity}x Felnőtt <span className={styles.ticketPrices}>{ticket.quantity * ticketPrices.adult} Ft</span></p>) :
                                (null)}
                            </div>
                            <div>
                              {ticket.type == "senior" ?
                                (<p key="senior-ticket">{ticket.quantity}x Nyugdíjas <span className={styles.ticketPrices}>{ticket.quantity * ticketPrices.senior} Ft</span></p>) :
                                (null)}
                            </div>
                          </>
                        )}
                      </div>
                      <hr className={styles.hr} />
                      <div>
                        <p>Helyek</p>
                        <p>
                          {b.seats.map((seat, index) =>
                            `${seat.row}. sor ${seat.seat}. szék${index < b.seats.length - 1 ? ", " : ""}`
                          )}
                        </p>
                      </div>
                      <hr className={styles.hr} />
                    </div>
                    <div className={styles.imagepast}>
                      <img src={b.screening.movie.image_path} alt="" />
                    </div>
                  </div>
                );
              })}
            </ul>
        )}
      </div>
    </>

  );
}

export default Bookings;