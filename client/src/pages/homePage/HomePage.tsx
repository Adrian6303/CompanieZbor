import { useState } from "react";
import { Input } from "../../components/input/Input";
import styles from "./HomePage.module.css";
import { Button } from "../../components/button/Button";
import { getFlightByDepartureAndArrival } from "../../api/flightApi";
import type { Flight } from "../../types/flight";
import { FlightCard } from "../../components/flightCard/FlightCard";

export const HomePage = () => {
    const [departure, setDeparture] = useState("");
    const [arrival, setArrival] = useState("");
    const [flight, setFlight] = useState<Flight | null>(null);

    const handleSearch = async () => {
        const flights = await getFlightByDepartureAndArrival(departure, arrival);
        setFlight(flights);
    };

    return (
        <div className={styles.homePageContainer}>
            <h1 className={styles.title}>Search flights</h1>
            <div className={styles.inputContainer}>
                <Input 
                    label="Departure" 
                    type="text" 
                    value={departure} 
                    setValue={setDeparture}     
                />
                <Input 
                    label="Arrival" 
                    type="text"  
                    value={arrival}
                    setValue={setArrival}   
                />
                <Button text="Search" handleClick={handleSearch} />
            </div>
            <div className={styles.flightContainer}>
                {flight && <FlightCard {...flight} />}
            </div>
        </div>
    );
}