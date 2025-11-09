import React from "react";
import styles from "./Modal.module.css";

const Modal = ({ isOpen, onClose, children }) => {
  if (!isOpen) return null;

  return (
    <div className={styles.overlay} onClick={onClose}>
      <div className={styles.modal} onClick={e => e.stopPropagation()}>
        {children}
        <button onClick={onClose} className={styles.closeButton}>Bezárás</button>
      </div>
    </div>
  );
};

export default Modal;
