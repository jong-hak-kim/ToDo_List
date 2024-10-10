import React, {useEffect, useState} from "react";
import axios from "axios";
import {useNavigate, useParams} from "react-router-dom";
import Header from "../components/Header";

const UpdateToDo = ({token, handleLogout}) => {

    const getTodayDate = () => {
        const today = new Date();
        const year = today.getFullYear();
        const month = String(today.getMonth() + 1).padStart(2, '0');
        const day = String(today.getDate()).padStart(2, '0');
        return `${year}-${month}-${day}`
    }

    const {listId} = useParams();

    const [title, setTitle] = useState("");
    const [content, setContent] = useState("");
    const [priority, setPriority] = useState("중간");
    const [date, setDate] = useState(getTodayDate());

    const navigate = useNavigate()

    useEffect(() => {
        const fetchToDo = async () => {
            try {
                const response = await axios.get(`http://127.0.0.1:8080/todo/${listId}`, {
                    headers: {
                        Authorization: `Bearer ${token}`
                    }
                });

                const todoData = response.data;
                setTitle(todoData.title);
                setContent(todoData.content);
                setPriority(todoData.priority);
                setDate(todoData.date);

            } catch (error) {
                console.error("Error fetching todo data:", error);
            }
        };

        fetchToDo();
    }, [listId, token]);

    const handleSubmit = async (e) => {
        e.preventDefault()

        const updateToDo = {
            title,
            content,
            priority,
            date
        }


        try {
            const response = await axios.post(`http://127.0.0.1:8080/todo/${listId}/update`, updateToDo, {
                headers: {
                    Authorization: `Bearer ${token}`
                }
            })
            alert("할 일이 수정되었습니다.")
            console.log("할 일이 수정되었습니다: ", response.data);
            navigate("/")
        } catch (error) {
            console.error("Error update todo: ", error);
        }
    }

    return (
        <main>
            <Header isLoggedIn={!!token} handleLogout={handleLogout}/>
            <div className="update-todo-page">
                <h2>할 일 수정</h2>
                <form onSubmit={handleSubmit} className="update-todo-form">
                    <div className="form-group">
                        <label htmlFor="title">제목</label>
                        <input
                            type="text"
                            id="title"
                            value={title}
                            onChange={(e) => setTitle(e.target.value)}
                            required
                        />
                    </div>
                    <div className="form-group">
                        <label htmlFor="content">내용</label>
                        <textarea
                            id="content"
                            value={content}
                            onChange={(e) => setContent(e.target.value)}
                            required
                        />
                    </div>
                    <div className="form-group">
                        <label htmlFor="priority">우선순위</label>
                        <select
                            id="priority"
                            value={priority}
                            onChange={(e) => setPriority(e.target.value)}
                        >
                            <option value="높음">높음</option>
                            <option value="중간">중간</option>
                            <option value="낮음">낮음</option>
                        </select>
                    </div>
                    <div className="form-group">
                        <label htmlFor="date">날짜</label>
                        <input
                            type="date"
                            id="date"
                            value={date}
                            onChange={(e) => setDate(e.target.value)}
                        />
                    </div>
                    <button type="submit" className="update-todo-button">수정하기</button>
                </form>
            </div>
        </main>
    );
};

export default UpdateToDo;