import React, {useState, useEffect} from "react";
import axios from "axios";
import ToDoBoard from "./components/ToDoBoard";
import Calendar from "react-calendar";
import 'react-calendar/dist/Calendar.css';
import './App.css';
import moment from "moment";

function App() {

    const [inputValue, setInputValue] = useState('')
    const [todoList, setTodoList] = useState([])
    const [loading, setLoading] = useState(true)
    const [value, onChange] = useState(new Date()); // 초기값은 현재 날짜
    const [isChecked, setIsChecked] = useState(false)

    const handleCheckboxChange = (id) => {
        setTodoList(todoList.map(todo =>
            todo.listId === id ? {...todo, completionStatus: !todo.completionStatus} : todo))
    }

    useEffect(() => {
        const fetchToDoList = async () => {
            try {
                const response = await axios.get('http://127.0.0.1:8080/todo', {
                    headers: {
                        Authorization: "Bearer eyJhbGciOiJIUzI1NiJ9.eyJpYXQiOjE3Mjc4NjM2OTksImV4cCI6MTcyNzg2NzI5OSwiZW1haWwiOiJqb25naHl1azEyMzRAZ21haWwuY29tIiwicm9sZSI6InVzZXIifQ.GdYiFCSlywEA-wk8Uxn3aTCrco1cxJ2vTblJbh82g8M"
                    }
                })
                setTodoList(response.data.filterToDoList)
                setLoading(false)
            } catch (error) {
                console.error("error fetching todo list: ", error);
                setLoading(false)
            }
        };
        fetchToDoList()
    }, []);

    return (
        <main>
            <header className="header">
                <h1>ToDo List</h1>
                <div className="auth-buttons">
                    <button className="auth-button">로그인</button>
                    <button className="auth-button">회원가입</button>
                </div>
            </header>
            <div className="container">
                <br/>
                <div className="calendar-container">
                    <Calendar
                        onChange={onChange}
                        value={value}
                        formatDay={(locale, date) => moment(date).format("D")}
                        formatYear={(locale, date) => moment(date).format("YYYY")}
                        formatMonthYear={(locale, date) => moment(date).format("YYYY. MM")}
                        calendarType="gregory"
                        showNeighboringMonth={false}
                        next2Label={null}
                        prev2Label={null}
                        minDetail="year"
                    />
                    <br/>
                </div>
                <div className="todo-board">
                    <ul>
                    {todoList.map(todo => (
                        <li key={todo.listId}>
                            <input
                                type="checkbox"
                                checked={todo.completionStatus}
                                onChange={() => handleCheckboxChange(todo.listId)}
                                className="todo-checkbox"
                            />
                            <span className={`todo-text ${todo.completionStatus ? 'completed' : ''}`}>
                                    {todo.title}
                                </span>
                        </li>
                    ))}
                    </ul>
                </div>
            </div>
        </main>
    );
}

export default App;
