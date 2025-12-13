import { useNavigate } from "react-router-dom";
import { useAuth } from "../../hooks/useAuth";
import { useState } from "react";
import styles from "./Register.module.css";
import { Input } from "../../components/input/Input";
import { Button } from "../../components/button/Button";

const RegisterPage = () => {
    const [username, setUsername] = useState("");
    const [password, setPassword] = useState("");
    const [error, setError] = useState("");
    const navigate = useNavigate();

    const { register } = useAuth();

    const handleSubmit = async () => {
        const result = await register(username, password);

        if (!result) {
            setError("There was an error encountered.");
            return;
        }

        setError("");
        navigate("/login", { state: { fromRegister: true } });
    };

    return (
        <div className={styles.registerPage}>
            <h1 className={styles.title}>Register</h1>

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

            <Button text="Register" handleClick={handleSubmit} />
        </div>
    );
}

export default RegisterPage;