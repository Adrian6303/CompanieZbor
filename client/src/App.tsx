import { BrowserRouter, Routes, Route } from 'react-router-dom';
import LoginPage from './pages/loginPage/LoginPage';
import RegisterPage from './pages/registerPage/RegisterPage';

function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/login" element={<LoginPage />} />
        <Route path="/register" element={<RegisterPage />} />
        <Route path="/home" element={<h1>Home</h1>} />
      </Routes>
    </BrowserRouter>
  );
}

export default App
