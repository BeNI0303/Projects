import styles from "./MovieCard.module.css";

function MovieCard({id, onClick, url, title, duration, genre}) {
  return (    
    <div className={styles.root} onClick={onClick}>
      <img className={styles.image} width="200" height="200" src={url} alt={title} />
      <h3>{title}</h3>
      <p className={styles.p}>{genre} {duration}perc</p>
    </div>
  )
}

export default MovieCard;