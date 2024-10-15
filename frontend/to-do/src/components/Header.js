import React, {useEffect, useState} from "react";
import {useNavigate} from "react-router-dom";
import axios from "axios";

function Header({isLoggedIn, token, handleLogout}) {
    const navigate = useNavigate()

    const [profileImageUrl, setProfileImageUrl] = useState("");

    useEffect(() => {
        if (isLoggedIn) {
            axios.get("http://127.0.0.1:8080/profile_img", {
                headers: {
                    Authorization: `Bearer ${token}`,
                },
            })
                .then((response) => {
                    setProfileImageUrl(`data:image/png;base64,${response.data.profileImg}`);
                })
                .catch((error) => {
                    console.error("Error fetching profile image:", error);
                });
        }
    }, [isLoggedIn]);

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
                        <img
                            src={profileImageUrl}
                            alt="프로필"
                            className="profile-image"
                            onClick={() => navigate("/mypage")}
                            style={{
                                width: "40px",
                                height: "40px",
                                borderRadius: "50%",
                                cursor: "pointer",
                                marginRight: "10px",
                            }}
                        />
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