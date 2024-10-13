import React, { useEffect } from 'react';
import axios from 'axios';
import {useLocation, useNavigate} from 'react-router-dom';

const EmailVerification = () => {
    const location = useLocation();
    const navigate = useNavigate()

    useEffect(() => {
        const queryParams = new URLSearchParams(location.search);
        const token = queryParams.get('token');

        if (token) {
            verifyEmail(token);
        }
    }, [location]);

    const verifyEmail = async (token) => {
        try {
            const response = await axios.get(`http://127.0.0.1:8080/email/verify?token=${token}`);
            if (response.status === 200) {
                // 성공 시 alert 표시
                alert("인증이 완료되었습니다."); // "인증이 완료되었습니다." 메시지
                navigate("/");
            }
        } catch (error) {
            // 에러 처리
            console.error("이메일 인증 실패:", error);
            alert("이메일 인증에 실패했습니다."); // 실패 시 alert 표시
        }
    };

    return (
        <div>
            <h3>이메일 인증 처리 중...</h3>
        </div>
    );
};

export default EmailVerification;