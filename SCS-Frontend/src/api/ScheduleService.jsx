import React, { useState, useEffect } from 'react';

const BASE_URL = 'http://localhost:8080';

const ScheduleService = {
  createSchedule: async (year) => {
    const response = await fetch(`${BASE_URL}/schedules`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify({ year }),
    });
    return response.json();
  },

  getSchedule: async (year) => {
    const response = await fetch(`${BASE_URL}/schedules/${year}`);
    return response.json();
  },

  getAllSchedules: async () => {
    const response = await fetch(`${BASE_URL}/schedules`);
    return response.json();
  },

  deleteSchedule: async (year) => {
    const response = await fetch(`${BASE_URL}/schedules/${year}`, {
      method: 'DELETE',
    });
    return response.status;
  },

  deleteAllSchedules: async () => {
    const response = await fetch(`${BASE_URL}/schedules`, {
      method: 'DELETE',
    });
    return response.status;
  },
};

const ScheduleComponent = () => {
  const [schedules, setSchedules] = useState([]);
  const [year, setYear] = useState('');

  useEffect(() => {
    const fetchSchedules = async () => {
      const schedulesData = await ScheduleService.getAllSchedules();
      setSchedules(schedulesData);
    };
    fetchSchedules();
  }, []);

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      const newSchedule = await ScheduleService.createSchedule(year);
      setSchedules([...schedules, newSchedule]);
      setYear('');
    } catch (error) {
      console.error('Error creating schedule:', error);
    }
  };

  return (
    <div>
      <h2>Schedules</h2>
      <form onSubmit={handleSubmit}>
        <label>
          Year:
          <input type="number" value={year} onChange={(e) => setYear(e.target.value)} />
        </label>
        <button type="submit">Create Schedule</button>
      </form>
      <ul>
        {schedules.map((schedule) => (
          <li key={schedule.year}>Year: {schedule.year}</li>
        ))}
      </ul>
    </div>
  );
};

export default ScheduleComponent;