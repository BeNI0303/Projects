import styles from "./DaySelector.module.css";

function DaySelector({day, onClick}) {
    const daysHU = {
        Monday: "Hétfő",
        Tuesday: "Kedd",
        Wednesday: "Szerda",
        Thursday: "Csütörtök",
        Friday: "Péntek",
        Saturday: "Szombat",
        Sunday: "Vasárnap"
    };
    return (
        <div onClick={onClick} className={styles.root}>
            <p>{daysHU[day]}</p>
        </div>
    )
}

export default DaySelector;