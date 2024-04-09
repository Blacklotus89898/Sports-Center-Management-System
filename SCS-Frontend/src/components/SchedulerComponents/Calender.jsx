import React, { useEffect, useState } from 'react';
import Calendar from 'react-calendar';
import 'react-calendar/dist/Calendar.css';
import DayCell from './DayCell';
import { PageProvider } from '../../providers/PageProvider';
import useFetch from "../../api/useFetch";

const API_URL = 'http://localhost:8080/';

function App() {
  const [selectedDate, setSelectedDate] = useState(new Date());
  const [selectedDay, setSelectedDay] = useState(null);
  const [events, setEvents] = useState(Array(365).fill().map(() => []));
  
  // useFetch hook
  const { data, loading, error, fetchData } = useFetch();

  useEffect(() => {
    getSpecificClasss();
  }, []);

  const getSpecificClasss = async () => {
    await fetchData(`${API_URL}specificClass/year/${2022}`, {});
    console.log(data);
    const newEvents = generateEvents(data.specificClasses);
    // setEvents(newEvents);
    console.log(newEvents);
  };
  const getWeekDates = (selectedDate) => {
    const currentDate = new Date(selectedDate);
    const weekStart = new Date(currentDate);
    const weekEnd = new Date(currentDate);
    const diff = currentDate.getDay();
    weekStart.setDate(currentDate.getDate() - diff);
    weekEnd.setDate(currentDate.getDate() - diff + 6);
    return { weekStart, weekEnd };
  };

  function generateEvents(classList) {
    const events = Array(365).fill().map(() => []);
  
    classList.forEach((classObj) => {
      const classDate = new Date(classObj.date);
      const dayIndex = getDayIndex(classDate);
      const startTime = convertTimeToMinutes(classObj.startTime);
      const endTime = startTime + classObj.hourDuration * 60;
  
      const description = `Class ID: ${classObj.classId}
  Class Type: ${JSON.stringify(classObj.classType)}
  Current Capacity: ${classObj.currentCapacity}
  Max Capacity: ${classObj.maxCapacity} 
  Hour Duration: ${classObj.hourDuration}
  Registration Fee: ${classObj.registrationFee}
  Schedule: ${JSON.stringify(classObj.schedule)}
  Description: ${classObj.description}
  Image: ${classObj.image}`;
  
      events[dayIndex].push({
        startTime,
        endTime,
        title: classObj.specificClassName,
        description,
      });
    });
  
    return events;
  }
  function getDayIndex(date) {
    const startOfYear = new Date(date.getFullYear(), 0, 1);
    const timeDiff = date.getTime() - startOfYear.getTime();
    const dayIndex = Math.floor(timeDiff / (1000 * 3600 * 24));
    return dayIndex;
  }

  function convertTimeToMinutes(timeString) {
    const [hours, minutes, seconds] = timeString.split(':').map(Number);
    return hours * 60 + minutes;
  }

  const handleCalendarChange = (selectedDate) => {
    setSelectedDate(selectedDate);
  };

  const handleDayClick = (day) => {
    setSelectedDay(day);
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

  const roundedCalendarStyle = {
    borderRadius: '11px',
    overflow: 'hidden',
    border: '1px solid black'
  };

  return (
    <PageProvider>
      <div className="h-screen w-full bg-gray-300 p-6 flex flex-col items-center justify-center">
        <div className="bg-gray-100 rounded-2xl overflow-hidden shadow-lg mb-4 w-full" style={{ height: '80vh' }}>
          <div className="flex">
            <div className="w-1/3 bg-gradient-to-r from-black to-gray-500 bg-opacity-200 p-4 rounded-2xl my-2 mx-2 hidden sm:hidden lg:block">
              {selectedDay && (
                <div className="bg-white p-4 rounded-lg mb-4">
                  <p className="text-lg font-bold mb-2">Montreal Weather On Day {selectedDay}</p>
                  <p>Weather: Sunny</p>
                  <p>Temperature: 25°C</p>
                </div>
              )}
              <div className='rounded-[11px] overflow-hidden bg-white flex justify-center items-center'>
                <div className style={roundedCalendarStyle}>
                  <Calendar
                    border-radius='3px'
                    onChange={handleCalendarChange}
                    value={selectedDate}
                    calendarType="US"
                  />
                </div>
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
              <div className="bg-gradient-to-r from-gray-500 to-black bg-opacity-40 p-4 rounded-2xl">
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
                      const date = new Date(weekDates[index]);
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
                  <div className="w-full overflow-y-auto" style={{ height: '80vh', maxHeight: 'calc(80vh - 200px)' }}>
                    <div className="container relative">
                      <div className="content">
                        <div className="grid grid-cols-7 gap-4">
                          {weekDates.map((date, index) => {
                            const dayIndex = getDayIndex(date);
                            return (
                              <div key={index} className="day-cell">
                                <DayCell
                                  date={date.toISOString().slice(0, 10)}
                                  events={events[dayIndex]}
                                />
                              </div>
                            );
                          })}
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
    </PageProvider>
  );
}

export default App;