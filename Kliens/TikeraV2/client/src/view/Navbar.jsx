import styles from "./Navbar.module.css";
import { useContext } from "react";
import { ReservationProgressContext } from '../state/reservationProgress.jsx';
import ThemeToggle from "./ThemeToggle.jsx";

function Navbar() {
  const { isLoggedIn, handleLogout,user, isAdmin
  } = useContext(ReservationProgressContext);
  return (
    <nav className={styles.navbar}>
      <div className={styles.left}>
        <a href="/" className={styles.logo}>Tikera</a>
        {isLoggedIn ? (
            <>
              {isAdmin ? (
                <>
                  <a href="./moviecreate" rel="noopener noreferrer" className={styles.button}> Film hozzáadása </a>
                  <a href="./screeningcreate" rel="noopener noreferrer" className={styles.button}> Vetítés hozzáadása </a>
                  <a href="./movieupdate" rel="noopener noreferrer" className={styles.button}>Film szerkesztése</a>
                  <a href="./screeningupdate"  rel="noopener noreferrer" className={styles.button}>Vetítés szerkesztése</a>
                </>
              ): (
                <a href="./bookings" rel="noopener noreferrer" className={styles.button}> Vetítéseim </a>
              )}
            </>
          ) : (
            null
          )
        }
      </div>
      <div className={styles.right}>
        {!isLoggedIn ? (
          <>
            <a href="./login" rel="noopener noreferrer" className={styles.button}> Bejelentkezés </a>
            <a href="./register" rel="noopener noreferrer" className={styles.button}> Regisztráció </a>
          </>
        ) : (
          <>
            <p className={styles.button}>Szia {user.name}!</p>
            <button onClick={handleLogout} className={styles.button}>
              Kijelentkezés
            </button>
          </>
        )}
        <ThemeToggle />
      </div>
    </nav>
  );
}

export default Navbar;