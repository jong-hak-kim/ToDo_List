// import React, {useState} from "react";
//
// function ToDoItem(props) {
//
//     const [isChecked, setIsChecked] = useState(false)
//
//     const handleCheckboxChange = () => {
//         setIsChecked(!isChecked)
//     }
//
//     return (
//         <div className="todo-item">
//             <input type="checkbox" checked={isChecked} onChange={handleCheckboxChange} className="todo-checkbox"/>
//             <span className={`todo-text ${isChecked ? 'completed' : ''}`}>
//                 {props.item}
//             </span>
//         </div>
//     )
// }
//
// export default ToDoItem