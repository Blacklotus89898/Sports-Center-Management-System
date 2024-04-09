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
  const [currentTipIndex, setCurrentTipIndex] = useState(0);

  const handleTipClick = () => {
    setCurrentTipIndex((prevIndex) => (prevIndex + 1) % tips.length);
  };

  const tips = [
    {
      title: "Tips of the day",
      content: "Set Goals: Define what you want to achieve in various aspects of your life, such as career, relationships, health, and personal development. Setting clear, achievable goals provides direction and motivation.",
    },
    {
      title: "Tips of the day",
      content: "Stay Positive: Cultivate a positive mindset by focusing on the good things in your life and practicing gratitude. Optimism can improve your resilience in the face of challenges and enhance your overall well-being.",
    },
    // ... add the remaining tips ...
  ];
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
  const [selectedDay, setSelectedDay] = useState(null);

  const handleDayClick = (day) => {
    setSelectedDay(day);
  };
    const events = [
      // Day 1
      [
        { startTime: 480, endTime: 600, title: 'Morning Meeting' }, // 8:00 AM - 10:00 AM
        { startTime: 720, endTime: 780, title: 'Lunch Break' },     // 12:00 PM - 1:00 PM
        { startTime: 960, endTime: 1020, title: 'Afternoon Seminar' }, // 4:00 PM - 5:00 PM
      ],
      // Day 2
      [
        { startTime: 540, endTime: 660, title: 'Team Discussion' }, // 9:00 AM - 11:00 AM
        { startTime: 840, endTime: 960, title: 'Project Review' },  // 2:00 PM - 4:00 PM
      ],
      // Day 3
      [
        { startTime: 600, endTime: 660, title: 'Training Session' }, // 10:00 AM - 11:00 AM
        { startTime: 840, endTime: 900, title: 'Client Call' },      // 2:00 PM - 3:00 PM
      ],
      // Day 4
      [
        { startTime: 480, endTime: 540, title: 'Breakfast Meeting' }, // 8:00 AM - 9:00 AM
        { startTime: 660, endTime: 720, title: 'Team Building' },     // 11:00 AM - 12:00 PM
        { startTime: 900, endTime: 960, title: 'Project Planning' },  // 3:00 PM - 4:00 PM
      ],
      // Day 5
      [
        { startTime: 540, endTime: 660, title: 'Client Presentation' }, // 9:00 AM - 11:00 AM
        { startTime: 780, endTime: 900, title: 'Design Workshop' },     // 1:00 PM - 3:00 PM
        { startTime: 1020, endTime: 1080, title: 'Feedback Session' }, // 5:00 PM - 6:00 PM
      ],
      // Day 6
      [
        { startTime: 480, endTime: 600, title: 'Sales Meeting' }, // 8:00 AM - 10:00 AM
        { startTime: 720, endTime: 840, title: 'Marketing Campaign' }, // 12:00 PM - 2:00 PM
      ],
      // Day 7
      [
        { startTime: 600, endTime: 660, title: 'Product Demo' }, // 10:00 AM - 11:00 AM
        { startTime: 780, endTime: 900, title: 'Customer Support' }, // 1:00 PM - 3:00 PM
        { startTime: 1020, endTime: 1080, title: 'Team Celebration' }, // 5:00 PM - 6:00 PM
      ],
    ];
    
    const roundedCalendarStyle = {
      borderRadius: '11px', // 设置圆角大小
      overflow: 'hidden', // 防止内容溢出
      border: '1px solid black'
    };

  return (
<div className="h-screen w-full bg-gray-300 p-6 flex flex-col items-center justify-center">
  <div className="bg-gray-100 rounded-2xl overflow-hidden shadow-lg mb-4 w-full" style={{ height: '80vh' }}>
    <div className="flex">
      <div className="w-1/3 bg-gradient-to-r from-purple-300 to-blue-300 bg-opacity-200 p-4 rounded-2xl my-2 mx-2 hidden sm:hidden lg:block">
      {selectedDay && (
    <div className="bg-white p-4 rounded-lg mb-4">
      <p className="text-lg font-bold mb-2">Montreal Weather On Day {selectedDay}</p>
      <p>Weather: Sunny</p>
      <p>Temperature: 25°C</p>
    </div>
  )}
      <div className style={roundedCalendarStyle}>
        <Calendar
          border-radius='3px'
          onChange={handleCalendarChange}
          value={selectedDate}
          calendarType="US"
        />
        </div>
        <div
          className="bg-gray-100 p-4 rounded-lg cursor-pointer my-3"
          onClick={handleTipClick}
        >
          <p className="text-lg font-bold mb-2 text-gray-800">{tips[currentTipIndex].title}</p>
          <p className="text-gray-600">{tips[currentTipIndex].content}</p>
        </div>
      </div>

      <div className="w-full my-2 mx-2">
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
              <div className="flex justify-between mx-3">
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
                        className={`flex items-center justify-center w-10 h-10 rounded-full ${
                          selectedDay === date.getDate() ? 'bg-purple-500 text-white' : 'bg-white text-gray-900'
                        } font-bold hover:bg-purple-500 hover:text-white transition-colors duration-300`}
                        onClick={() => handleDayClick(date.getDate())}
                      >
                        {date.getDate()}
                      </button>
                    </div>
                  );
                })}
              </div>
          </div>
          <div className="bg-white bg-opacity-50 shadow-md rounded-lg p-4">
            {/* {selectedDate && (
              <p className="text-gray-900 font-semibold mb-4">{selectedDate.toDateString()}</p>
            )} */}
          <div className="w-full overflow-y-auto" style={{ height: '80vh', maxHeight: 'calc(80vh - 200px)' }}>
          <div className="container relative">
                <div className="content">
                  <div className="grid grid-cols-7 gap-4">
                    {weekDates.map((date, index) => (
                      <div key={index} className="day-cell">
                        <DayCell
                          date={date.toISOString().slice(0, 10)}
                          events={events[index]}
                        />
                      </div>
                    ))}
                  </div>
                </div>
                <svg className="absolute top-0 left-0 w-full h-full pointer-events-none" xmlns="http://www.w3.org/2000/svg">
                  <defs>
                    <pattern id="grid" width="100%" height="20" patternUnits="userSpaceOnUse">
                      <path d="M 0 20 L 100% 20" fill="none" stroke="rgba(0, 0, 0, 0.1)" strokeWidth="0.5" />
                    </pattern>
                  </defs>
                  <rect width="100%" height="100%" fill="url(#grid)" />
                </svg>
              </div>
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