import React, {useEffect, useState} from "react";
import axios from "axios";

const ToDoList = () => {
    const [loading, setLoading] = useState(true)
    const [todoList, setTodoList] = useState([])
    useEffect(() => {
        fetchToDoList()
    }, []);

    const fetchToDoList = async () => {
        try {
            const response = await axios.get('http://127.0.0.1:8080/todo', {
                headers: {
                    Authorization: "Bearer eyJhbGciOiJIUzI1NiJ9.eyJpYXQiOjE3Mjc5NTUxODYsImV4cCI6MTcyNzk1ODc4NiwiZW1haWwiOiJqb25naHl1azEyMzRAZ21haWwuY29tIiwicm9sZSI6InVzZXIifQ.5z0SO7BeaaVQg-SQP4q7cBUGirX1oAMfjyiHhvCHvoc"
                }
            })
            setTodoList(response.data.filterToDoList)
            setLoading(false)
        } catch (error) {
            console.error("error fetching todo list: ", error);
            setLoading(false)
        }
    };

    const completeToDo = async (id) => {
        try {
            const response = await axios.post("http://127.0.0.1:8080/todo/complete",
                {listId: id},
                {
                    headers: {
                        Authorization: "Bearer eyJhbGciOiJIUzI1NiJ9.eyJpYXQiOjE3Mjc5NTUxODYsImV4cCI6MTcyNzk1ODc4NiwiZW1haWwiOiJqb25naHl1azEyMzRAZ21haWwuY29tIiwicm9sZSI6InVzZXIifQ.5z0SO7BeaaVQg-SQP4q7cBUGirX1oAMfjyiHhvCHvoc"
                    }
                }
            );
            console.log("complete response : ", response.data);

            setTodoList(prevTodoList =>
                prevTodoList.map(todo =>
                    todo.listId === id ? {...todo, completionStatus: true} : todo
                )
            );
        } catch
            (error) {
            console.error("Error completing todo:", error);
        }
    }

    const cancelCompleteToDo = async (id) => {
        try {
            const response = await axios.post("http://127.0.0.1:8080/todo/cancel", {listId: id},
                {
                    headers: {
                        Authorization: "Bearer eyJhbGciOiJIUzI1NiJ9.eyJpYXQiOjE3Mjc5NTUxODYsImV4cCI6MTcyNzk1ODc4NiwiZW1haWwiOiJqb25naHl1azEyMzRAZ21haWwuY29tIiwicm9sZSI6InVzZXIifQ.5z0SO7BeaaVQg-SQP4q7cBUGirX1oAMfjyiHhvCHvoc"
                    }
                }
            );
            console.log("Cancel response: ", response.data);

            setTodoList(prevTodoList =>
                prevTodoList.map(todo =>
                    todo.listId === id ? {...todo, completionStatus: false} : todo
                )
            );
        } catch (error) {
            console.error("Error cancelling complete todo: ", error);
        }
    }

    const handleCheckboxChange = (id, isChecked) => {
        if (isChecked) {
            completeToDo(id);
        } else {
            cancelCompleteToDo(id);
        }
    };

    return (
        <div className="todo-board">
            <ul>
                {todoList.map(todo => (
                    <li key={todo.listId}>
                        <input
                            type="checkbox"
                            checked={todo.completionStatus}
                            onChange={(e) => handleCheckboxChange(todo.listId, e.target.checked)}
                            className="todo-checkbox"
                        />
                        <span className={`todo-text ${todo.completionStatus ? 'completed' : ''}`}>
                                    {todo.title}
                                </span>
                    </li>
                ))}
            </ul>
        </div>
    )
}

export default ToDoList