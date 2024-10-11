import React, { useState } from 'react';
import axios from 'axios';

const DeactivationModal = ({ isOpen, onClose, userEmail }) => {
    const [reason, setReason] = useState('');
    const [deactivateDate, setDeactivateDate] = useState('');

    if (!isOpen) return null;

    const handleSubmit = async () => {
        if (!reason || !deactivateDate) {
            alert('사유와 날짜를 입력해주세요.');
            return;
        }

        const token = localStorage.getItem('adminToken');

        if (!token) {
            alert('인증 토큰이 없습니다. 다시 로그인 해주세요.');
            return;
        }

        try {
            const response = await axios.post(
                'http://127.0.0.1:8080/admin/deactivate',
                {
                    email: userEmail,
                    reason: reason,
                    deactivateDate: deactivateDate
                },
                {
                    headers: {
                        Authorization: `Bearer ${token}`
                    }
                }
            );

            if (response.status === 200) {
                alert(`${userEmail} 님의 활동이 정지되었습니다.`);
                onClose(); // 모달 닫기
            }
        } catch (error) {
            alert('활동 정지에 실패했습니다.');
        }
    };

    return (
        <div className="modal-backdrop">
            <div className="modal-content">
                <h3>회원 활동 정지</h3>
                <p>{userEmail} 회원의 활동을 정지하려면 사유와 날짜를 입력하세요.</p>

                <div className="form-group">
                    <label htmlFor="reason">정지 사유</label>
                    <input
                        type="text"
                        id="reason"
                        value={reason}
                        onChange={(e) => setReason(e.target.value)}
                        placeholder="정지 사유를 입력하세요"
                    />
                </div>

                <div className="form-group">
                    <label htmlFor="deactivateDate">정지 날짜</label>
                    <input
                        type="datetime-local"
                        id="deactivateDate"
                        value={deactivateDate}
                        onChange={(e) => setDeactivateDate(e.target.value)}
                    />
                </div>

                <div className="modal-buttons">
                    <button onClick={handleSubmit}>확인</button>
                    <button onClick={onClose}>취소</button>
                </div>
            </div>
        </div>
    );
};

export default DeactivationModal;