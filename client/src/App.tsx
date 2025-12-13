import { BrowserRouter, Routes, Route } from 'react-router-dom';
import LoginPage from './pages/loginPage/LoginPage';
import RegisterPage from './pages/registerPage/RegisterPage';
import { HomePage } from './pages/homePage/HomePage';
import { NavBar } from './components/navBar/NavBar';
import { useState } from 'react';
import { AllFlightsPage } from './pages/allFlightsPage/AllFlightsPage';
import { ReservationsPage } from './pages/reservationsPage/ReservationsPage';

function App() {
  const [logged, setLogged] = useState<boolean>(() => {
    return sessionStorage.getItem("user") !== null;
  });

  return (
    <BrowserRouter>
      {logged && <NavBar setLogged={setLogged} />}
      <Routes>
        <Route path="/login" element={<LoginPage setLogged={setLogged} />} />
        <Route path="/register" element={<RegisterPage />} />
        <Route path="/home" element={<HomePage />} />
        <Route path="/all-flights" element={<AllFlightsPage />} />
        <Route path="/reservations" element={<ReservationsPage />} />
      </Routes>
    </BrowserRouter>
  );
}

export default App
