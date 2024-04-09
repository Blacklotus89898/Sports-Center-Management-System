import React, { useState, useEffect, useRef } from 'react';

const DayCell = ({ date, events }) => {
  const [hoveredEventIndex, setHoveredEventIndex] = useState(null);
  const [selectedEventIndex, setSelectedEventIndex] = useState(null);
  const containerRef = useRef(null);


  useEffect(() => {
    const container = containerRef.current;
    if (container) {
      container.scrollTop = 400; // 将容器滚动到早上8:00的位置 (8 * 40)
    }
  }, []);

  const renderEvents = () => {
    const sortedEvents = events.sort((a, b) => a.startTime - b.startTime);

    return sortedEvents.map((event, index) => {
      const startRow = Math.floor(event.startTime / 30) + 1;
      const endRow = Math.floor(event.endTime / 30) + 1;
      const rowSpan = endRow - startRow + 1;

      return (
        <div
          key={`event-${index}`}
          className={`event absolute left-0 right-0 bg-purple-200 bg-opacity-75 p-2 rounded-xl cursor-pointer transition-transform duration-200 ease-in-out ${
            hoveredEventIndex === index ? 'transform scale-105 shadow-md' : ''
          }`}
          style={{ gridRow: `${startRow} / span ${rowSpan}`, marginTop: '4px', marginBottom: '4px' }}
          onMouseEnter={() => setHoveredEventIndex(index)}
          onMouseLeave={() => setHoveredEventIndex(null)}
          onClick={() => setSelectedEventIndex(index)}
        >
          <div className="event-time text-sm text-gray-600 font-semibold">
            {formatTime(event.startTime)} - {formatTime(event.endTime)}
          </div>
          <div className="event-title text-gray-800 font-bold overflow-wrap break-word">
            {truncateTitle(event.title)}
          </div>
        </div>
      );
    });
  };

  const formatTime = (time) => {
    const hours = Math.floor(time / 60);
    const minutes = time % 60;
    return `${hours.toString().padStart(2, '0')}:${minutes.toString().padStart(2, '0')}`;
  };

  const truncateTitle = (title, maxLength = 20) => {
    if (title.length > maxLength) {
      return `${title.slice(0, maxLength)}...`;
    }
    return title;
  };

  const renderTimeSlots = () => {
    const timeSlots = [];
    for (let i = 0; i < 1440; i += 30) {
      const isEventSlot = events.some(event => event.startTime <= i && i < event.endTime);

      if (!isEventSlot) {
        const hours = Math.floor(i / 60);
        const minutes = i % 60;
        timeSlots.push(
          <div
            key={`time-${i}`}
            className="time-slot text-gray-500 opacity-0 hover:opacity-100 transition-opacity duration-300 font-semibold"
            style={{ gridRow: i / 30 + 1, gridColumn: 1 }}
          >
            {`${hours.toString().padStart(2, '0')}:${minutes.toString().padStart(2, '0')}`}
          </div>
        );
      }
    }
    return timeSlots;
  };

  const renderEventPopup = () => {
    if (selectedEventIndex !== null) {
      const event = events[selectedEventIndex];
      return (
        <div className="event-popup fixed top-1/2 left-1/2 transform -translate-x-1/2 -translate-y-1/2 bg-white p-12 rounded-lg shadow-2xl z-50 w-11/12 sm:w-5/6 md:w-2/3 lg:w-1/2 xl:w-2/5">
          <h3 className="text-3xl font-bold mb-6">{event.title}</h3>
          <img src={event.image || 'jpg'} alt="Event" className="event-image mb-6 w-full rounded-lg" />
          <p className="text-gray-600 mb-6 text-xl">
            {formatTime(event.startTime)} - {formatTime(event.endTime)}
          </p>
          <p className="mb-8 text-lg">{event.description}</p>
          <button
            className="mt-4 px-8 py-4 bg-purple-500 text-white rounded-lg text-xl"
            onClick={() => setSelectedEventIndex(null)}
          >
            Close
          </button>
        </div>
      );
    }
    return null;
  };
  return (
    <div className="day-cell relative">
      <div className="container relative grid grid-cols-1 grid-rows-30 gap-0.5 h-full" style={{ gridAutoRows: '40px' }}>
        {Array.from({ length: 48 }).map((_, rowIndex) => (
          <React.Fragment key={rowIndex}>
            <div
              key={`${rowIndex}`}
              className={`${rowIndex % 2 === 0 ? 'bg-gray-100' : 'bg-gray-200'}`}
              style={{ gridRow: rowIndex + 1, gridColumn: 1 }}
            ></div>
          </React.Fragment>
        ))}
        {renderEvents()}
        {renderTimeSlots()}
      </div>
      {renderEventPopup()}
    </div>
  );
};

export default DayCell;