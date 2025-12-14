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

export const registerUser = async (username: string, password: string) => {
    const response = await fetch("/api/users", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ username, password })
    });

    if (response.status === 401)
        return null;
    return response.json();
};

export const getUserById = async (userId: number) => {
    const response = await fetch(`/api/users/${userId}`);
    if (response.status !== 200)
        return null;
    return response.json();
};

export const updateUsername = async (userId: number, username: string, isAdmin: boolean) => {
  const response = await fetch(`/api/users/${userId}`, {
    method: "PUT",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({ 
        username,
        admin: isAdmin 
    }),
  });

  if (response.status !== 200) 
    return null;
  return response.json();
};

export const deleteUser = async (userId: number) => {
  const response = await fetch(`/api/users/${userId}`, {
    method: "DELETE",
  });

  if (response.status !== 204) 
    return null;
  return true;
};
