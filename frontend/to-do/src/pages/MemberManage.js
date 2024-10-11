import React, {useEffect, useState} from 'react';
import axios from "axios";
import RemoveUserModal from "./RemoveUserModal";
import DeactivationModal from "./DeactivationModal";

const MemberManage = () => {
    const [users, setUsers] = useState([]); // 회원 목록 상태
    const [loading, setLoading] = useState(true); // 로딩 상태
    const [error, setError] = useState(''); // 에러 상태
    const [isRemoveModalOpen, setIsRemoveModalOpen] = useState(false); // 회원 탈퇴 모달 상태
    const [isDeactivationModalOpen, setIsDeactivationModalOpen] = useState(false); // 활동 정지 모달 상태
    const [selectedUser, setSelectedUser] = useState(null); // 선택된 회원

    // 회원 목록을 받아오는 함수
    const fetchUserList = async () => {

        const token = localStorage.getItem("adminToken")

        if (!token) {
            setError("인증 토큰이 없습니다. 다시 로그인해주세요.")
            setLoading(false)
            return;
        }

        try {
            const response = await axios.get('http://127.0.0.1:8080/admin/user',
                {
                    headers: {
                        Authorization: `Bearer ${token}`
                    }
                }
            );
            if (response.status === 200) {
                setUsers(response.data.users); // 유저 데이터 설정
                setLoading(false); // 로딩 완료
            }
        } catch (error) {
            setError('회원 목록을 가져오는 데 실패했습니다.');
            setLoading(false);
        }
    };

    const resetPassword = async (email) => {

        const token = localStorage.getItem("adminToken")

        if (!token) {
            setError("인증 토큰이 없습니다. 다시 로그인해주세요.")
            setLoading(false)
            return;
        }

        try {
            const response = await axios.post('http://127.0.0.1:8080/admin/password',
                {email: email},
                {
                    headers: {
                        Authorization: `Bearer ${token}`
                    }
                }
            );
            if (response.status === 200) {
                alert("비밀번호 재발급하였습니다")
                setLoading(false); // 로딩 완료
            }
        } catch (error) {
            setError('비밀번호 재발급에 실패했습니다.');
            setLoading(false);
        }
    };

    const removeUser = async (email, reason) => {

        const token = localStorage.getItem("adminToken")

        if (!token) {
            setError("인증 토큰이 없습니다. 다시 로그인해주세요.")
            setLoading(false)
            return;
        }

        try {
            const response = await axios.post('http://127.0.0.1:8080/admin/remove',
                {
                    email: email, reason: reason
                },
                {
                    headers: {
                        Authorization: `Bearer ${token}`
                    }
                }
            );
            if (response.status === 200) {
                alert(`${email} 회원을 탈퇴하였습니다`)
                setLoading(false); // 로딩 완료
                setUsers(users.filter(user => user.email !== email))
            }
        } catch (error) {
            setError('회원 탈퇴에 실패했습니다.');
            setLoading(false);
        }
    };

    const openRemoveModal = (user) => {
        setSelectedUser(user);
        setIsRemoveModalOpen(true);
    };

    const openDeactivationModal = (user) => {
        setSelectedUser(user);
        setIsDeactivationModalOpen(true);
    };

    const closeModal = () => {
        setIsRemoveModalOpen(false);
        setIsDeactivationModalOpen(false);
        setSelectedUser(null);
    };

    const handleRemoveSubmit = (reason) => {
        if (selectedUser) {
            removeUser(selectedUser.email, reason);
        }
    };

    // 컴포넌트가 마운트될 때 회원 목록을 가져옴
    useEffect(() => {
        fetchUserList();
    }, []);

    if (loading) {
        return <p>로딩 중...</p>; // 로딩 상태 표시
    }

    if (error) {
        return <p>{error}</p>; // 에러 메시지 표시
    }

    return (
        <div>
            <h3>회원 관리</h3>
            <table className="user-table">
                <thead>
                <tr>
                    <th>이메일</th>
                    <th>권한</th>
                    <th>전화번호</th>
                    <th>활동 정지 기간</th>
                    <th>활성화 여부</th>
                    <th>비밀번호</th>
                    <th>활동 정지</th>
                    <th>회원 삭제</th>
                </tr>
                </thead>
                <tbody>
                {users.map((user, index) => (
                    <tr key={index}>
                        <td>{user.email}</td>
                        <td>{user.role}</td>
                        <td>{user.phoneNumber}</td>
                        <td>{user.deactivationDate ? new Date(user.deactivationDate).toLocaleString() : 'N/A'}</td>
                        <td>{user.isActive ? '활성화' : '비활성화'}</td>
                        <td>
                            <button onClick={() =>
                                resetPassword(user.email)
                            }>비밀번호 재발급
                            </button>
                        </td>
                        <td>
                            <button onClick={() =>
                                openDeactivationModal(user)
                            }>활동 정지
                            </button>
                        </td>
                        <td>
                            <button onClick={() =>
                                openRemoveModal(user)
                            }>회원 탈퇴
                            </button>
                        </td>
                    </tr>
                ))}
                </tbody>
            </table>

            <RemoveUserModal
                isOpen={isRemoveModalOpen}
                closeModal={closeModal}
                onSubmit={handleRemoveSubmit}
            />

            <DeactivationModal
                isOpen={isDeactivationModalOpen}
                onClose={closeModal}
                userEmail={selectedUser?.email} // 선택된 유저의 이메일 전달
            />
        </div>
    );
};

export default MemberManage;