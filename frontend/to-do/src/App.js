import React, {useState} from "react";
import 'react-calendar/dist/Calendar.css';
import './App.css';
import Router from "./shared/Router";

function App() {

    const [token, setToken] = useState(localStorage.getItem("token"));

    const handleLogout = () => {
        localStorage.removeItem("token");
        setToken(null);
    };

    const handleLoginSuccess = (newToken) => {
        localStorage.setItem("token", newToken);
        setToken(newToken);
    };

    return (
      <Router token={token} handleLogout={handleLogout} handleLoginSuccess={handleLoginSuccess}/>
    );
}

export default App;
