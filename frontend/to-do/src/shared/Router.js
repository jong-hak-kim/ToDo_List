import React from "react";
import {BrowserRouter, Route, Routes} from "react-router-dom";
import Home from "../pages/Home";
import LoginForm from "../pages/LoginForm";
import AddToDo from "../pages/AddToDo";
import SignUp from "../pages/SignUp";
import UpdateToDo from "../pages/UpdateToDo";
import AdminLoginForm from "../pages/AdminLoginForm";
import AdminPage from "../pages/AdminPage";
import EmailVerification from "../pages/EmailVerification";
import SearchToDo from "../pages/SearchToDo";
import MyPage from "../pages/MyPage";
import FindPassword from "../pages/FindPassword"

const Router = ({token, handleLogout, handleLoginSuccess}) => {

    return (
        <BrowserRouter>
            <Routes>
                <Route path="/" element={<Home token={token} handleLogout={handleLogout}/>}/>
                <Route path="/login" element={<LoginForm token={token} handleLogout={handleLogout}
                                                         handleLoginSuccess={handleLoginSuccess}/>}/>
                <Route path="/todo/add" element={<AddToDo token={token} handleLogout={handleLogout}/>}/>
                <Route path="/signup" element={<SignUp/>}/>
                <Route path="/todo/:listId/update" element={<UpdateToDo token={token} handleLogout={handleLogout}/>}/>
                <Route path="/admin/login" element={<AdminLoginForm/>}/>
                <Route path="/admin" element={<AdminPage/>}/>
                <Route path="/email/verify" element={<EmailVerification/>}/>
                <Route path="/todo/search" element={<SearchToDo token={token} handleLogout={handleLogout}/>}/>
                <Route path="/mypage" element={<MyPage token={token} handleLogout={handleLogout}/>}/>
                <Route path="/find/password" element={<FindPassword/>}/>
            </Routes>
        </BrowserRouter>
    )
}

export default Router
