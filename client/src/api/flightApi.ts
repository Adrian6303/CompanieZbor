export const getFlightByDepartureAndArrival = async (departure: String, arrival: String) => {
    const response = await fetch(`/api/flights/search?departure=${departure}&arrival=${arrival}`);
    if (response.status === 401)
        return null;
    return response.json();
};