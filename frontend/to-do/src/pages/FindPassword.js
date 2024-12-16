import Header from "../components/Header";
import React, {useState} from "react";
import axios from "axios";
import {useNavigate} from "react-router-dom";

const FindPassword = () => {

    const [email, setEmail] = useState("");
    const [error, setError] = useState("");
    const [loading, setLoading] = useState(false);

    const navigate = useNavigate()

    const handleSubmit = async (e) => {
        e.preventDefault();
        setError("")

        try {
            const response = await axios.post("http://127.0.0.1:8080/reset/password", {
                email: email
            })

            console.log("send new Password Successful", response.data);

            alert("이메일로 임시 비밀번호가 발송되었습니다")

            navigate("/")
        } catch (e) {
            console.error("Error logging in:", error);
            setError("존재하지 않는 이메일입니다.")
        } finally {
            setLoading(false)
        }
    }

    return (
        <main>
            <Header/>

            <div className="find-password-container">
                <h2>비밀번호 찾기</h2>
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
                    {error && <p style={{color: "red"}}>{error}</p>}
                    <button type="submit" disabled={loading}>
                        {loading ? "전송 중..." : "확인"}
                    </button>
                </form>
            </div>
        </main>
    )

}

export default FindPassword

