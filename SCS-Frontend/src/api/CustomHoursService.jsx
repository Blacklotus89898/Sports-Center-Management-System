import React, { useState, useEffect } from 'react';

const BASE_URL = 'http://localhost:8080';

const CustomHoursService = {
  createCustomHours: async (name, description, date, openTime, closeTime, year) => {
    const response = await fetch(`${BASE_URL}/customHours`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify({ name, description, date, openTime, closeTime, year }),
    });
    return response.json();
  },

  getCustomHours: async (name, year) => {
    const response = await fetch(`${BASE_URL}/customHours?name=${name}&year=${year}`);
    return response.json();
  },

  getCustomHoursByDate: async (date) => {
    const response = await fetch(`${BASE_URL}/customHours?date=${date}`);
    return response.json();
  },

  updateCustomHours: async (name, description, date, openTime, closeTime, year) => {
    const response = await fetch(`${BASE_URL}/customHours?name=${name}&year=${year}`, {
      method: 'PUT',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify({ name, description, date, openTime, closeTime, year }),
    });
    return response.json();
  },

  getAllCustomHours: async (year) => {
    const response = await fetch(`${BASE_URL}/customHours?year=${year}`);
    return response.json();
  },

  deleteCustomHours: async (name, year) => {
    const response = await fetch(`${BASE_URL}/customHours?name=${name}&year=${year}`, {
      method: 'DELETE',
    });
    return response.status;
  },

  deleteAllCustomHours: async (year) => {
    const response = await fetch(`${BASE_URL}/customHours?year=${year}`, {
      method: 'DELETE',
    });
    return response.status;
  },
};

const CustomHoursComponent = () => {
  const [customHours, setCustomHours] = useState([]);
  const [name, setName] = useState('');
  const [description, setDescription] = useState('');
  const [date, setDate] = useState('');
  const [openTime, setOpenTime] = useState('');
  const [closeTime, setCloseTime] = useState('');
  const [year, setYear] = useState(new Date().getFullYear());

  useEffect(() => {
    const fetchCustomHours = async () => {
      const customHoursData = await CustomHoursService.getAllCustomHours(year);
      setCustomHours(customHoursData);
    };
    fetchCustomHours();
  }, [year]);

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      const newCustomHours = await CustomHoursService.createCustomHours(
        name,
        description,
        date,
        openTime,
        closeTime,
        year
      );
      setCustomHours([...customHours, newCustomHours]);
      setName('');
      setDescription('');
      setDate('');
      setOpenTime('');
      setCloseTime('');
    } catch (error) {
      console.error('Error creating custom hours:', error);
    }
  };

  return (
    <div>
      <h2>Custom Hours</h2>
      <form onSubmit={handleSubmit}>
        <label>
          Name:
          <input type="text" value={name} onChange={(e) => setName(e.target.value)} />
        </label>
        <label>
          Description:
          <input type="text" value={description} onChange={(e) => setDescription(e.target.value)} />
        </label>
        <label>
          Date:
          <input type="date" value={date} onChange={(e) => setDate(e.target.value)} />
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
        <button type="submit">Create Custom Hours</button>
      </form>
      <ul>
        {customHours.map((ch) => (
          <li key={ch.name}>
            {ch.name} - {ch.description} - {ch.date} - {ch.openTime} - {ch.closeTime}
          </li>
        ))}
      </ul>
    </div>
  );
};

export default CustomHoursComponent;