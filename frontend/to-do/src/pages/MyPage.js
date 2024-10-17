import React, {useEffect, useState, useRef} from "react";
import axios from "axios";
import Header from "../components/Header";
import {useNavigate} from "react-router-dom";

const MyPage = ({token, handleLogout}) => {
    const [email, setEmail] = useState("");
    const [profileImg, setProfileImg] = useState("");
    const [newProfileImg, setNewProfileImg] = useState(null);

    // 전화번호 상태
    const [phone1, setPhone1] = useState("");
    const [phone2, setPhone2] = useState("");
    const [phone3, setPhone3] = useState("");

    const phone2Ref = useRef(null);
    const phone3Ref = useRef(null);

    // 비밀번호 변경을 위한 상태
    const [currentPassword, setCurrentPassword] = useState("");
    const [newPassword, setNewPassword] = useState("");
    const [confirmNewPassword, setConfirmNewPassword] = useState("");

    const navigate = useNavigate();

    useEffect(() => {
        fetchUserProfile();
    }, [token]);

    const fetchUserProfile = () => {
        // 사용자 프로필 정보 가져오기
        axios
            .get("http://127.0.0.1:8080/user/profile", {
                headers: {
                    Authorization: `Bearer ${token}`,
                },
            })
            .then((response) => {
                setEmail(response.data.email);
                setProfileImg(`data:image/png;base64,${response.data.profileImg}`);

                // 전화번호 분할
                const phoneParts = response.data.phoneNumber.split("-");
                if (phoneParts.length === 3) {
                    setPhone1(phoneParts[0]);
                    setPhone2(phoneParts[1]);
                    setPhone3(phoneParts[2]);
                }
            })
            .catch((error) => {
                console.error("Error fetching user profile:", error);
                if (error.response && error.response.status === 401) {
                    handleLogout();
                    navigate("/login");
                }
            });
    }

    const handlePhoneInputChange = (e, setPhone, nextInputRef, maxLength) => {
        const value = e.target.value;
        if (/^\d*$/.test(value) && value.length <= maxLength) {
            setPhone(value);
            if (value.length === maxLength && nextInputRef) {
                nextInputRef.current.focus();
            }
        }
    };

    const handleSubmit = async (e) => {
        e.preventDefault();

        // 비밀번호 확인
        if (newPassword !== confirmNewPassword) {
            alert("새 비밀번호가 일치하지 않습니다.");
            return;
        }

        const phoneNumber = `${phone1}-${phone2}-${phone3}`;

        const formData = new FormData();
        formData.append("phoneNumber", phoneNumber);

        if (newProfileImg) {
            formData.append("profileImg", newProfileImg);
        }

        // 비밀번호 변경 정보 추가
        if (currentPassword && newPassword) {
            formData.append("currentPassword", currentPassword);
            formData.append("newPassword", newPassword);
        }

        try {
            const response = await axios.post(
                "http://127.0.0.1:8080/user/profile",
                formData,
                {
                    headers: {
                        Authorization: `Bearer ${token}`,
                        "Content-Type": "multipart/form-data",
                    },
                }
            );
            alert("프로필이 업데이트되었습니다.");
            // 프로필 이미지 업데이트
            setProfileImg(`data:image/png;base64,${response.data.profileImg}`);

            // 비밀번호 입력 필드 초기화
            setCurrentPassword("");
            setNewPassword("");
            setConfirmNewPassword("");

            fetchUserProfile();
            window.location.reload();

        } catch (error) {
            console.error("Error updating profile:", error);
            if (error.response && error.response.data && error.response.data.message) {
                alert(error.response.data.message);
            } else {
                alert("프로필 업데이트 중 오류가 발생했습니다.");
            }
        }
    };

    const handleProfileImgChange = (e) => {
        const file = e.target.files[0];
        setNewProfileImg(file);

        // 미리보기 이미지 설정
        if (file) {
            const imageUrl = URL.createObjectURL(file);
            setProfileImg(imageUrl);
        } else {
            // 파일이 없을 경우 기본 이미지로 설정
            setProfileImg(profileImg);
        }
    };

    const handleDeleteAccount = async () => {
        const confirmed = window.confirm("정말로 회원 탈퇴를 하시겠습니까?");
        if (!confirmed) return;

        try {
            await axios.post("http://127.0.0.1:8080/remove", null, {
                headers: {
                    Authorization: `Bearer ${token}`,
                },
            });
            alert("회원 탈퇴가 완료되었습니다.");
            handleLogout(); // 로그아웃 처리
            navigate("/login"); // 로그인 페이지로 이동
        } catch (error) {
            console.error("Error deleting account:", error);
            alert("회원 탈퇴 중 오류가 발생했습니다.");
        }
    };

    return (
        <main>
            <Header isLoggedIn={true} token={token} handleLogout={handleLogout}/>
            <div className="my-page-container">
                <h2>마이 페이지</h2>
                <form onSubmit={handleSubmit} className="my-page-form">
                    <div className="form-group">
                        <label htmlFor="email">이메일</label>
                        <input type="text" id="email" value={email} disabled/>
                    </div>
                    {/* 비밀번호 변경 섹션 */}
                    <div className="form-group">
                        <label htmlFor="currentPassword">현재 비밀번호</label>
                        <input
                            type="password"
                            id="currentPassword"
                            value={currentPassword}
                            onChange={(e) => setCurrentPassword(e.target.value)}
                            minLength="8"
                            maxLength="20"
                        />
                    </div>
                    <div className="form-group">
                        <label htmlFor="newPassword">새 비밀번호</label>
                        <input
                            type="password"
                            id="newPassword"
                            value={newPassword}
                            onChange={(e) => setNewPassword(e.target.value)}
                            minLength="8"
                            maxLength="20"
                        />
                    </div>
                    <div className="form-group">
                        <label htmlFor="confirmNewPassword">새 비밀번호 확인</label>
                        <input
                            type="password"
                            id="confirmNewPassword"
                            value={confirmNewPassword}
                            onChange={(e) => setConfirmNewPassword(e.target.value)}
                            minLength="8"
                            maxLength="20"
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
                    <div className="form-group">
                        <label htmlFor="profileImg">프로필 이미지</label>
                        <img
                            src={profileImg}
                            alt="프로필 이미지"
                            className="profile-image-preview"
                            style={{
                                width: "150px",
                                height: "150px",
                                borderRadius: "50%",
                                objectFit: "cover",
                                marginBottom: "10px",
                            }}
                        />
                        <input
                            type="file"
                            id="profileImg"
                            onChange={handleProfileImgChange}
                        />
                    </div>

                    <div className="form-actions">
                        <button type="submit" className="update-profile-button">
                            업데이트
                        </button>
                        <span
                            className="delete-account-text"
                            onClick={handleDeleteAccount}
                        >
                        회원 탈퇴
                    </span>
                    </div>
                </form>
            </div>
        </main>
    );
};

export default MyPage;