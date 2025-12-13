import { useState } from "react";
import type { Flight } from "../../types/flight";
import { Button } from "../button/Button";
import styles from "./FlightCard.module.css";
import { postReservation } from "../../api/reservationsApi";

const formatDepartureTime = (isoString: string) => {
    const date = new Date(isoString);
    return date.toLocaleString("en-GB", {
        day: "2-digit",
        month: "2-digit",
        year: "numeric",
        hour: "2-digit",
        minute: "2-digit",
    });
};

export const FlightCard = (flight: Flight) => {
    const [success, setSuccess] = useState(false);
    const handleClick = async () => {
        const user = JSON.parse(sessionStorage.getItem("user")!);
        const response = await postReservation(user.id, flight.id);
        if (response)
            setSuccess(true);
    };

    return (
        <div className={`${styles.card} ${success ? styles.successCard : styles.defaultCard}`}>
            {success && <p className={styles.successMessage}>Flight booked successfully</p>}
            <p className={styles.departure}><b>Departure:</b> {flight.departure}</p>
            <p className={styles.arrival}><b>Arrival:</b> {flight.arrival}</p>
            <p className={styles.planeName}><b>{flight.plane_name}</b></p>
            <p className={styles.departureTime}>Leaves at {formatDepartureTime(flight.departure_time)}</p>
            <p className={styles.duration}>Flight duration {flight.duration} minutes</p>
            <p className={styles.price}><b>Price</b> {flight.price}</p>
            <Button text="Book" handleClick={handleClick} />
        </div>
    );
}