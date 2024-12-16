import Header from "../components/Header";
import ToDoCalendar from "../components/ToDoCalendar";
import React, {useState} from "react";
import SearchToDoList from "../components/SearchToDoList";

function SearchToDo({token, handleLogout}) {

    const [selectedDate, setSelectedDate] = useState(new Date());

    return (
        <main>
            <Header isLoggedIn={!!token} token={token} handleLogout={handleLogout}/>
            <div className="title">
                <h2>탐색하기</h2>
            </div>
            <div className="container">
                <br/>
                <ToDoCalendar selectedDate={selectedDate} setSelectedDate={setSelectedDate}/>
                <br/>
                <SearchToDoList isLoggedIn={!!token} token={token} selectedDate={selectedDate}/>
            </div>
        </main>
    );
}

export default SearchToDo