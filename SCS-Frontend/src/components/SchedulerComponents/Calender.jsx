import React, { useState } from 'react';
import Calendar from 'react-calendar';
import 'react-calendar/dist/Calendar.css';
import DayCell from './DayCell';
import { PageProvider } from '../../providers/PageProvider';

function App() {
  const [selectedDate, setSelectedDate] = useState(new Date());

  const handleCalendarChange = (selectedDate) => {
    setSelectedDate(selectedDate);
  };

  const days = ['Sun', 'Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat'];

  const weekDates = [];
  for (let i = 0; i < 7; i++) {
    const date = new Date(selectedDate);
    date.setDate(date.getDate() - selectedDate.getDay() + i);
    weekDates.push(date);
  }

  // Function to get the start and end dates of the week
  const getWeekDates = (selectedDate) => {
    const currentDate = new Date(selectedDate);
    const weekStart = new Date(currentDate);
    const weekEnd = new Date(currentDate);
    const diff = currentDate.getDay();
    weekStart.setDate(currentDate.getDate() - diff);
    const daysInMonth = [31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31];
    const currentMonth = currentDate.getMonth();
    const lastDateOfMonth = daysInMonth[currentMonth];
    let endOfTheWeek = currentDate.getDate() - diff + 6;
    if (endOfTheWeek > lastDateOfMonth) {
      weekEnd.setMonth(weekEnd.getMonth() + 1);
      weekEnd.setDate(endOfTheWeek - lastDateOfMonth);
    } else {
      weekEnd.setDate(endOfTheWeek);
    }
    return { weekStart, weekEnd };
  };

  const { weekStart, weekEnd } = getWeekDates(selectedDate);

  const events = [
    {
      startTime: 540, // 9:00 AM (in minutes)
      endTime: 600, // 10:00 AM (in minutes)
      title: 'Team Meeting',
    },
    {
      startTime: 720, // 12:00 PM (in minutes)
      endTime: 780, // 1:00 PM (in minutes)
      title: 'Lunch Break',
    },
    {
      startTime: 900, // 3:00 PM (in minutes)
      endTime: 960, // 4:00 PM (in minutes)
      title: 'Client Call',
    },
    {
      startTime: 1020, // 5:00 PM (in minutes)
      endTime: 1080, // 6:00 PM (in minutes)
      title: 'Project Review',
    },
  ];

  return (
<div className="h-screen w-full bg-gray-100 p-6 flex flex-col items-center justify-center">
  <div className="bg-gray-100 rounded-2xl overflow-hidden shadow-lg mb-4 w-full md:w-2/3" style={{ height: '80vh' }}>
    <div className="flex">
      <div className="w-1/3 bg-gray-200 p-4 rounded-2xl mx-2 hidden sm:hidden lg:block">
        <Calendar
          onChange={handleCalendarChange}
          value={selectedDate}
          calendarType="US"
        />
      </div>
      <div className="w-full md:w-2/3 mx-2">
        <div className="bg-gradient-to-r from-blue-300 to-purple-300 bg-opacity-40 p-4 rounded-2xl">
          <div className="bg-white bg-opacity-50 shadow-md rounded-lg p-4 mb-4">
            <div className="block lg:hidden mb-4">
              <input
                type="date"
                className="w-full px-4 py-2 text-gray-900 bg-white border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                value={selectedDate.toISOString().slice(0, 10)}
                onChange={(e) => setSelectedDate(new Date(e.target.value))}
              />
            </div>
            <div className="flex justify-between">
              {days.map((day, index) => {
                const date = new Date(weekStart);
                date.setDate(date.getDate() + index);
                return (
                  <div
                    key={index}
                    className="flex flex-col items-center justify-center w-16"
                  >
                    <p className="text-gray-900 text-sm font-semibold mb-1">{day}</p>
                    <button
                      className="flex items-center justify-center w-10 h-10 rounded-full bg-white text-gray-900 font-bold hover:bg-purple-500 hover:text-white transition-colors duration-300"
                    >
                      {date.getDate()}
                    </button>
                  </div>
                );
              })}
            </div>
          </div>
          <div className="bg-white bg-opacity-50 shadow-md rounded-lg p-4">
            {selectedDate && (
              <p className="text-gray-900 font-semibold mb-4">{selectedDate.toDateString()}</p>
            )}
            <div className="grid grid-cols-7 gap-4">
              {weekDates.map((date, index) => (
                <DayCell
                  key={index}
                  date={date.toISOString().slice(0, 10)}
                  events={events}
                />
              ))}
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</div>


 );
}

export default App; 