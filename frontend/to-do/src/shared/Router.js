import React from "react";
import {BrowserRouter, Route, Routes} from "react-router-dom";
import Home from "../pages/Home";
import LoginForm from "../pages/LoginForm";
import AddToDo from "../pages/AddToDo";

const Router = ({token, handleLogout, handleLoginSuccess}) => {
    return (
        <BrowserRouter>
            <Routes>
                <Route path="/" element={<Home token={token} handleLogout={handleLogout}/>}/>
                <Route path="/login" element={<LoginForm token={token} handleLogout={handleLogout} handleLoginSuccess={handleLoginSuccess}/>}/>
                <Route path="/ToDo/add" element={<AddToDo token={token} handleLogout={handleLogout}/>}/>
            </Routes>
        </BrowserRouter>
    )
}

export default Router
