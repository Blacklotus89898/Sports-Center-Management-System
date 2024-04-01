import React, { useState, useEffect } from 'react';

const BASE_URL = 'http://localhost:8080';

const InstructorService = {
  createInstructor: async (email, password, name) => {
    const response = await fetch(`${BASE_URL}/instructors`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify({ email, password, name }),
    });
    return response.json();
  },

  getInstructorById: async (id) => {
    const response = await fetch(`${BASE_URL}/instructors/${id}`);
    return response.json();
  },

  getAllInstructors: async () => {
    const response = await fetch(`${BASE_URL}/instructors`);
    return response.json();
  },

  updateInstructor: async (accountId, email, password, name) => {
    const response = await fetch(`${BASE_URL}/instructors/${accountId}`, {
      method: 'PUT',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify({ email, password, name }),
    });
    return response.json();
  },

  deleteInstructor: async (accountId) => {
    const response = await fetch(`${BASE_URL}/instructors/${accountId}`, {
      method: 'DELETE',
    });
    return response.status;
  },

  deleteAllInstructors: async () => {
    const response = await fetch(`${BASE_URL}/instructors`, {
      method: 'DELETE',
    });
    return response.status;
  },
};

const InstructorComponent = () => {
  const [instructors, setInstructors] = useState([]);
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [name, setName] = useState('');

  useEffect(() => {
    const fetchInstructors = async () => {
      const instructorsData = await InstructorService.getAllInstructors();
      setInstructors(instructorsData);
    };
    fetchInstructors();
  }, []);

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      const newInstructor = await InstructorService.createInstructor(email, password, name);
      setInstructors([...instructors, newInstructor]);
      setEmail('');
      setPassword('');
      setName('');
    } catch (error) {
      console.error('Error creating instructor:', error);
    }
  };

  return (
    <div>
      <h2>Instructors</h2>
      <form onSubmit={handleSubmit}>
        <label>
          Email:
          <input type="email" value={email} onChange={(e) => setEmail(e.target.value)} />
        </label>
        <label>
          Password:
          <input type="password" value={password} onChange={(e) => setPassword(e.target.value)} />
        </label>
        <label>
          Name:
          <input type="text" value={name} onChange={(e) => setName(e.target.value)} />
        </label>
        <button type="submit">Create Instructor</button>
      </form>
      <ul>
        {instructors.map((instructor) => (
          <li key={instructor.accountId}>
            {instructor.email} - {instructor.name}
          </li>
        ))}
      </ul>
    </div>
  );
};

export default InstructorComponent;