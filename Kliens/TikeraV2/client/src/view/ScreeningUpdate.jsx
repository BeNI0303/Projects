import { useState, useContext } from "react";
import { ReservationProgressContext } from "../state/reservationProgress";
import Modal from "./Modal";
import styles from "./ScreeningCreate.module.css";
import Navbar from './Navbar.jsx';

function ScreeningCreate() {
    const { movies, updateScreening, deleteScreening } = useContext(ReservationProgressContext);

    const [modalOpen, setModalOpen] = useState(false);
    const [movieId, setMovieId] = useState("");
    const [room, setRoom] = useState("");
    const [date, setDate] = useState("");
    const [startTime, setStartTime] = useState("");
    const [loading, setLoading] = useState(false);
    const [screenings, setScreenings] = useState([]);
    const [SelectedScreening, setSelectedScreening] = useState("");

    const handleSubmit = async (e) => {
        e.preventDefault();
        setLoading(true);
        const screeningData = {
            movie_id: Number(movieId),
            date: date,
            start_time: startTime
        };
        const success = await updateScreening(screeningData, SelectedScreening.id);
        setLoading(false);
        if (success) {
            setMovieId("");
            setRoom("");
            setDate("");
            setStartTime("");
        }
    };

    const handleDelete = async (e) => {
        e.preventDefault();
        setLoading(true);
        const success = await deleteScreening(SelectedScreening.id);
        setLoading(false);
        if (success) {
            setMovieId("");
            setRoom("");
            setDate("");
            setStartTime("");
            setSelectedScreening("");
            setScreenings([]);
        }
    }

    const handleRoomChange = (e) => {
        const selectedScreening = screenings.find(screening => screening.id === Number(e));
        if (selectedScreening) {
            setSelectedScreening(selectedScreening || {});
            setRoom(selectedScreening.room.id);
        } else {
            setRoom("");
        }
    }

    const handleMovieChange = (e) => {
        setMovieId(e);
        const selectedMovie = movies.find(movie => movie.id === Number(e));
        if (selectedMovie) {
            setScreenings(selectedMovie.screenings || []);
        } else {
            setScreenings([]);
        }
    }

    return (
        <>
            <Navbar />
            <div className={styles.container}>
                <button onClick={() => setModalOpen(true)}>Vetítés szerkesztése</button>
                <Modal isOpen={modalOpen} onClose={() => setModalOpen(false)}>
                    <h2>Vetítés szerkesztése</h2>
                    <form onSubmit={handleSubmit}>
                        <div>
                            <label>Film:</label>
                            <select value={movieId} onChange={e => handleMovieChange(e.target.value)} required>
                                <option value="">Válassz ki egy filmet</option>
                                {movies.map(movie => (
                                    <option key={movie.id} value={movie.id}>{movie.title}</option>
                                ))}
                            </select>
                        </div>
                        <div>
                            <label>Terem:</label>
                            <select value={room} onChange={e => handleRoomChange(e.target.value)} required>
                                <option value="">Válassz ki egy termet</option>
                                {screenings.map(screening => (
                                    <option key={screening.id} value={screening.id}>{screening.room.name} ({screening.room.rows} sor, {screening.room.seatsPerRow} szék/sor)</option>
                                ))}
                            </select>
                        </div>
                        <div>
                            <label>Dátum:</label>
                            <input type="text" value={date} placeholder={SelectedScreening.date} onChange={e => setDate(e.target.value)} required />
                        </div>
                        <div>
                            <label>Kezdés időpontja:</label>
                            <input type="text" value={startTime} placeholder={SelectedScreening.start_time} onChange={e => setStartTime(e.target.value)} required />
                        </div>
                        <button type="submit" disabled={loading}>{loading ? "Mentés..." : "Vetítés módosítása"}</button>
                        <button type="submit" disabled={loading} onClick={handleDelete}>{loading ? "Mentés..." : "Vetítés törlése"}</button>
                    </form>
                </Modal>
            </div>
        </>

    );
}

export default ScreeningCreate;