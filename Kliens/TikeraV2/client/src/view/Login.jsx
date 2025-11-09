import styles from "./Login.module.css";
import { useState, useContext } from "react";
import { ReservationProgressContext } from '../state/reservationProgress.jsx';
import  Navbar  from './Navbar.jsx';
import { useToast } from "./ToastProvider";

function Login() {

  const toast = useToast();
  const { handleLogin, setIsAdmin} = useContext(ReservationProgressContext);
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const handleSubmit = async (e) => {
    e.preventDefault();
     try {
      const response = await fetch(`${import.meta.env.VITE_API_URL}/login`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({
            email: email,
            password: password
            }),   
      });
      const data = await response.json();
      if (!response.ok) {
        if (data.errors || data.message === "Invalid credentials") {
          toast("Hibás email vagy jelszó!", "error", 2000, () => null)
        } else {
          toast("Bejelentkezés sikertelen: " + JSON.stringify(data), "error", 2000, () => null)
        }
        return;
      }
      if (data.data && data.data.token) {
        
        if(data.data.user.role === "admin") {
          setIsAdmin(true);
          localStorage.setItem("isAdmin", true);
        }
        const token = data.data.token.split("|")[1] || data.data.token;
        localStorage.setItem("token", token);
        handleLogin(data.data.user);
        toast("Sikeres bejelentkezés!", "success", 2000,() => window.location.href = "/")
    } else {
      toast("Nem kaptunk tokent a szervertől!", "error", 2000, () => null)
    }
    } catch (err) {
      toast("Hiba történt: " + err.message, "error", 2000, () => null)
    }
  };
  return (
    <div className={styles.container}>
      <Navbar/>
      <div className={styles.loginContainer}>
        <h2>Bejelentkezés</h2>
        <form onSubmit={handleSubmit} className={styles.loginForm}>
          <label>
            <span className={styles.toLeft}>Email:</span>
            <br />
            <input
              type="email"
              value={email}
              onChange={e => setEmail(e.target.value)}
              required
              className={styles.input}
            />
          </label>
          <label>
            <span className={styles.toLeft}>Jelszó:</span>
            <br/>
            <input
              type="password"
              value={password}
              onChange={e => setPassword(e.target.value)}
              required
              className={styles.input}
            />
          </label>
          <button type="submit" className={styles.formbutton}>Bejelentkezés</button>
        </form>
      </div>
    </div>
  );
}

export default Login;