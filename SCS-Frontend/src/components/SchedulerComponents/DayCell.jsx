import React, { useState } from 'react';

const DayCell = ({ date, events }) => {
  const [hoveredEventIndex, setHoveredEventIndex] = useState(null);

  const renderEvents = () => {
    // Sort events by start time
    const sortedEvents = events.sort((a, b) => a.startTime - b.startTime);

    return sortedEvents.map((event, index) => {
      const eventHeight = 120; // 增大事件的高度
      const eventTop = event.startTime*2; // 计算事件的顶部位置

      return (
        <div
          key={index}
          className={`event absolute left-0 right-0 bg-purple-200 bg-opacity-75 p-2 mb-2 rounded-xl cursor-pointer transition-transform duration-200 ease-in-out ${
            hoveredEventIndex === index ? 'transform scale-105 shadow-md' : ''
          }`}
          style={{ top: `${eventTop}px`, height: `${eventHeight}px` }}
          onMouseEnter={() => setHoveredEventIndex(index)}
          onMouseLeave={() => setHoveredEventIndex(null)}
        >
          <div className="event-time text-sm text-gray-600">
            {formatTime(event.startTime)} - {formatTime(event.endTime)}
          </div>
          <div className="event-title font-semibold text-gray-800">{event.title}</div>
        </div>
      );
    });
  };

  const formatTime = (time) => {
    // Format time as HH:mm
    const hours = Math.floor(time / 60);
    const minutes = time % 60;
    return `${hours.toString().padStart(2, '0')}:${minutes.toString().padStart(2, '0')}`;
  };

  return (
    <div className="day-cell relative">
      <div className="date text-lg font-semibold mb-2">{date}</div>
      <div className="timeline relative">{renderEvents()}</div>
    </div>
  );
};

export default DayCell;