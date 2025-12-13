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