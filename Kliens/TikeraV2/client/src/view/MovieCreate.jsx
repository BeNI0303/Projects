import { useState, useContext } from "react";
import { ReservationProgressContext } from "../state/reservationProgress";
import Modal from "./Modal";
import styles from "./MovieCreate.module.css";
import Navbar from './Navbar.jsx';

function MovieCreate() {
    const { createMovie } = useContext(ReservationProgressContext);

    const [modalOpen, setModalOpen] = useState(false);
    const [title, setTitle] = useState("");
    const [description, setDescription] = useState("");
    const [duration, setDuration] = useState("");
    const [genre, setGenre] = useState("");
    const [releaseYear, setReleaseYear] = useState("");
    const [imagePath, setImagePath] = useState("");
    const [loading, setLoading] = useState(false);

    const handleSubmit = async (e) => {
        e.preventDefault();
        setLoading(true);
        const movieData = {
            title,
            description,
            duration: Number(duration),
            genre,
            release_year: Number(releaseYear),
            image_path: imagePath
        };
        const success = await createMovie(movieData);
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

    return (
        <>
            <Navbar />
            <div className={styles.container}>
                <button onClick={() => setModalOpen(true)}>Új film hozzáadása</button>
                <Modal isOpen={modalOpen} onClose={() => setModalOpen(false)}>
                    <h2>Új film hozzáadása</h2>
                    <form onSubmit={handleSubmit}>
                        <div>
                            <label>Film címe:</label>
                            <input type="text" value={title} placeholder="The Fast and the Furious: Tokyo Drift" onChange={e => setTitle(e.target.value)} required />
                        </div>
                        <div>
                            <label>Leírás:</label>
                            <textarea value={description} className={styles.desc} placeholder="The Fast and the Furious: Tokyo Drift is a 2006 action film directed by Justin Lin and written by Chris Morgan. 
                        It is a standalone sequel to The Fast and the Furious (2001) and 2 Fast 2 Furious (2003), and the third installment in the Fast & Furious franchise. 
                        It stars Lucas Black and Bow Wow. 
                        In the film, car enthusiast Sean Boswell (Black) is sent to live in Tokyo with his estranged father and finds solace exploring the city's drifting community."
                                onChange={e => setDescription(e.target.value)} required />
                        </div>
                        <div>
                            <label>Hossz (percben):</label>
                            <input type="number" value={duration} placeholder="104" onChange={e => setDuration(e.target.value)} required min="1" />
                        </div>
                        <div>
                            <label>Műfaj:</label>
                            <input type="text" value={genre} placeholder="Action" onChange={e => setGenre(e.target.value)} required />
                        </div>
                        <div>
                            <label>Megjelenés éve:</label>
                            <input type="number" value={releaseYear} placeholder="2006" onChange={e => setReleaseYear(e.target.value)} required min="1900" />
                        </div>
                        <div>
                            <label>Borítókép URL:</label>
                            <input type="text" value={imagePath} placeholder="https://upload.wikimedia.org/wikipedia/en/4/4f/Poster_-_Fast_and_Furious_Tokyo_Drift.jpg" onChange={e => setImagePath(e.target.value)} required />
                        </div>
                        <button type="submit" disabled={loading}>{loading ? "Mentés..." : "Film hozzáadása"}</button>
                    </form>
                </Modal>
            </div>
        </>

    );
}

export default MovieCreate;