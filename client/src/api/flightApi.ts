import type { Flight } from "../types/flight";

export const getFlightByDepartureAndArrival = async (departure: String, arrival: String) => {
    const response = await fetch(`/api/flights/search?departure=${departure}&arrival=${arrival}`);
    if (response.status === 401)
        return null;
    return response.json();
};

export const getAllFlights = async () => {
    const response = await fetch("/api/flights");
    if (response.status !== 200)
        return null;
    return response.json();
};

export const deleteFlight = async (id: number) => {
    const response = await fetch(`/api/flights/${id}`, {
        method: "DELETE",
    });
    if (response.status !== 204) 
        return null;
    return true;
};

export const updateFlight = async (id: number, flight: Flight) => {
  const response = await fetch(`/api/flights/${id}`, {
    method: "PUT",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify(flight),
  });

  if (response.status !== 200) return null;
  return response.json();
};