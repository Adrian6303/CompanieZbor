import { useNavigate } from "react-router-dom";
import { useAuth } from "../../hooks/useAuth";
import styles from "./NavBar.module.css";

interface Props {
    setLogged: React.Dispatch<React.SetStateAction<boolean>>;
}

export const NavBar = ({ setLogged }: Props) => {
    const { logout } = useAuth();
    const navigate = useNavigate();
    
    const handleLogout = () => {
        logout();
        setLogged(false);
        navigate("/login");
    };

    const storedUser = sessionStorage.getItem("user");
    const user = storedUser ? JSON.parse(storedUser) : null;

    return (
        <ul className={styles.navBar}>
            <li><a href="/home">Home</a></li>
            <li><a href="/all-flights">See all flights</a></li>
            <li><a href="/reservations">Reservations</a></li>
            <li className={styles.welcome}>Welcome, {user.username}</li>
            <li className={styles.logout} onClick={handleLogout}>logout</li>
        </ul>
    );
}