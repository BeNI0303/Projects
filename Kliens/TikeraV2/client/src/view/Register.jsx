import styles from "./Register.module.css";
import { useState } from "react";
import  Navbar  from './Navbar.jsx';
import { useToast } from "./ToastProvider";

function Register() {

    const toast = useToast();
    const [name, setName] = useState("");
    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");
    const [confirmPassword, setConfirmPassword] = useState("");

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (password.length < 8) {
        toast("A jelszónak legalább 8 karakter hosszúnak kell lennie!", "error", 2000, () => null)
        return;
    }
   if (password !== confirmPassword) {
      toast("A jelszavak nem egyeznek!", "error", 2000, () => null)
      return;
    }
    try {
      const response = await fetch(`${import.meta.env.VITE_API_URL}/register`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({
                name: name,
                email: email,
                password: password,
                password_confirmation: confirmPassword
            })
      });
      const data = await response.json();
        if (!response.ok) {
           if (data.errors && data.errors.email) {
                toast("Ez az email cím már létezik!", "error", 2000, () => null)
            } else {
                toast("A regisztráció sikertelen: " + JSON.stringify(data), "error", 2000, () => null)
            }
            return;
        }
      toast("Sikeres regisztráció!", "success", 2000, () => null)
    } catch (err) {
      toast(err.message, "error", 2000, () => null)
    }
  };
  return (
    <div className={styles.container}>
      <Navbar/>
      <div className={styles.loginContainer}>
        <h2>Regisztráció</h2>
        <form onSubmit={handleSubmit} className={styles.loginForm}>
          <label>
            <span className={styles.toLeft}>Név:</span>
            <br/>
            <input
              type="name"
              value={name}
              onChange={e => setName(e.target.value)}
              required
              className={styles.input}
            />
          </label>
          <label>
            <span className={styles.toLeft}>Email:</span>
            <br/>
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
          <label>
            <span className={styles.toLeft2}>Jelszó megerősítése:</span>
            <br/>
            <input
              type="password"
              value={confirmPassword}
              onChange={e => setConfirmPassword(e.target.value)}
              required
              className={styles.input}
            />
          </label>
          <button type="submit" className={styles.formbutton}>Regisztráció</button>
        </form>
      </div>
    </div>
  );
}

export default Register;