import React, {useEffect, useState} from "react";
import axios from "axios";
import {useNavigate} from "react-router-dom";
import {jwtDecode} from "jwt-decode"
import moment from "moment/moment";

const SearchToDoList = ({isLoggedIn, token, selectedDate}) => {
    const [loading, setLoading] = useState(true)
    const [selectedTodoId, setSelectedTodoId] = useState(null);
    const [comments, setComments] = useState([]);
    const navigate = useNavigate()
    const [editCommentId, setEditCommentId] = useState(null);
    const [editCommentContent, setEditCommentContent] = useState("");
    const [newCommentContent, setNewCommentContent] = useState("");

    const [users, setUsers] = useState([]); // 모든 유저 저장
    const [currentUserEmail, setCurrentUserEmail] = useState(null); // 현재 로그인한 유저 ID
    const [todosByUser, setTodosByUser] = useState({}); // 각 유저의 할 일 목록 저장

    const fetchUsers = async () => {
        try {
            const response = await axios.get("http://127.0.0.1:8080/todo/search/users", {
                headers: {
                    Authorization: `Bearer ${token}`,
                },
            });
            setUsers(response.data.users); // 유저 목록 저장
        } catch (error) {
            console.error("유저 목록을 가져오는 중 오류 발생: ", error);
        }
    };

    const fetchToDoListByUser = async (email) => {
        try {
            const response = await axios.get(`http://127.0.0.1:8080/todo/other?email=${email}&date=${moment(selectedDate).format("YYYY-MM-DD")}`, {
                headers: {
                    Authorization: `Bearer ${token}`,
                },
            });
            return {email, todos: response.data.filterToDoList}; // 유저 ID와 할 일 목록 반환
        } catch (error) {
            console.error(`유저 ${email}의 할 일 목록을 가져오는 중 오류 발생: `, error);
            return {email, todos: []}; // 오류 발생 시 빈 목록 반환
        }
    };

    // 모든 유저의 할 일 목록을 한 번에 가져오는 함수
    const fetchAllUsersToDoLists = async () => {
        try {
            const todoPromises = users.map((user) => fetchToDoListByUser(user.email));
            const todoResults = await Promise.all(todoPromises);
            const todosMap = {};

            todoResults.forEach(({email, todos}) => {
                todosMap[email] = todos;
            });

            setTodosByUser(todosMap);
            setLoading(false);
        } catch (error) {
            console.error("할 일 목록을 가져오는 중 오류 발생: ", error);
            setLoading(false);
        }
    };

    useEffect(() => {
        if (token) {
            const storedToken = localStorage.getItem("token");
            if (storedToken && !token) {
                const decoded = jwtDecode(token);
                setCurrentUserEmail(decoded.email);
                fetchUsers().then(() => {
                    fetchAllUsersToDoLists();
                });
            } else if (token) {
                const decoded = jwtDecode(token);
                setCurrentUserEmail(decoded.email);
                fetchUsers().then(() => {
                    fetchAllUsersToDoLists();
                });
            }
        } else {
            setLoading(false);
        }
    }, [token, selectedDate]);

    useEffect(() => {
        if (users.length > 0) {
            fetchAllUsersToDoLists(); // 유저 목록과 할 일 목록을 바로 가져오기
        }
    }, [users, selectedDate]);

    const fetchComments = async (todoId) => {
        try {
            const response = await axios.get(`http://127.0.0.1:8080/todo/${todoId}/comment`, {
                headers: {
                    Authorization: `Bearer ${token}`
                }
            });
            setComments(response.data.comments); // 응답에서 댓글 데이터 설정
            setSelectedTodoId(todoId); // 선택된 할 일 ID 업데이트
            setNewCommentContent('')

            localStorage.setItem("selectedToDoId", todoId)
        } catch (error) {
            console.error("Error fetching comments: ", error);
        }
    };


    const handleEditComment = (commentId, currentContent) => {
        setEditCommentId(commentId);
        setEditCommentContent(currentContent);
    };

    const handleSaveEditComment = async (commentId) => {
        try {
            const response = await axios.post(`http://127.0.0.1:8080/todo/comment/${commentId}/modify`, {content: editCommentContent}, {
                headers: {
                    Authorization: `Bearer ${token}`
                }
            });

            // 댓글 수정 성공 시 상태 업데이트
            setComments(prevComments => prevComments.map(comment => comment.commentId === commentId ? {
                ...comment,
                content: editCommentContent
            } : comment));

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


    const handleSaveNewComment = async (id) => {
        try {
            const newComment = {
                toDoListId: id, parentCommentId: null, content: newCommentContent
            }

            const response = await axios.post(`http://127.0.0.1:8080/todo/comment`, newComment, {
                headers: {
                    Authorization: `Bearer ${token}` // 필요한 경우 Authorization 헤더 추가
                }
            });

            fetchComments(id);
            setNewCommentContent('')
        } catch (error) {
            console.error("Error save new comment", error)
        }
    }

    const handleToDoClick = (todoId) => {
        if (selectedTodoId === todoId) {
            // 이미 열려 있는 경우 다시 클릭하면 닫기
            setSelectedTodoId(null);
        } else {
            // 새롭게 할 일을 클릭하면 해당 댓글 불러오기
            fetchComments(todoId);
        }
    };

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
            {loading ? (
                <p>할 일 목록을 불러오는 중..</p>
            ) : (
                <div className="todo-board">
                    {users
                        .filter(user => todosByUser[user.email] && todosByUser[user.email].length > 0)
                        .map(user => (
                            <div key={user.email} className="user-todo-section">
                                <h3>{user.email}의 할 일 목록</h3>
                                <ul className="todo-list">
                                    {todosByUser[user.email].map((todo) => (
                                        <li key={todo.listId}>
                                            <div className="todo-item">
                                                <span
                                                    className="todo-text"
                                                    onClick={() => handleToDoClick(todo.listId)}
                                                >
                                                    {todo.title}
                                                </span>

                                                {selectedTodoId === todo.listId && (
                                                    <>
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
                                                                                <span className="comment-time">
                                                                                {formatDate(comment.creationDate)}
                                                                            </span>
                                                                            )}
                                                                            {comment.email === currentUserEmail && (
                                                                                <div className="comment-actions">
                                                                                    {editCommentId === comment.commentId ? (
                                                                                        <button
                                                                                            onClick={() => handleSaveEditComment(comment.commentId)}>
                                                                                            수정완료
                                                                                        </button>
                                                                                    ) : (
                                                                                        <>
                                                                                            <button
                                                                                                onClick={() => handleEditComment(comment.commentId, comment.content)}>
                                                                                                수정
                                                                                            </button>
                                                                                            <button
                                                                                                onClick={() => handleDeleteComment(comment.commentId)}>
                                                                                                삭제
                                                                                            </button>
                                                                                        </>
                                                                                    )}
                                                                                </div>
                                                                            )}
                                                                        </div>
                                                                    </div>
                                                                </li>
                                                            ))}
                                                        </ul>
                                                        {/* 댓글 input 창은 할 일 클릭 시 표시 */}
                                                        <div className="new-comment">
                                                            <input
                                                                type="text"
                                                                placeholder="댓글을 입력하세요"
                                                                value={newCommentContent}
                                                                onChange={(e) => setNewCommentContent(e.target.value)}
                                                                className="new-comment-input"
                                                            />
                                                            <button
                                                                className="new-comment-button"
                                                                onClick={() => handleSaveNewComment(todo.listId)}
                                                            >
                                                                댓글 추가
                                                            </button>
                                                        </div>
                                                    </>
                                                )}
                                            </div>
                                        </li>
                                    ))}
                                </ul>
                            </div>
                        ))}
                </div>
            )}
        </>
    );
};

export default SearchToDoList