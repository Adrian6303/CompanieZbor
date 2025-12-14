import type { Reservation } from "../types/reservation";
import type { Flight } from "../types/flight";
import type { ReservationWithFlight } from "../types/reservationWithFlight";

export const postReservation = async (userId: number, flightId: number) => {
    const response = await fetch("/api/reservations", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({
        userId: userId,
        flightId: flightId,
        }),
    });
    if (response.status === 401)
        return null;
    return response.json();
};

export const getReservationsByUserWithFlight = async (userId: number) => {
    const res = await fetch("/api/reservations");
    if (res.status !== 200)
        return null;

    const reservations: Reservation[] = await res.json();

    const userReservations = reservations.filter(r => r.userId === userId);

    const results = await Promise.all(
        userReservations.map(async (r) => {
        const flightRes = await fetch(`/api/flights/${r.flightId}`);
        if (flightRes.status !== 200)
            return null;

        const flight: Flight = await flightRes.json();
        return { id: r.id, flight };
        })
    );

    return results.filter((x): x is ReservationWithFlight => x !== null);
};

export const deleteReservation = async (reservationId: number) => {
    const response = await fetch(`/api/reservations/${reservationId}`, {method: "DELETE"});
    if (response.status !== 204)
        return null;
    return true;
};
