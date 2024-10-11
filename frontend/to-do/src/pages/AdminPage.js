import React, { useState } from 'react';
import MemberManage from './MemberManage'
import ToDoListManage from './ToDoListManage';

const AdminPage = (token) => {
    const [activeTab, setActiveTab] = useState('members'); // 기본 탭을 'members'로 설정

    const renderTabContent = () => {
        switch (activeTab) {
            case 'members':
                return <MemberManage />;
            case 'todo':
                return <ToDoListManage />;
            default:
                return <MemberManage />;
        }
    };

    return (
        <div className="admin-page">
            <h2>관리자 페이지</h2>
            <div className="tabs">
                <button onClick={() => setActiveTab('members')} className={activeTab === 'members' ? 'active' : ''}>
                    회원 관리
                </button>
                <button onClick={() => setActiveTab('todo')} className={activeTab === 'todo' ? 'active' : ''}>
                    투두 리스트 관리
                </button>
            </div>
            <div className="tab-content">
                {renderTabContent()}
            </div>
        </div>
    );
};

export default AdminPage;