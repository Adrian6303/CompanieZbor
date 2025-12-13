import { useState } from "react";
import type { User } from "../types/user";
import { loginUser, registerUser } from "../api/userApi";

export const useAuth = () => {
    const [user, setUser] = useState<User | null>(null);

    const login = async (username: string, password: string) => {
        const result = await loginUser(username, password);
        if (result)
            setUser(result);
        return result;
    };

    const register = async (username: string, password: string) => {
        const result = await registerUser(username, password);
        if (result)
            setUser(result);
        return result;
    }

    const logout = () => setUser(null);

    return { user, login, register, logout };
};