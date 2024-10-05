import React, {useState} from "react";
import axios from "axios";
import {useNavigate} from "react-router-dom";
import Header from "../components/Header";

const LoginForm = ({token, handleLogout, handleLoginSuccess}) => {
    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");
    const [error, setError] = useState("");
    const [loading, setLoading] = useState(false);

    const navigate = useNavigate()

    const handleSubmit = async (e) => {
        e.preventDefault();
        setLoading(true)
        setError("")

        try {
            const response = await axios.post("http://127.0.0.1:8080/login", {
                email: email,
                password: password,
            });

            console.log("Login Successful", response.data);

            handleLoginSuccess(response.data.token);

            navigate("/")

        } catch (error) {
            console.error("Error logging in:", error);
            setError("로그인 실패")
        } finally {
            setLoading(false)
        }
    }

    return (
        <main>
            <Header isLoggedIn={!!token} handleLogout={handleLogout}/>

            <div className="login-form-container">
                <h2>로그인</h2>
                <form onSubmit={handleSubmit}>
                    <div>
                        <label htmlFor="email">이메일:</label>
                        <input
                            type="email"
                            id="email"
                            value={email}
                            onChange={(e) => setEmail(e.target.value)}
                            required
                        />
                    </div>
                    <div>
                        <label htmlFor="password">비밀번호:</label>
                        <input
                            type="password"
                            id="password"
                            value={password}
                            onChange={(e) => setPassword(e.target.value)}
                            required
                        />
                    </div>
                    {error && <p style={{color: "red"}}>{error}</p>}
                    <button type="submit" disabled={loading}>
                        {loading ? "로그인 중..." : "로그인"}
                    </button>
                </form>
            </div>
        </main>
    )

}

export default LoginForm