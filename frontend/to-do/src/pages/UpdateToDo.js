import React, {useState} from "react";
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
    const [startDate, setStartDate] = useState(getTodayDate());
    const [repeatEndDate, setRepeatEndDate] = useState("")

    const navigate = useNavigate()


    const handleSubmit = async (e) => {
        e.preventDefault()

        const checkRepeatEndDate = repeatEndDate === "" ? getTodayDate() : repeatEndDate;

        const updateToDo = {
            title,
            content,
            priority,
            startDate,
            repeatEndDate: checkRepeatEndDate
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
                        <label htmlFor="startDate">시작일</label>
                        <input
                            type="date"
                            id="startDate"
                            value={startDate}
                            onChange={(e) => setStartDate(e.target.value)}
                        />
                    </div>
                    <div className="form-group">
                        <label htmlFor="repeatEndDate">반복 종료일</label>
                        <input
                            type="date"
                            id="repeatEndDate"
                            value={repeatEndDate}
                            onChange={(e) => setRepeatEndDate(e.target.value)}
                        />
                    </div>
                    <button type="submit" className="update-todo-button">수정하기</button>
                </form>
            </div>
        </main>
    );
};

export default UpdateToDo;