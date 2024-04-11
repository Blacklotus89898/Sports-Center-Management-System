import React, { useState, useEffect } from 'react';
import Calendar from 'react-calendar';
import 'react-calendar/dist/Calendar.css';
import DayCell from './DayCell';
import { PageProvider } from '../../providers/PageProvider';
import useFetch from '../../api/useFetch';

function App() {
    const [selectedDate, setSelectedDate] = useState(new Date());
    const [fetching, setFetching] = useState(false);
    const [schedules, setSchedules] = useState([]);
    const [isMobile, setIsMobile] = useState(false);
    const [classes, setClasses] = useState([]);
    const [openingHours, setOpeningHours] = useState({});   // {'year': [openingHoursForYear]}
    const [customHours, setCustomHours] = useState({});     // {'year': [customHoursForYear]}

    const days = ['Sun', 'Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat'];

    const API_URL = 'http://localhost:8080';
    const { data, loading, error, fetchData, reset } = useFetch();

    const handleCalendarChange = (selectedDate) => {
        setSelectedDate(selectedDate);
    };

    const weekDates = [];
    for (let i = 0; i < 7; i++) {
        const date = new Date(selectedDate);
        date.setDate(date.getDate() - selectedDate.getDay() + i);
        weekDates.push(date);
    }
    useEffect(() => {
      const handleResize = () => {
        setIsMobile(window.innerWidth < 768); // 根据需要调整阈值
      };
    
      window.addEventListener('resize', handleResize);
      handleResize(); // 初始化时检查一次
    
      return () => {
        window.removeEventListener('resize', handleResize);
      };
    }, []);
    
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

    function convertTimeToMinutes(timeString) {
        const [hours, minutes, seconds] = timeString.split(':').map(Number);
        return hours * 60 + minutes;
    }

    const handleDayClick = (day) => {
        setSelectedDay(day);
    };

    const getEvents = (date, index) => {
        // get the opening hours for the day
        const dayMap = {
            0: 'SUNDAY',
            1: 'MONDAY',
            2: 'TUESDAY',
            3: 'WEDNESDAY',
            4: 'THURSDAY',
            5: 'FRIDAY',
            6: 'SATURDAY'
        };

        const formattedDate = date.toISOString().slice(0, 10);

        // format the date to match the date in the classes
        const filteredClasses = classes.filter((klass) => klass.date === formattedDate);

        // get the opening hours for the year
        const openingHourForYear = openingHours[date.getFullYear()];
        let openingHourForDay = openingHourForYear && openingHourForYear.find((oh) => oh.dayOfWeek === dayMap[date.getDay()]);

        // get custom hours for the year
        const customHoursForYear = customHours[date.getFullYear()];
        const customHoursForDay = customHoursForYear && customHoursForYear.filter((ch) => ch.date === formattedDate);
        
        // find custom hours for the day (cH.date === formattedDate)
        const customHourForDay = customHoursForYear && customHoursForYear.filter((ch) => ch.date === formattedDate)[0];
        if (customHourForDay) openingHourForDay = null;

        // convert the time to minutes
        const classEvents = filteredClasses.map((klass) => {
            const startTime = convertTimeToMinutes(klass.startTime);
            const endTime = startTime + klass.hourDuration * 60;
            return {
                startTime,
                endTime,
                title: klass.specificClassName,
                description: klass.description
            };
        });

        // add the custom hours to the class events
        let startTime = 0;
        let endTime = 0;
        if (customHourForDay) {
            startTime = convertTimeToMinutes(customHourForDay.openTime);
            endTime = startTime;
            classEvents.push({
                startTime,
                endTime,
                title: customHourForDay.name,
                description: `Open from ${customHourForDay.openTime} to ${customHourForDay.closeTime}`
            });

            startTime = convertTimeToMinutes(customHourForDay.closeTime);
            endTime = startTime
            classEvents.push({
                startTime,
                endTime,
                title: customHourForDay.name,
                description: `Open from ${customHourForDay.openTime} to ${customHourForDay.closeTime}`
            });
        }

        // add the opening hours to the class events
        if (openingHourForDay) {
            if (openingHourForDay.openTime === '00:00:00' && openingHourForDay.closeTime === '00:00:00') {
                classEvents.push({
                    startTime,
                    endTime,
                    title: 'Closed All Day',
                    description: `Open from ${openingHourForDay.openTime} to ${openingHourForDay.closeTime}`
                });
            } else {
                // add start of day
                startTime = convertTimeToMinutes(openingHourForDay.openTime);
                endTime = startTime;
                
                classEvents.push({
                    startTime,
                    endTime,
                    title: 'Open',
                    description: `Open from ${openingHourForDay.openTime} to ${openingHourForDay.closeTime}`
                });

                // end of day
                startTime = convertTimeToMinutes(openingHourForDay.closeTime);
                endTime = startTime;

                classEvents.push({
                    startTime,
                    endTime,
                    title: 'Close',
                    description: `Open from ${openingHourForDay.openTime} to ${openingHourForDay.closeTime}`
                });
            }
        }

        return classEvents;
    };
        
    const roundedCalendarStyle = {
        borderRadius: '11px', // 设置圆角大小
        overflow: 'hidden', // 防止内容溢出
        border: '1px solid black'
    };

    useEffect(() => {
        // fetch class types from the server
        setFetching(true);
        fetchData(`${API_URL}/schedules`, {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json'
            }
        }, (data) => {
            // loop through schedules and get classes by year
            data.schedules.forEach(schedule => {
                // get the classes for the year
                fetchData(`${API_URL}/specificClass/year/${schedule.year}`, {
                    method: 'GET',
                    headers: {
                        'Content-Type': 'application/json'
                    }
                }, (data) => {
                    setClasses(prevClasses => {
                        const newClasses = data.specificClasses.filter(newClass => !prevClasses.some(prevClass => prevClass.classId === newClass.classId));
                        return [...prevClasses, ...newClasses];
                    });
                    setFetching(false);
                });
            
                // get the opening hours and custom hours
                // opening hours: http://localhost:8080/schedules/{year}/openingHours
                fetchData(`${API_URL}/schedules/${schedule.year}/openingHours`, {
                    method: 'GET',
                    headers: {
                        'Content-Type': 'application/json'
                    }
                }, (data) => {
                    setOpeningHours(prevOpeningHours => {
                        const newOpeningHours = { [schedule.year]: data };
                        return { ...prevOpeningHours, ...newOpeningHours };
                    });
                });

                // custom hours: http://localhost:8080/schedules/{year}/customHours
                fetchData(`${API_URL}/schedules/${schedule.year}/customHours`, {
                    method: 'GET',
                    headers: {
                        'Content-Type': 'application/json'
                    }
                }, (data) => {
                    setCustomHours(prevCustomHours => {
                        const newCustomHours = { [schedule.year]: data.customHours.filter(cH => cH.schedule.year === schedule.year) };
                        return { ...prevCustomHours, ...newCustomHours };
                    });
                });
            }); 
        });

        // get all the instructors
        fetchData(`${API_URL}/instructors`, {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json'
            }
        }, (data) => {
            if (data && data.instructors) {
                setAvailableInstructors(data.instructors);
            }
        });
    }, []);
    const calendarStyles = {
      dayCell: {
        gridColumn: '1 / -1',
        height: '100%',
      },
      dayCellContainer: {
        height: '100%',
      },
    };
    return (
        <PageProvider>
            <div className="w-full p-6 flex flex-col items-center justify-center">
                <div className="bg-base-100 rounded-2xl overflow-hidden shadow-lg mb-4 w-full" style={{ height: '80vh' }}>
                    <div className="flex">
                    <div className="w-1/3 bg-gradient-to-r p-4 rounded-2xl my-2 mx-2 hidden sm:hidden lg:block">
                    {/* bonus feature that may be implemented in the future, kept for formatting sake */}
                    {/* {selectedDay && (
                    <div className="p-4 rounded-lg mb-4">
                        <p className="text-lg font-bold mb-2">Montreal Weather On Day {selectedDay}</p>
                        <p>Weather: Sunny</p>
                        <p>Temperature: 25°C</p>
                    </div>)} */}
                    <div className='rounded-[11px] overflow-hidden flex justify-center items-center'>
                    <div className style={roundedCalendarStyle}>
                        {/* the calendar */}
                        <Calendar
                            border-radius='3px'
                            onChange={handleCalendarChange}
                            value={selectedDate}
                            calendarType="US"
                        />
                        </div>
                        </div>
                        <div
                            className="bg-base-100 p-4 rounded-lg cursor-pointer my-3"
                            onClick={handleTipClick}
                        >
                            <p className="text-lg font-bold mb-2 text-accent">{tips[currentTipIndex].title}</p>
                            <p className="text-accent">{tips[currentTipIndex].content}</p>
                        </div>
                    </div>

                    <div className="w-full my-2 mx-2">
                        <div className="bg-gradient-to-r p-4 rounded-2xl">
                        <div className="shadow-md rounded-lg p-4 mb-4">
                            <div className="block lg:hidden mb-4">
                            {/* input for the date on the side calendar */}
                            <input
                                type="date"
                                className="w-full px-4 py-2 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                                value={selectedDate.toISOString().slice(0, 10)}
                                onChange={(e) => setSelectedDate(new Date(e.target.value))}
                            />
                            </div>
                            <div className="grid grid-cols-7 gap-4">
                                {days.map((day, index) => {
                                    const date = new Date(weekStart);
                                    date.setDate(date.getDate() + index);

                                    // render the day cell
                                    return (
                                        <div
                                        key={index}
                                        className="flex flex-col items-center justify-center w-16"
                                        >
                                        <p className="text-accent text-sm font-semibold mb-1">{day}</p>
                                        <button
                                            className={`flex items-center justify-center w-10 h-10 rounded-full ${
                                            selectedDay === date.getDate() ? 'bg-primary text-accent' : 'bg-base-100 text-accent'
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
                        <div className="bg-base-100 bg-opacity-50 shadow-md rounded-lg p-4">
                            {/* {selectedDate && (
                            <p className="text-gray-900 font-semibold mb-4">{selectedDate.toDateString()}</p>
                            )} */}
                        <div className="w-full overflow-y-auto" style={{ height: '80vh', maxHeight: 'calc(80vh - 200px)' }}>
                        <div className="container relative">
                                <div className="content">
                                <div className="grid grid-cols-7 gap-4">
                                  {isMobile ? (
                                    <div className="day-cell" style={calendarStyles.dayCell}>
                                      <DayCell
                                        date={selectedDate.toISOString().slice(0, 10)}
                                        events={getEvents(selectedDate, 0)}
                                      />
                                    </div>
                                  ) : (
                                    weekDates.map((date, index) => (
                                      <div key={index} className="day-cell">
                                        <DayCell
                                          date={date.toISOString().slice(0, 10)}
                                          events={getEvents(date, index)}
                                        />
                                      </div>
                                    ))
                                  )}
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