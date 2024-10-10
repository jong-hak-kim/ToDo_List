import React, {useEffect, useState} from "react";
import axios from "axios";
import {useNavigate} from "react-router-dom";
import {jwtDecode} from "jwt-decode"

const ToDoList = ({isLoggedIn, token}) => {
    const [loading, setLoading] = useState(true)
    const [todoList, setTodoList] = useState([])
    const [selectedTodoId, setSelectedTodoId] = useState(null);
    const [comments, setComments] = useState([]);
    const [user, setUser] = useState(null);
    const navigate = useNavigate()
    const [editCommentId, setEditCommentId] = useState(null);
    const [editCommentContent, setEditCommentContent] = useState("");

    useEffect(() => {
        if (token) {
            const decoded = jwtDecode(token);
            setUser(decoded);
            fetchToDoList()

            const storedToDoId = localStorage.getItem("selectedToDoId")
            if (storedToDoId) {
                fetchComments(Number(storedToDoId))
            }
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

    const fetchComments = async (todoId) => {
        try {
            const response = await axios.get(`http://127.0.0.1:8080/todo/${todoId}/comment`, {
                headers: {
                    Authorization: `Bearer ${token}`
                }
            });
            setComments(response.data.comments); // 응답에서 댓글 데이터 설정
            setSelectedTodoId(todoId); // 선택된 할 일 ID 업데이트
            setEditCommentContent('')

            localStorage.setItem("selectedToDoId", todoId)
        } catch (error) {
            console.error("Error fetching comments: ", error);
        }
    };


    const handleEditComment = (commentId, currentContent) => {
        // 수정 모드로 전환하며, 수정할 댓글의 내용을 상태에 저장
        setEditCommentId(commentId);
        setEditCommentContent(currentContent);
    };

    const handleSaveEditComment = async (commentId) => {
        try {
            const response = await axios.post(`http://127.0.0.1:8080/todo/comment/${commentId}/modify`,
                {content: editCommentContent},
                {
                    headers: {
                        Authorization: `Bearer ${token}`
                    }
                }
            );

            // 댓글 수정 성공 시 상태 업데이트
            setComments(prevComments =>
                prevComments.map(comment =>
                    comment.commentId === commentId ? {...comment, content: editCommentContent} : comment
                )
            );

            fetchComments(selectedTodoId);

            setEditCommentId(null); // 수정 모드 종료
            setEditCommentContent(""); // 수정 중인 내용 초기화

            alert("댓글이 수정되었습니다.");
            console.log("댓글이 수정되었습니다: ", response.data);
        } catch (error) {
            console.error("Error updating comment: ", error);
        }
    };

    const handleDeleteComment = async (commentId) => {
        try {
            const response = await axios.post(`http://127.0.0.1:8080/todo/comment/${commentId}/remove`, {}, {
                headers: {
                    Authorization: `Bearer ${token}`
                }
            })

            alert("댓글이 삭제되었습니다.")
            console.log("댓글이 삭제되었습니다: ", response.data);
            navigate(0)
        } catch (error) {
            console.error("Error deleting comments: ", error);
        }
    }

    const handleToDoClick = (id) => {
        if (selectedTodoId === id) {
            setSelectedTodoId(null); // 이미 선택된 할 일이면 해제
            localStorage.removeItem("selectedToDoId")
        } else {
            fetchComments(id); // 할 일 클릭 시 댓글 불러오기
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

    const handleDeleteToDo = async (listId) => {
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

    const handleSaveNewComment = async (id) => {
        try {
            const newComment = {
                toDoListId: id,
                parentCommentId: null,
                content: editCommentContent
            }

            const response = await axios.post(`http://127.0.0.1:8080/todo/comment`, newComment, {
                headers: {
                    Authorization: `Bearer ${token}` // 필요한 경우 Authorization 헤더 추가
                }
            });

            fetchComments(id);
            setEditCommentContent('')
        } catch (error) {
            console.error("Error save new comment", error)
        }
    }

    const formatDate = (dateString) => {
        const date = new Date(dateString)
        const year = date.getFullYear()
        const month = String(date.getMonth() + 1).padStart(2, '0')
        const day = String(date.getDate()).padStart(2, '0')
        const hours = String(date.getHours()).padStart(2, '0')
        const minutes = String(date.getMinutes()).padStart(2, '0')
        const seconds = String(date.getSeconds()).padStart(2, '0')

        return `${year}-${month}-${day} ${hours}:${minutes}:${seconds}`;
    }


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
                                    <div className="todo-item">
                                        <div className="todo-header">
                                            <input
                                                type="checkbox"
                                                checked={todo.completionStatus}
                                                onChange={(e) => handleCheckboxChange(todo.listId, e.target.checked)}
                                                className="todo-checkbox"
                                            />
                                            <span className={`todo-text ${todo.completionStatus ? 'completed' : ''}`}
                                                  onClick={() => handleToDoClick(todo.listId)}>
                                             {todo.title}
                                        </span>
                                            <div className="todo-actions">
                                                <button className={"todo-update"} onClick={() => {
                                                    navigate(`/todo/${todo.listId}/update`)
                                                }}>수정
                                                </button>
                                                <button className={"todo-remove"} onClick={() => {
                                                    handleDeleteToDo(todo.listId)
                                                }}>삭제
                                                </button>
                                            </div>
                                        </div>


                                        {selectedTodoId === todo.listId && (
                                            <ul className="comments-list">
                                                {comments.length > 0 && comments.map(comment => (
                                                    <li key={comment.commentId}>
                                                        <div className="comment-body">
                                                            <div className="comment-header">
                                                                {editCommentId === comment.commentId ? (
                                                                    <input
                                                                        type="text"
                                                                        value={editCommentContent}
                                                                        onChange={(e) => setEditCommentContent(e.target.value)}
                                                                        className="edit-comment-input"
                                                                    />
                                                                ) : (
                                                                    <>
                                                                        <strong>{comment.email}:</strong> {comment.content}
                                                                    </>
                                                                )}
                                                            </div>
                                                            <div className="comment-footer">
                                                                {editCommentId !== comment.commentId && (
                                                                    <span
                                                                        className="comment-time">{formatDate(comment.creationDate)}</span>
                                                                )}
                                                                {comment.email === user?.email && (
                                                                    <div className="comment-actions">
                                                                        {editCommentId === comment.commentId ? (
                                                                            <button
                                                                                onClick={() => handleSaveEditComment(comment.commentId)}>수정완료</button>
                                                                        ) : (
                                                                            <>
                                                                                <button
                                                                                    onClick={() => handleEditComment(comment.commentId, comment.content)}>수정
                                                                                </button>
                                                                                <button
                                                                                    onClick={() => handleDeleteComment(comment.commentId)}>삭제
                                                                                </button>
                                                                            </>
                                                                        )}
                                                                    </div>
                                                                )}
                                                            </div>
                                                        </div>
                                                    </li>
                                                ))}
                                                <div className="new-comment">
                                                    <input
                                                        type="text"
                                                        placeholder="댓글을 입력하세요"
                                                        value={editCommentContent} // 새로운 댓글 입력 상태로 설정
                                                        onChange={(e) => setEditCommentContent(e.target.value)}
                                                        className="new-comment-input"
                                                    />
                                                    <button className="new-comment-button" onClick={() => handleSaveNewComment(todo.listId)}>댓글
                                                        추가
                                                    </button>
                                                </div>
                                            </ul>
                                        )}
                                    </div>
                                </li>
                            ))}
                        </ul>
                    </div>
                ) :
                (<p className="todo-need-login"> 할 일을 보려면 로그인하세요.</p>)
            }
        </>
    )
}

export default ToDoList