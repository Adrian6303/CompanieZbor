import { useEffect, useState } from "react";
import { deleteUser, getUserById, updateUsername } from "../../api/userApi";
import { Input } from "../../components/input/Input";
import { Button } from "../../components/button/Button";
import styles from "./AccountPage.module.css";
import { useNavigate } from "react-router-dom";
import { useAuth } from "../../hooks/useAuth";
import { useAuthContext } from "../../context/AuthContext";

interface Props {
    setLogged: React.Dispatch<React.SetStateAction<boolean>>;
}

export const AccountPage = ({ setLogged }: Props) => {
    const [username, setUsername] = useState("");
    const [error, setError] = useState("");
    const [message, setMessage] = useState("");
    const navigate = useNavigate();
    const { logout } = useAuth();
    const { isAdmin } = useAuthContext();

    const userString = sessionStorage.getItem("user");
    const user = userString ? JSON.parse(userString) : null;

    useEffect(() => {
       const getUser = async () => {
            const response = await getUserById(user.id);
            if (!response) {
                setError("There was an error encountered");
                return;
            }
            setUsername(response.username);
       } 
       getUser();
    }, []);

    const handleUpdateUsername = async () => {
        const response = await updateUsername(user.id, username, isAdmin);
        if (!response) {
            setError("Error encountered while updating username");
            return;
        }
        setMessage("Username updated successfully");
    };

    const handleDelete = async () => {
        const response = await deleteUser(user.id);
        if (!response) {
            setError("Error encountered while deleting user");
            return;
        }
        logout();
        setLogged(false);
        navigate("/login");
    };

    return (
        <div className={styles.pageContainer}>
            <h1 className={styles.title}>My Account</h1>

            {error && <p className={styles.errorMessage}>{error}</p>}
            {message && <p className={styles.message}>{message}</p>}
            <div className={styles.updateUsernameContainer}>
                <Input label="Username" type="text" value={username} setValue={setUsername} />
                <Button text="Update" handleClick={handleUpdateUsername} />
             </div>
             <Button className={styles.deleteAccountButton} text="Delete Account" handleClick={handleDelete} />
        </div>
    );
}