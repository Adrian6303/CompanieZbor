import type { Flight } from "./flight";

export interface ReservationWithFlight {
    id: number;
    flight: Flight;
}