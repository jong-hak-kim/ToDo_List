import Header from "../components/Header";
import ToDoCalendar from "../components/ToDoCalendar";
import ToDoList from "../components/ToDoList";
import React from "react";

function Home({token, handleLogout}) {

    return (
        <main>
            <Header isLoggedIn={!!token} handleLogout={handleLogout}/>
            <div className="container">
                <br/>
                <ToDoCalendar/>
                <br/>
                <ToDoList isLoggedIn={!!token} token={token}/>
            </div>
        </main>
    );
}

export default Home