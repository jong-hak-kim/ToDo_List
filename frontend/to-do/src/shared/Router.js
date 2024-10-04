import React from "react";
import {BrowserRouter, Route, Routes} from "react-router-dom";
import Home from "../pages/Home";
import LoginForm from "../pages/LoginForm";

const Router = ({token, handleLogout, handleLoginSuccess}) => {
    return (
        <BrowserRouter>
            <Routes>
                <Route path="/" element={<Home token={token} handleLogout={handleLogout}/>}/>
                <Route path="/login" element={<LoginForm handleLoginSuccess={handleLoginSuccess}/>}/>
            </Routes>
        </BrowserRouter>
    )
}

export default Router
