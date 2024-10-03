import React, {useState} from "react";
import 'react-calendar/dist/Calendar.css';
import './App.css';
import ToDoCalendar from "./components/ToDoCalendar";
import ToDoList from "./components/ToDoList";

function App() {

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
                    <ToDoCalendar/>
                    <br/>
                </div>
                <ToDoList/>
            </div>
        </main>
    );
}

export default App;
