import { useState } from "react";
import { useAuth } from "../../hooks/useAuth";

const LoginPage = () => {
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const [error, setError] = useState("");

  const { login, user, logout } = useAuth();

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();

    const result = await login(username, password);

    if (!result) {
      setError("Invalid username or password");
      return;
    }

    setError("");
    alert(`Welcome, ${result.username}!`); 
  }

  return (
    <div>
      <h1>Login</h1>

      <form onSubmit={handleSubmit}>
        <input
          type="text"
          placeholder="Username"
          value={username}
          onChange={e => setUsername(e.target.value)}
          required
        />

        <input
          type="password"
          placeholder="Password"
          value={password}
          onChange={e => setPassword(e.target.value)}
          required
        />

        {error && <p>{error}</p>}

        <button type="submit">Login</button>
      </form>
    </div>
  );
}

export default LoginPage;