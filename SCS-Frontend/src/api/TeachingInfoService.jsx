import React, { useState, useEffect } from 'react';

const BASE_URL = 'http://localhost:8080';

const TeachingInfoService = {
  createTeachingInfo: async (accountId, specificClassId) => {
    const response = await fetch(`${BASE_URL}/teachingInfos`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify({ accountId, specificClassId }),
    });
    return response.json();
  },

  getTeachingInfo: async (teachingInfoId) => {
    const response = await fetch(`${BASE_URL}/teachingInfos/${teachingInfoId}`);
    return response.json();
  },

  getAllTeachingInfos: async () => {
    const response = await fetch(`${BASE_URL}/teachingInfos`);
    return response.json();
  },

  updateTeachingInfo: async (teachingInfoId, accountId, specificClassId) => {
    const response = await fetch(`${BASE_URL}/teachingInfos/${teachingInfoId}`, {
      method: 'PUT',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify({ accountId, specificClassId }),
    });
    return response.json();
  },

  deleteTeachingInfo: async (teachingInfoId) => {
    const response = await fetch(`${BASE_URL}/teachingInfos/${teachingInfoId}`, {
      method: 'DELETE',
    });
    return response.status;
  },

  deleteAllTeachingInfos: async () => {
    const response = await fetch(`${BASE_URL}/teachingInfos`, {
      method: 'DELETE',
    });
    return response.status;
  },

  getTeachingInfoByClassId: async (classId) => {
    const response = await fetch(`${BASE_URL}/specificClasses/${classId}/teachingInfo`);
    return response.json();
  },
};

const TeachingInfoComponent = () => {
  const [teachingInfos, setTeachingInfos] = useState([]);
  const [accountId, setAccountId] = useState('');
  const [specificClassId, setSpecificClassId] = useState('');

  useEffect(() => {
    const fetchTeachingInfos = async () => {
      const teachingInfosData = await TeachingInfoService.getAllTeachingInfos();
      setTeachingInfos(teachingInfosData);
    };
    fetchTeachingInfos();
  }, []);

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      const newTeachingInfo = await TeachingInfoService.createTeachingInfo(
        accountId,
        specificClassId
      );
      setTeachingInfos([...teachingInfos, newTeachingInfo]);
      setAccountId('');
      setSpecificClassId('');
    } catch (error) {
      console.error('Error creating teaching info:', error);
    }
  };

  return (
    <div>
      <h2>Teaching Infos</h2>
      <form onSubmit={handleSubmit}>
        <label>
          Instructor Account ID:
          <input type="number" value={accountId} onChange={(e) => setAccountId(e.target.value)} />
        </label>
        <label>
          Specific Class ID:
          <input type="number" value={specificClassId} onChange={(e) => setSpecificClassId(e.target.value)} />
        </label>
        <button type="submit">Create Teaching Info</button>
      </form>
      <ul>
        {teachingInfos.map((teachingInfo) => (
          <li key={teachingInfo.teachingInfoId}>
            {teachingInfo.instructor.name} - {teachingInfo.specificClass.specificClassName}
          </li>
        ))}
      </ul>
    </div>
  );
};

export default TeachingInfoComponent;