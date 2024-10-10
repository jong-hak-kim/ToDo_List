import Header from "../components/Header";
import ToDoCalendar from "../components/ToDoCalendar";
import ToDoList from "../components/ToDoList";
import React, {useState} from "react";

function Home({token, handleLogout}) {

    const [selectedDate, setSelectedDate] = useState(new Date());

    return (
        <main>
            <Header isLoggedIn={!!token} handleLogout={handleLogout}/>
            <div className="container">
                <br/>
                <ToDoCalendar selectedDate={selectedDate} setSelectedDate={setSelectedDate}/>
                <br/>
                <ToDoList isLoggedIn={!!token} token={token} selectedDate={selectedDate}/>
            </div>
        </main>
    );
}

export default Home