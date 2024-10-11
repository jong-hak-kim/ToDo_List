import React, {useState} from "react";
import axios from "axios";

const AdminLoginForm = () => {
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [error, setError] = useState('');

    const handleLogin = async (e) => {
        e.preventDefault();

        if (!email || !password) {
            setError('이메일과 비밀번호를 입력해주세요.');
            return;
        }

        try {
            const response = await axios.post('http://127.0.0.1:8080/admin/login', { email, password });

            if (response.status === 200) {
                const { token } = response.data;
                localStorage.setItem('adminToken', token); // 토큰 저장
                window.location.href = '/admin';
            } else {
                setError('로그인에 실패했습니다.');
            }
        } catch (error) {
            if (error.response && error.response.data) {
                setError(error.response.data.message || '로그인에 실패했습니다.');
            } else {
                setError('서버 오류가 발생했습니다.');
            }
        }
    };

    return (
        <div className="admin-login-container">
            <h2>관리자 로그인</h2>
            <form onSubmit={handleLogin}>
                <div className="form-group">
                    <label htmlFor="email">이메일</label>
                    <input
                        type="email"
                        id="email"
                        value={email}
                        onChange={(e) => setEmail(e.target.value)}
                        placeholder="이메일을 입력하세요"
                    />
                </div>
                <div className="form-group">
                    <label htmlFor="password">비밀번호</label>
                    <input
                        type="password"
                        id="password"
                        value={password}
                        onChange={(e) => setPassword(e.target.value)}
                        placeholder="비밀번호를 입력하세요"
                    />
                </div>
                {error && <p className="error-message">{error}</p>}
                <button type="submit">로그인</button>
            </form>
        </div>
    );
};

export default AdminLoginForm;