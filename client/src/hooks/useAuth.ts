import { useState } from "react";
import type { User } from "../types/user";
import { loginUser, registerUser } from "../api/userApi";
import { useAuthContext } from "../context/AuthContext";

export const useAuth = () => {
    const [user, setUser] = useState<User | null>(null);
    const { setIsAdmin } = useAuthContext();

    const login = async (username: string, password: string) => {
        const result = await loginUser(username, password);
        if (result) {
            setUser(result);
            setIsAdmin(result.admin);
        }
        return result;
    };

    const register = async (username: string, password: string) => {
        const result = await registerUser(username, password);
        if (result)
            setUser(result);
        return result;
    }

    const logout = () => {
        sessionStorage.clear();
        setUser(null);
        setIsAdmin(false);
    };

    return { user, login, register, logout };
};