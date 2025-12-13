import { useState } from "react";
import { useAuth } from "../../hooks/useAuth";
import { Input } from "../../components/input/Input";
import styles from "./LoginPage.module.css";
import { Button } from "../../components/button/Button";
import { useLocation, useNavigate } from "react-router-dom";

interface Props {
    setLogged: React.Dispatch<React.SetStateAction<boolean>>;
}

const LoginPage = ({ setLogged }: Props) => {
    const [username, setUsername] = useState("");
    const [password, setPassword] = useState("");
    const [error, setError] = useState("");
    const navigate = useNavigate();
    const location = useLocation();
    const fromRegister = location.state?.fromRegister;

    const { login } = useAuth();

    const handleSubmit = async () => {
        const result = await login(username, password);

        if (!result) {
            setError("Invalid username or password");
            return;
        }

        setError("");

        sessionStorage.setItem(
            "user",
            JSON.stringify({
                id: result.id,
                username: result.username,
                admin: result.admin,
            })
        );
        setLogged(true);
        navigate("/home");
    };

    return (
        <div className={styles.loginPage}>
        {fromRegister && 
        <p className={styles.accountCreatedMessage}>Account created successfully! Login here</p>}
        <h1 className={styles.title}>Login</h1>

            <Input 
                label="Username" 
                type="text" 
                value={username} 
                setValue={setUsername} 
            />
            <Input 
                label="Password" 
                type="password" 
                value={password} 
                setValue={setPassword} 
            />

            {error && <p className={styles.error}>{error}</p>}

            <Button text="Submit" handleClick={handleSubmit} />
            <p className={styles.registerMessage}>
                Don't have an account? Register <a href="/register" className={styles.link}>Here</a>
            </p>
        </div>
    );
}

export default LoginPage;