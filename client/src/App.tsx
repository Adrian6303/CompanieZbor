import { BrowserRouter, Routes, Route } from 'react-router-dom';
import LoginPage from './pages/loginPage/LoginPage';
import RegisterPage from './pages/registerPage/RegisterPage';
import { HomePage } from './pages/homePage/HomePage';
import { NavBar } from './components/navBar/NavBar';
import { useState } from 'react';
import { AllFlightsPage } from './pages/allFlightsPage/AllFlightsPage';
import { ReservationsPage } from './pages/reservationsPage/ReservationsPage';
import { EditFlightPage } from './pages/editFlightPage/EditFlightPage';
import { AccountPage } from './pages/accountPage/AccountPage';

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
        <Route path="/edit-flight" element={<EditFlightPage />} />
        <Route path="/account" element={<AccountPage setLogged={setLogged} />} />
      </Routes> 
    </BrowserRouter>
  );
}

export default App
