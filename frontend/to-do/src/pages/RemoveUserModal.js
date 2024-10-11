import React, {useState} from 'react';

const RemoveUserModal = ({ isOpen, closeModal, onSubmit }) => {
    const [reason, setReason] = useState('');

    if (!isOpen) return null;

    const handleSubmit = () => {
        if (reason) {
            onSubmit(reason);
            closeModal();
        } else {
            alert("사유를 선택해주세요.");
        }
    };

    return (
        <div className="modal-backdrop">
            <div className="modal-content">
                <h3>회원 탈퇴 사유 선택</h3>
                <select className="model-select" value={reason} onChange={(e) => setReason(e.target.value)}>
                    <option value="">사유를 선택하세요</option>
                    <option value="욕설">욕설</option>
                    <option value="비정상적인 활동">비정상적인 활동</option>
                    <option value="스팸 및 광고">스팸 및 광고</option>
                </select>
                <div className="modal-buttons">
                    <button onClick={handleSubmit}>확인</button>
                    <button onClick={closeModal}>취소</button>
                </div>
            </div>
        </div>
    );
};

export default RemoveUserModal;