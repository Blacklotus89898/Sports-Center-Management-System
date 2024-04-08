import React, { useState } from 'react';
import Calendar from 'react-calendar';
import 'react-calendar/dist/Calendar.css';
import DayCell from './DayCell';

function App() {
  const [selectedDate, setSelectedDate] = useState(new Date());

  const handleCalendarChange = (selectedDate) => {
    setSelectedDate(selectedDate);
  };

  const days = ['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun'];

  const weekDates = [];
  for (let i = 0; i < 7; i++) {
    const date = new Date(selectedDate);
    date.setDate(date.getDate() + 1 - selectedDate.getDay() + i);
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
    <div className="h-screen bg-gray-100 p-6 flex flex-col md:flex-row">
      <div className="flex-grow-0 md:flex-grow-1 md:w-1/3 mr-0 md:mr-4">
        <div className="bg-white p-4 rounded shadow-lg">
          <Calendar onChange={handleCalendarChange} value={selectedDate} />
        </div>
      </div>
      <div className="flex-grow md:flex-grow-2 md:w-2/3 bg-white shadow-md rounded-lg overflow-x-scroll py-4 px-2 md:ml-4">
        <div className='flex bg-white shadow-md justify-start md:justify-center rounded-lg overflow-x-scroll mx-auto py-4 px-2 md:mx-12 mb-10'>
          <div className="flex">
            {days.map((day, index) => {
              const date = new Date(weekStart);
              date.setDate(date.getDate() + index + 1);
              return (
                <button
                  key={index}
                  className='flex group hover:bg-purple-500 hover:shadow-lg hover-dark-shadow rounded-full mx-1 transition-all duration-300 cursor-pointer justify-center w-16'
                >
                  <div className='flex items-center px-4 py-4'>
                    <div className='text-center'>
                      <p className='text-gray-900 group-hover:text-gray-100 text-sm transition-all group-hover:font-semibold duration-300'>{day}</p>
                      <p className='text-gray-900 group-hover:text-gray-100 mt-3 group-hover:font-bold transition-all duration-300'>{date.getDate()}</p>
                    </div>
                  </div>
                </button>
              );
            })}
          </div>
        </div>
        {selectedDate && (
          <p className="text-gray-900 font-semibold">{selectedDate.toDateString()}</p>
        )}
        <div className="grid grid-cols-7 gap-4 mt-4">
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
  );
}

export default App;