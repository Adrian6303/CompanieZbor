import { useEffect, useState } from "react";
import type { Flight } from "../../types/flight";
import { getAllFlights } from "../../api/flightApi";
import { FlightCard } from "../../components/flightCard/FlightCard";
import styles from "./AllFlightsPage.module.css";

export const AllFlightsPage = () => {
    const [flights, setFlights] = useState<Flight[] | null>(null);

    useEffect(() => {
        const fetchFlights = async () => {
            const response = await getAllFlights();
            if (response)
                setFlights(response);
        };
        fetchFlights();
    }, []);

    return (
        <div className={styles.pageContainer}>
            <h1>Flights</h1>
            {flights && flights.map(flight => (
                <FlightCard flight={flight} key={flight.id} />
            ))}
        </div>
    );
}