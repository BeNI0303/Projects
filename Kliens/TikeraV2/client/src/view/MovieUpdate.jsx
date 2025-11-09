import { useState, useContext } from "react";
import { ReservationProgressContext } from "../state/reservationProgress";
import Modal from "./Modal";
import styles from "./MovieCreate.module.css";
import Navbar from './Navbar.jsx';

function MovieCreate() {
    const { updateMovie, movies, deleteMovie } = useContext(ReservationProgressContext);

    const [modalOpen, setModalOpen] = useState(false);
    const [title, setTitle] = useState("");
    const [description, setDescription] = useState("");
    const [duration, setDuration] = useState("");
    const [genre, setGenre] = useState("");
    const [releaseYear, setReleaseYear] = useState("");
    const [imagePath, setImagePath] = useState("");
    const [loading, setLoading] = useState(false);
    const [selectedMovieId, setSelectedMovieId] = useState("");
    const selectedMovie = movies.find(movie => movie.id === Number(selectedMovieId)) || {};

    const handleSubmit = async (e) => {
        e.preventDefault();
        setLoading(true);
        const movieData = {
            title: selectedMovie.title,
            description: description,
            duration: Number(duration),
            genre: genre,
            release_year: Number(releaseYear),
            image_path: imagePath
        };
        const success = await updateMovie(movieData, selectedMovieId);
        setLoading(false);
        if (success) {
            setTitle("");
            setDescription("");
            setDuration("");
            setGenre("");
            setReleaseYear("");
            setImagePath("");
        }
    };

    const handleDelete = async (e) => {
        e.preventDefault();
        setLoading(true);
        const success = await deleteMovie(selectedMovieId);
        setLoading(false);
        if (success) {
            setSelectedMovieId("");
            setTitle("");
            setDescription("");
            setDuration("");
            setGenre("");
            setReleaseYear("");
            setImagePath("");
        }
    }

    return (
        <>
            <Navbar />
            <div className={styles.container}>
                <button onClick={() => setModalOpen(true)}>Film szerkesztése</button>
                <Modal isOpen={modalOpen} onClose={() => setModalOpen(false)}>
                    <h2>Film szerkesztése</h2>
                    <form onSubmit={handleSubmit}>
                        <label>Film:</label>
                        <select value={selectedMovieId} onChange={e => setSelectedMovieId(e.target.value)} required>
                            <option value="">Válassz ki egy filmet</option>
                            {movies.map(movie => (
                                <option key={movie.id} value={movie.id}>{movie.title}</option>
                            ))}
                        </select>
                        <div>
                            <label>Leírás:</label>
                            <textarea value={description} className={styles.desc} placeholder={selectedMovie.description}
                                onChange={e => setDescription(e.target.value)} required />
                        </div>
                        <div>
                            <label>Hossz (percben):</label>
                            <input type="number" value={duration} placeholder={selectedMovie.duration} onChange={e => setDuration(e.target.value)} required min="1" />
                        </div>
                        <div>
                            <label>Műfaj:</label>
                            <input type="text" value={genre} placeholder={selectedMovie.genre} onChange={e => setGenre(e.target.value)} required />
                        </div>
                        <div>
                            <label>Megjelenés éve:</label>
                            <input type="number" value={releaseYear} placeholder={selectedMovie.release_year} onChange={e => setReleaseYear(e.target.value)} required min="1900" />
                        </div>
                        <div>
                            <label>Borítókép URL:</label>
                            <input type="text" value={imagePath} placeholder={selectedMovie.image_path} onChange={e => setImagePath(e.target.value)} required />
                        </div>
                        <button type="submit" disabled={loading}>{loading ? "Mentés..." : "Film módosítása"}</button>
                        <button type="submit" disabled={loading} onClick={handleDelete}>{loading ? "Mentés..." : "Törlése"}</button>
                    </form>
                </Modal>
            </div>
        </>

    );
}

export default MovieCreate;