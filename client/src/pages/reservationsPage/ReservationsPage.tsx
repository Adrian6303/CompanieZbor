import { useEffect, useState } from "react";
import { getReservationsByUserWithFlight } from "../../api/reservationsApi";
import type { ReservationWithFlight } from "../../types/reservationWithFlight";
import { FlightCard } from "../../components/flightCard/FlightCard";
import styles from "./ReservationsPage.module.css";

export const ReservationsPage = () => {
    const [reservations, setReservations] = useState<ReservationWithFlight[]>([]);

    useEffect(() => {
        const fetchFlights = async () => {
            const user = JSON.parse(sessionStorage.getItem("user")!);
            const data = await getReservationsByUserWithFlight(user.id);

            if (data) 
                setReservations(data);
        };

        fetchFlights();
    }, []);

    return (
        <div className={styles.pageContainer}>
            <h1>My Reservations</h1>
            {reservations && reservations.map(reservation => {
                const flight = reservation.flight;
                return <FlightCard flight={flight} key={flight.id} isReservation={true} reservationId={reservation.id} setReservations={setReservations} />
            })}
        </div>
    );
}