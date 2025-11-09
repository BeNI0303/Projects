import { useState, useContext, useEffect } from "react";
import { ReservationProgressContext } from "../state/reservationProgress";
import Modal from "./Modal";
import styles from "./ScreeningCreate.module.css";
import Navbar from './Navbar.jsx';

function ScreeningCreate() {
    const { movies, createScreening } = useContext(ReservationProgressContext);

    const [modalOpen, setModalOpen] = useState(false);
    const [movieId, setMovieId] = useState("");
    const [room, setRoom] = useState("");
    const [date, setDate] = useState("");
    const [startTime, setStartTime] = useState("");
    const [loading, setLoading] = useState(false);
    const rooms = [{ id: "1", "rows": 10, "seatsPerRow": 10 }, { id: 2, "rows": 7, "seatsPerRow": 8 }];
    const moviesArray = Array.isArray(movies) ? movies : [];

    const handleSubmit = async (e) => {
        e.preventDefault();
        setLoading(true);
        const screeningData = {
            movie_id: Number(movieId),
            room_id: Number(room),
            date: date,
            start_time: startTime
        };
        const success = await createScreening(screeningData);
        setLoading(false);
        if (success) {
            setMovieId("");
            setRoom("");
            setDate("");
            setStartTime("");
        }
    };

    const handleMovieChange = (e) => {
        setMovieId(e);
    }

    return (
        <>
            <Navbar />
            <div className={styles.container}>
                <button onClick={() => setModalOpen(true)}>Új vetítés hozzáadása</button>
                <Modal isOpen={modalOpen} onClose={() => setModalOpen(false)}>
                    <h2>Új vetítés hozzáadása</h2>
                    <form onSubmit={handleSubmit}>
                        <div>
                            <label>Film:</label>
                            <select value={movieId} onChange={e => handleMovieChange(e.target.value)} required>
                                <option value="">Válassz ki egy filmet</option>
                                {moviesArray.map(movie => (
                                    <option key={movie.id} value={movie.id}>{movie.title}</option>
                                ))}
                            </select>
                        </div>
                        <div>
                            <label>Terem:</label>
                            <select value={room} onChange={e => setRoom(e.target.value)} required>
                                <option value="">Válassz ki egy termet</option>
                                {rooms.map(r => (
                                    <option key={r.id} value={r.id}>{r.name} ({r.rows} sor, {r.seatsPerRow} szék/sor)</option>
                                ))}
                            </select>
                        </div>
                        <div>
                            <label>Dátum:</label>
                            <input type="text" value={date} placeholder="2025-04-24" onChange={e => setDate(e.target.value)} required />
                        </div>
                        <div>
                            <label>Kezdés időpontja:</label>
                            <input type="text" value={startTime} placeholder="15:30" onChange={e => setStartTime(e.target.value)} required />
                        </div>
                        <button type="submit" disabled={loading}>{loading ? "Mentés..." : "Vetítés hozzáadása"}</button>
                    </form>
                </Modal>
            </div>
        </>

    );
}

export default ScreeningCreate;