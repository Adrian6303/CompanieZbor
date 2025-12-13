// import { User } from "../types/user";

export const loginUser = async (username: string, password: string) => {
    const response = await fetch("/api/users/login", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ username, password })
    });

    if (response.status === 401)
        return null;
    return response.json();
};

export const registerUser = async(username: string, password: string) => {
    const response = await fetch("/api/users", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ username, password })
    });

    if (response.status === 401)
        return null;
    return response.json();
};