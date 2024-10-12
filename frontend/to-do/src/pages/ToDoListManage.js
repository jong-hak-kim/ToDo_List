import React, {useEffect, useState} from 'react';
import axios from "axios";

const ToDoListManagement = () => {

    const [toDoLists, setToDoLists] = useState([]);
    const [loading, setLoading] = useState(true); // 로딩 상태
    const [error, setError] = useState(''); // 에러 상태

    const formatDate = (dateString) => {
        const date = new Date(dateString);
        const year = date.getFullYear();
        const month = String(date.getMonth() + 1).padStart(2, '0');
        const day = String(date.getDate()).padStart(2, '0');
        const hours = String(date.getHours()).padStart(2, '0');
        const minutes = String(date.getMinutes()).padStart(2, '0');
        const seconds = String(date.getSeconds()).padStart(2, '0');
        return `${year}-${month}-${day} ${hours}:${minutes}:${seconds}`;
    };

    // 할 일목록을 받아오는 함수
    const fetchToDoList = async () => {

        const token = localStorage.getItem("adminToken")

        if (!token) {
            setError("인증 토큰이 없습니다. 다시 로그인해주세요.")
            setLoading(false)
            return;
        }

        try {
            const response = await axios.get('http://127.0.0.1:8080/admin/todo',
                {
                    headers: {
                        Authorization: `Bearer ${token}`
                    }
                }
            );
            if (response.status === 200) {
                setToDoLists(response.data.toDoLists); // 유저 데이터 설정
                setLoading(false); // 로딩 완료
            }
        } catch (error) {
            setError('투두 리스트 목록을 가져오는 데 실패했습니다.');
            setLoading(false);
        }
    };

    useEffect(() => {
        fetchToDoList();
    }, []);

    if (loading) {
        return <p>로딩 중...</p>; // 로딩 상태 표시
    }

    if (error) {
        return <p>{error}</p>; // 에러 메시지 표시
    }

    return (
        <div>
            <h3>투두 리스트 관리</h3>
            <table className="user-table">
                <thead>
                <tr>
                    <th>제목</th>
                    <th>이메일</th>
                    <th>날짜</th>
                    <th>작성 날짜</th>
                    <th>할 일 삭제</th>
                </tr>
                </thead>
                <tbody>
                {toDoLists.map((toDoList, index) => (
                    <tr key={toDoList.listId}>
                        <td>{toDoList.title}</td>
                        <td>{toDoList.email}</td>
                        <td>{formatDate(toDoList.creationDate)}</td>
                        <td>{toDoList.date}</td>
                        <td>
                            <button>할 일 삭제
                            </button>
                        </td>
                    </tr>
                ))}
                </tbody>
            </table>
        </div>
    );
};

export default ToDoListManagement;