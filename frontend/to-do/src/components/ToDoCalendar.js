import React, {useState} from "react";
import Calendar from "react-calendar";
import 'react-calendar/dist/Calendar.css';
import moment from "moment/moment";

function ToDoCalendar({selectedDate, setSelectedDate}) {

    return (
        <div className="calendar-container">
            <Calendar
                onChange={setSelectedDate}
                value={selectedDate}
                formatDay={(locale, date) => moment(date).format("D")}
                formatYear={(locale, date) => moment(date).format("YYYY")}
                formatMonthYear={(locale, date) => moment(date).format("YYYY. MM")}
                calendarType="gregory"
                showNeighboringMonth={false}
                next2Label={null}
                prev2Label={null}
                minDetail="year"
            />
        </div>
    )
}

export default ToDoCalendar