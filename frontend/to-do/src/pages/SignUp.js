import React, {useRef, useState} from "react";
import axios from "axios";
import {useNavigate} from "react-router-dom";
import Header from "../components/Header";

const SignUp = () => {
    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");
    const [profileImg, setProfileImg] = useState(null);
    const [phone1, setPhone1] = useState("");
    const [phone2, setPhone2] = useState("");
    const [phone3, setPhone3] = useState("");

    const phone2Ref = useRef(null)
    const phone3Ref = useRef(null)

    const handlePhoneInputChange = (e, setPhone, nextInputRef, maxLength) => {
        const value = e.target.value;
        if (/^\d*$/.test(value) && value.length <= maxLength) {
            setPhone(value)
            if (value.length === maxLength && nextInputRef) {
                nextInputRef.current.focus()
            }
        }
    }

    const navigate = useNavigate()

    const handleSubmit = async (e) => {
        e.preventDefault()

        const phoneNumber = `${phone1}-${phone2}-${phone3}`

        const formData = new FormData();
        formData.append("email", email);
        formData.append("password", password);
        formData.append("phoneNumber", phoneNumber);
        if (profileImg) {
            formData.append("profileImg", profileImg);
        }


        try {
            const response = await axios.post("http://127.0.0.1:8080/register", formData, {
                headers: {
                    "Content-Type": "multipart/form-data"
                }
            })
            alert("회원가입이 완료되었습니다.\n서비스 이용을 위해 이메일 인증을 완료해 주세요.")
            console.log("회원가입이 완료되었습니다: ", response.data);
            navigate("/")
        } catch (error) {
            console.error("Error adding todo: ", error);
        }

    }

    return (
        <main>
            <Header/>
            <div className="add-user-container">
                <h2>회원가입</h2>
                <form onSubmit={handleSubmit} className="add-user-form">
                    <div className="form-group">
                        <label htmlFor="email">이메일</label>
                        <input
                            type="text"
                            id="email"
                            value={email}
                            onChange={(e) => setEmail(e.target.value)}
                            required
                        />
                    </div>
                    <div className="form-group">
                        <label htmlFor="password">비밀번호</label>
                        <input
                            type="password"
                            id="password"
                            value={password}
                            onChange={(e) => setPassword(e.target.value)}
                            minLength="8"
                            maxLength="20"
                            required
                        />
                    </div>
                    <div className="form-group">
                        <label htmlFor="profileImg">프로필 이미지</label>
                        <input
                            type="file"
                            id="profileImg"
                            onChange={(e) => setProfileImg(e.target.files[0])}
                        />
                    </div>
                    <div className="form-group">
                        <label htmlFor="phoneNumber">전화번호</label>
                        <div style={{display: "flex", gap: "10px"}}>
                            <input
                                type="text"
                                value={phone1}
                                onChange={(e) => handlePhoneInputChange(e, setPhone1, phone2Ref, 3)}
                                maxLength="3"
                                style={{width: "50px"}}
                                required
                            />
                            -
                            <input
                                type="text"
                                ref={phone2Ref}
                                value={phone2}
                                onChange={(e) => handlePhoneInputChange(e, setPhone2, phone3Ref, 4)}
                                maxLength="4"
                                style={{width: "50px"}}
                                required
                            />
                            -
                            <input
                                type="text"
                                ref={phone3Ref}
                                value={phone3}
                                onChange={(e) => handlePhoneInputChange(e, setPhone3, null, 4)}
                                maxLength="4"
                                style={{width: "60px"}}
                                required
                            />
                        </div>
                    </div>
                    <button type="submit" className="add-user-button">가입하기</button>
                </form>
            </div>
        </main>
    );
};

export default SignUp