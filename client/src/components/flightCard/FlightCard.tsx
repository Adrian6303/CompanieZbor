import { useState } from "react";
import type { Flight } from "../../types/flight";
import { Button } from "../button/Button";
import styles from "./FlightCard.module.css";
import { deleteReservation, postReservation } from "../../api/reservationsApi";
import { useAuthContext } from "../../context/AuthContext";
import { useNavigate } from "react-router-dom";
import { deleteFlight } from "../../api/flightApi";
import type { ReservationWithFlight } from "../../types/reservationWithFlight";

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

interface Props {
    flight: Flight;
    isReservation?: boolean;
    reservationId?: number;
    setReservations? : React.Dispatch<React.SetStateAction<ReservationWithFlight[]>>;
}

export const FlightCard = ({ flight, isReservation = false, reservationId, setReservations }: Props) => {
    const [success, setSuccess] = useState(false);
    const [deleted, setDeleted] = useState(false);
    const { isAdmin } = useAuthContext();
    const navigate = useNavigate();
    
    const handleClick = async () => {
        const user = JSON.parse(sessionStorage.getItem("user")!);
        const response = await postReservation(user.id, flight.id);
        if (response)
            setSuccess(true);
    };

    const handleDelete = async () => {
        const response = await deleteFlight(flight.id);
        if (response)
            setDeleted(true);
    };

    const handleDeleteReservation = async () => {
        const response = await deleteReservation(reservationId || 0);
        if (response && setReservations && reservationId)
            setReservations(prev => prev.filter(r => r.id !== reservationId));
    };

    return (
        <div className={`${styles.card} ${success ? styles.successCard : styles.defaultCard}`}>
            {deleted ? 
            <p className={styles.deleteMessage}>Flight deleted successfully</p> :
            <>
            {success && <p className={styles.successMessage}>Flight booked successfully</p>}

            <p className={styles.departure}>
                <b>Departure:</b> {flight.departure}
            </p>
            <p className={styles.arrival}>
                <b>Arrival:</b> {flight.arrival}
            </p>
            <p className={styles.planeName}>
                <b>{flight.plane_name}</b>
            </p>
            <p className={styles.departureTime}>
                Leaves at {formatDepartureTime(flight.departure_time)}
            </p>
            <p className={styles.duration}>
                Flight duration {flight.duration} minutes
            </p>
            <p className={styles.price}>
                <b>Price</b> {flight.price}
            </p>

            <div className={styles.actions}>
                {isReservation && <Button className={styles.deleteReservation} text="Delete Reservation" handleClick={handleDeleteReservation} />}
                {isAdmin && !isReservation && 
                    <Button className={styles.edit} text="Edit" handleClick={() => navigate("/edit-flight", { state: { flight: flight }})} />}
                {isAdmin && !isReservation && 
                    <Button className={styles.delete} text="Delete" handleClick={handleDelete} />}
                {!isReservation && <Button className={styles.book} text="Book" handleClick={handleClick} />}
            </div>
            </>}
        </div>
    );
}