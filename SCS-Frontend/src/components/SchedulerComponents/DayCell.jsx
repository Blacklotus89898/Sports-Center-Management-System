import React, { useState } from 'react';

const DayCell = ({ date, events }) => {
  const [hoveredEventIndex, setHoveredEventIndex] = useState(null);

  const renderEvents = () => {
    const sortedEvents = events.sort((a, b) => a.startTime - b.startTime);

    return sortedEvents.map((event, index) => {
      const startRow = Math.floor((event.startTime - 420) / 30) + 1;
      const endRow = Math.floor((event.endTime - 420) / 30) + 1;
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
        >
          <div className="event-time text-sm text-gray-600 font-semibold">
            {formatTime(event.startTime)} - {formatTime(event.endTime)}
          </div>
          <div className="event-title text-gray-800 font-bold">{event.title}</div>
        </div>
      );
    });
  };

  const formatTime = (time) => {
    const hours = Math.floor(time / 60);
    const minutes = time % 60;
    return `${hours.toString().padStart(2, '0')}:${minutes.toString().padStart(2, '0')}`;
  };

  const renderTimeSlots = () => {
    const timeSlots = [];
    for (let i = 420; i < 1320; i += 30) {
      const hours = Math.floor(i / 60);
      const minutes = i % 60;
      timeSlots.push(
        <div
          key={`time-${i}`}
          className="time-slot text-gray-500 opacity-0 hover:opacity-100 transition-opacity duration-300 font-semibold"
          style={{ gridRow: (i - 420) / 30 + 1, gridColumn: 1 }}
        >
          {`${hours.toString().padStart(2, '0')}:${minutes.toString().padStart(2, '0')}`}
        </div>
      );
    }
    return timeSlots;
  };

  return (
    <div className="day-cell relative">
      <div className="container relative grid grid-cols-7 grid-rows-30 gap-0.5 h-full" style={{ gridAutoRows: '40px' }}>
        {Array.from({ length: 30 }).map((_, rowIndex) => (
          <React.Fragment key={rowIndex}>
            {Array.from({ length: 7 }).map((_, colIndex) => (
              <div
                key={`${rowIndex}-${colIndex}`}
                className={`${colIndex === 0 ? 'hover:bg-gray-200' : ''} ${
                  rowIndex % 2 === 0 ? 'bg-gray-100' : 'bg-gray-200'
                }`}
                style={{ gridRow: rowIndex + 1, gridColumn: colIndex + 1 }}
              ></div>
            ))}
          </React.Fragment>
        ))}
        {renderEvents()}
        {renderTimeSlots()}
      </div>
    </div>
  );
};

export default DayCell;