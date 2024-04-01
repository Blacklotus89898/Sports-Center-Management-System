import React, { useState, useEffect } from 'react';

const BASE_URL = 'http://localhost:8080';

const SpecificClassService = {
  createSpecificClass: async (
    className,
    year,
    specificClassName,
    description,
    date,
    startTime,
    hourDuration,
    maxCapacity,
    currentCapacity,
    registrationFee
  ) => {
    const response = await fetch(`${BASE_URL}/specificClasses`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify({
        className,
        year,
        specificClassName,
        description,
        date,
        startTime,
        hourDuration,
        maxCapacity,
        currentCapacity,
        registrationFee,
      }),
    });
    return response.json();
  },

  getSpecificClass: async (classId) => {
    const response = await fetch(`${BASE_URL}/specificClasses/${classId}`);
    return response.json();
  },

  updateSpecificClass: async (
    classId,
    className,
    year,
    specificClassName,
    description,
    date,
    startTime,
    hourDuration,
    maxCapacity,
    currentCapacity,
    registrationFee
  ) => {
    const response = await fetch(`${BASE_URL}/specificClasses/${classId}`, {
      method: 'PUT',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify({
        className,
        year,
        specificClassName,
        description,
        date,
        startTime,
        hourDuration,
        maxCapacity,
        currentCapacity,
        registrationFee,
      }),
    });
    return response.json();
  },

  getAllSpecificClasses: async (year) => {
    const response = await fetch(`${BASE_URL}/specificClasses?year=${year}`);
    return response.json();
  },

  deleteSpecificClass: async (classId) => {
    const response = await fetch(`${BASE_URL}/specificClasses/${classId}`, {
      method: 'DELETE',
    });
    return response.status;
  },

  deleteAllSpecificClasses: async () => {
    const response = await fetch(`${BASE_URL}/specificClasses`, {
      method: 'DELETE',
    });
    return response.status;
  },

  deleteAllSpecificClassesByYear: async (year) => {
    const response = await fetch(`${BASE_URL}/specificClasses?year=${year}`, {
      method: 'DELETE',
    });
    return response.status;
  },
};

const SpecificClassComponent = () => {
  const [specificClasses, setSpecificClasses] = useState([]);
  const [className, setClassName] = useState('');
  const [year, setYear] = useState('');
  const [specificClassName, setSpecificClassName] = useState('');
  const [description, setDescription] = useState('');
  const [date, setDate] = useState('');
  const [startTime, setStartTime] = useState('');
  const [hourDuration, setHourDuration] = useState('');
  const [maxCapacity, setMaxCapacity] = useState('');
  const [currentCapacity, setCurrentCapacity] = useState('');
  const [registrationFee, setRegistrationFee] = useState('');

  useEffect(() => {
    const fetchSpecificClasses = async () => {
      const specificClassesData = await SpecificClassService.getAllSpecificClasses(year);
      setSpecificClasses(specificClassesData);
    };
    fetchSpecificClasses();
  }, [year]);

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      const newSpecificClass = await SpecificClassService.createSpecificClass(
        className,
        year,
        specificClassName,
        description,
        date,
        startTime,
        hourDuration,
        maxCapacity,
        currentCapacity,
        registrationFee
      );
      setSpecificClasses([...specificClasses, newSpecificClass]);
      setClassName('');
      setSpecificClassName('');
      setDescription('');
      setDate('');
      setStartTime('');
      setHourDuration('');
      setMaxCapacity('');
      setCurrentCapacity('');
      setRegistrationFee('');
    } catch (error) {
      console.error('Error creating specific class:', error);
    }
  };

  return (
    <div>
      <h2>Specific Classes</h2>
      <form onSubmit={handleSubmit}>
        <label>
          Class Name:
          <input type="text" value={className} onChange={(e) => setClassName(e.target.value)} />
        </label>
        <label>
          Year:
          <input type="number" value={year} onChange={(e) => setYear(e.target.value)} />
        </label>
        <label>
          Specific Class Name:
          <input type="text" value={specificClassName} onChange={(e) => setSpecificClassName(e.target.value)} />
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
          Start Time:
          <input type="time" value={startTime} onChange={(e) => setStartTime(e.target.value)} />
        </label>
        <label>
          Hour Duration:
          <input type="number" value={hourDuration} onChange={(e) => setHourDuration(e.target.value)} />
        </label>
        <label>
          Max Capacity:
          <input type="number" value={maxCapacity} onChange={(e) => setMaxCapacity(e.target.value)} />
        </label>
        <label>
          Current Capacity:
          <input type="number" value={currentCapacity} onChange={(e) => setCurrentCapacity(e.target.value)} />
        </label>
        <label>
          Registration Fee:
          <input type="number" value={registrationFee} onChange={(e) => setRegistrationFee(e.target.value)} />
        </label>
        <button type="submit">Create Specific Class</button>
      </form>
      <ul>
        {specificClasses.map((specificClass) => (
          <li key={specificClass.classId}>
            {specificClass.specificClassName} - {specificClass.description}
          </li>
        ))}
      </ul>
    </div>
  );
};

export default SpecificClassComponent;