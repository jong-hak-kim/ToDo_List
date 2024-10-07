import React, {useEffect, useState} from "react";
import axios from "axios";
import {useNavigate} from "react-router-dom";

const ToDoList = ({isLoggedIn, token}) => {
    const [loading, setLoading] = useState(true)
    const [todoList, setTodoList] = useState([])
    const navigate = useNavigate()

    useEffect(() => {
        if (token) {
            fetchToDoList()
        } else {
            setTodoList([])
            setLoading(false);
        }
    }, [token]);

    const fetchToDoList = async () => {
        try {
            const response = await axios.get('http://127.0.0.1:8080/todo', {
                headers: {
                    Authorization: `Bearer ${token}`
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
                        Authorization: `Bearer ${token}`
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
                        Authorization: `Bearer ${token}`
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

    const handleDelete = async (listId) => {
        try {
            const response = await axios.post(`http://127.0.0.1:8080/todo/${listId}/remove`, {}, {
                headers: {
                    Authorization: `Bearer ${token}`
                }
            })
            alert("할 일이 삭제되었습니다.")
            console.log("할 일이 삭제되었습니다: ", response.data);
            navigate(0)
        } catch (error) {
            console.error("Error remove todo: ", error);
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
        <>
            {isLoggedIn ? (
                <div className="todo-board">
                    <button onClick={() => {
                        navigate("/todo/add")
                    }} className="add-todo">할 일 추가
                    </button>
                    <ul className="todo-list">
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
                                <button className={"todo-update"} onClick={() => {
                                    navigate(`/todo/${todo.listId}/update`)
                                }}>수정
                                </button>
                                <button className={"todo-remove"} onClick={() => {
                                    handleDelete(todo.listId)
                                }}>삭제
                                </button>
                            </li>
                        ))}
                    </ul>
                </div>
            ) : (<p className="todo-need-login"> 할 일을 보려면 로그인하세요.</p>)}
        </>
    )
}

export default ToDoList