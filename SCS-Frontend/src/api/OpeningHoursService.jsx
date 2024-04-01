import React, { useState, useEffect } from 'react';

const BASE_URL = 'http://localhost:8080';

const OpeningHoursService = {
  createOpeningHours: async (day, openTime, closeTime, year) => {
    const response = await fetch(`${BASE_URL}/openingHours`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify({ day, openTime, closeTime, year }),
    });
    return response.json();
  },

  getOpeningHoursByDay: async (day, year) => {
    const response = await fetch(`${BASE_URL}/openingHours?day=${day}&year=${year}`);
    return response.json();
  },

  updateOpeningHours: async (openTime, closeTime, year, day) => {
    const response = await fetch(`${BASE_URL}/openingHours?day=${day}&year=${year}`, {
      method: 'PUT',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify({ openTime, closeTime, year, day }),
    });
    return response.json();
  },

  getAllOpeningHours: async (year) => {
    const response = await fetch(`${BASE_URL}/openingHours?year=${year}`);
    return response.json();
  },

  deleteOpeningHours: async (day, year) => {
    const response = await fetch(`${BASE_URL}/openingHours?day=${day}&year=${year}`, {
      method: 'DELETE',
    });
    return response.status;
  },

  deleteAllOpeningHours: async (year) => {
    const response = await fetch(`${BASE_URL}/openingHours?year=${year}`, {
      method: 'DELETE',
    });
    return response.status;
  },
};

const OpeningHoursComponent = () => {
  const [openingHours, setOpeningHours] = useState([]);
  const [day, setDay] = useState('');
  const [openTime, setOpenTime] = useState('');
  const [closeTime, setCloseTime] = useState('');
  const [year, setYear] = useState(new Date().getFullYear());

  useEffect(() => {
    const fetchOpeningHours = async () => {
      const openingHoursData = await OpeningHoursService.getAllOpeningHours(year);
      setOpeningHours(openingHoursData);
    };
    fetchOpeningHours();
  }, [year]);

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      const newOpeningHours = await OpeningHoursService.createOpeningHours(
        day,
        openTime,
        closeTime,
        year
      );
      setOpeningHours([...openingHours, newOpeningHours]);
      setDay('');
      setOpenTime('');
      setCloseTime('');
    } catch (error) {
      console.error('Error creating opening hours:', error);
    }
  };

  return (
    <div>
      <h2>Opening Hours</h2>
      <form onSubmit={handleSubmit}>
        <label>
          Day:
          <input type="text" value={day} onChange={(e) => setDay(e.target.value)} />
        </label>
        <label>
          Open Time:
          <input type="time" value={openTime} onChange={(e) => setOpenTime(e.target.value)} />
        </label>
        <label>
          Close Time:
          <input type="time" value={closeTime} onChange={(e) => setCloseTime(e.target.value)} />
        </label>
        <label>
          Year:
          <input type="number" value={year} onChange={(e) => setYear(e.target.value)} />
        </label>
        <button type="submit">Create Opening Hours</button>
      </form>
      <ul>
        {openingHours.map((oh) => (
          <li key={`${oh.dayOfWeek}-${oh.schedule.year}`}>
            {oh.dayOfWeek} - {oh.openTime} - {oh.closeTime}
          </li>
        ))}
      </ul>
    </div>
  );
};

export default OpeningHoursComponent;