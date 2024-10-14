import React from "react";
import {useNavigate} from "react-router-dom";

function Header({isLoggedIn, handleLogout}) {
    const navigate = useNavigate()

    const handleLogoutAndRedirect = () => {
        handleLogout();
        navigate("/");
    };

    return (
        <header className="header">
            <h1
                style={{cursor: "pointer"}}
                onClick={() => {
                    navigate("/")
                }}>TaskMate</h1>
            <div className="auth-buttons">
                {isLoggedIn ? (
                    <>
                        <button onClick={() => {
                            navigate("/todo/search")
                        }} className="auth-button">
                            탐색
                        </button>
                        <button onClick={handleLogoutAndRedirect} className="auth-button">
                            로그아웃
                        </button>
                    </>
                ) : (
                    <>
                        <button onClick={() => {
                            navigate('/login');
                        }}
                                className="auth-button">
                            로그인
                        </button>

                        <button onClick={() => {
                            navigate("/signup");
                        }}
                                className="auth-button">회원가입
                        </button>
                    </>
                )}
            </div>
        </header>
    )
}

export default Header