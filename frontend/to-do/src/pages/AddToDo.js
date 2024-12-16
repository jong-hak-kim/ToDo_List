import React, {useEffect, useState} from "react";
import axios from "axios";
import {useNavigate} from "react-router-dom";
import Header from "../components/Header";

const AddToDo = ({isLoggedIn, token, handleLogout}) => {

    const getTodayDate = () => {
        const today = new Date();
        const year = today.getFullYear();
        const month = String(today.getMonth() + 1).padStart(2, '0');
        const day = String(today.getDate()).padStart(2, '0');
        return `${year}-${month}-${day}`
    }

    const [title, setTitle] = useState("");
    const [content, setContent] = useState("");
    const [priority, setPriority] = useState("중간");
    const [date, setDate] = useState(getTodayDate());
    const [repeatEndDate, setRepeatEndDate] = useState("")

    const navigate = useNavigate()

    const [profileImageUrl, setProfileImageUrl] = useState("");

    useEffect(() => {
        if (isLoggedIn) {
            axios.get("http://127.0.0.1:8080/profile_img", {
                headers: {
                    Authorization: `Bearer ${token}`,
                },
            })
                .then((response) => {
                    setProfileImageUrl(`data:image/png;base64,${response.data.profileImg}`);
                })
                .catch((error) => {
                    console.error("Error fetching profile image:", error);
                });
        }
    }, [isLoggedIn]);


    const handleSubmit = async (e) => {
        e.preventDefault()

        const checkRepeatEndDate = repeatEndDate === "" ? getTodayDate() : repeatEndDate;

        const newToDo = {
            title,
            content,
            priority,
            date,
            repeatEndDate: checkRepeatEndDate
        }


        try {
            const response = await axios.post("http://127.0.0.1:8080/todo", newToDo, {
                headers: {
                    Authorization: `Bearer ${token}`
                }
            })
            alert("할 일이 추가되었습니다.")
            console.log("할 일이 추가되었습니다: ", response.data);
            navigate("/")
        } catch (error) {
            console.error("Error adding todo: ", error);
        }
    }

    return (
        <main>
            <Header isLoggedIn={!!token} token={token} handleLogout={handleLogout}/>
            <div className="add-todo-page">
                <h2>할 일 추가</h2>
                <form onSubmit={handleSubmit} className="add-todo-form">
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
                            id="date"
                            value={date}
                            onChange={(e) => setDate(e.target.value)}
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
                    <button type="submit" className="add-todo-button">추가하기</button>
                </form>
            </div>
        </main>
    );
};

export default AddToDo;