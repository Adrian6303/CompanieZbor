import { useState } from "react";
import { Input } from "../../components/input/Input";
import { useLocation } from "react-router-dom";
import { Button } from "../../components/button/Button";
import styles from "./EditFlightPage.module.css";

export const EditFlightPage = () => {
    const location = useLocation();
    const flight = location.state?.flight;
    const [departure, setDeparture] = useState(flight.departure);
    const [arrival, setArrival] = useState(flight.arrival);
    const [departureTime, setDepartureTime] = useState(flight.departure_time);
    const [duration, setDuration] = useState(flight.duration);
    const [planeName, setPlaneName] = useState(flight.plane_name);
    const [price, setPrice] = useState(flight.price);

    const handleUpdate = () => {

    };

    return (
        <div className={styles.pageContainer}>
            <h1>Edit Flight</h1>

            <div className={styles.formContainer}>
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
                <Input 
                    label="Departure Time" 
                    type="date" 
                    value={departureTime} 
                    setValue={setDepartureTime} 
                />
                <Input 
                    label="Duration" 
                    type="number" 
                    value={duration} 
                    setValue={setDuration} 
                />
                <Input 
                    label="Plane Name" 
                    type="text" 
                    value={planeName} 
                    setValue={setPlaneName} 
                />
                <Input 
                    label="Price" 
                    type="number" 
                    value={price} 
                    setValue={setPrice} 
                />
            </div>

            <Button text="update" handleClick={handleUpdate} />
        </div>
    );
};