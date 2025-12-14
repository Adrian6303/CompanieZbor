import { useState } from "react";
import { Input } from "../../components/input/Input";
import { useLocation, useNavigate } from "react-router-dom";
import { Button } from "../../components/button/Button";
import styles from "./EditFlightPage.module.css";
import type { Flight } from "../../types/flight";
import { updateFlight } from "../../api/flightApi";

export const EditFlightPage = () => {
  const location = useLocation();
  const navigate = useNavigate();

  const flight = (location.state as { flight: Flight } | null)?.flight;
  if (!flight) {
    return <div>No flight selected</div>;
  }

  const [departure, setDeparture] = useState<string>(flight.departure);
  const [arrival, setArrival] = useState<string>(flight.arrival);
  const [departureDate, setDepartureDate] = useState<string>((flight.departure_time ?? "").slice(0, 10));
  const [duration, setDuration] = useState<string>(String(flight.duration));
  const [planeName, setPlaneName] = useState<string>(flight.plane_name ?? "");
  const [price, setPrice] = useState<string>(String(flight.price));
  const [error, setError] = useState<string>("");

  const handleUpdate = async () => {
    setError("");

    const updated: Flight = {
      ...flight,
      departure,
      arrival,
      departure_time: `${departureDate}T00:00:00`,
      duration: Number(duration),
      nr_seats: flight.nr_seats,
      plane_name: planeName,
      price: Number(price),
    };

    const result = await updateFlight(flight.id, updated);

    if (!result) {
      setError("Update failed.");
      return;
    }
    navigate("/home", { replace: true });
  };

  return (
    <div className={styles.pageContainer}>
      <h1>Edit Flight</h1>

      <div className={styles.formContainer}>
        <Input label="Departure" type="text" value={departure} setValue={setDeparture} />
        <Input label="Arrival" type="text" value={arrival} setValue={setArrival} />
        <Input label="Departure Time" type="date" value={departureDate} setValue={setDepartureDate} />
        <Input label="Duration" type="number" value={duration} setValue={setDuration} />
        <Input label="Plane Name" type="text" value={planeName} setValue={setPlaneName} />
        <Input label="Price" type="number" value={price} setValue={setPrice} />
      </div>

      {error && <p className={styles.errorMessage}>{error}</p>}

      <Button text={"Update"} handleClick={handleUpdate} />
    </div>
  );
};
