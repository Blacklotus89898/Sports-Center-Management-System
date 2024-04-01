const BASE_URL = 'http://localhost:8080';

const TeachingInfoService = {
  createTeachingInfo: async (teachingInfoRequestDto) => {
    const response = await fetch(`${BASE_URL}/teachingInfo`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(teachingInfoRequestDto),
    });
    return response.json();
  },

  getTeachingInfo: async (teachingInfoId) => {
    const response = await fetch(`${BASE_URL}/teachingInfo/${teachingInfoId}`);
    return response.json();
  },

  getAllTeachingInfos: async () => {
    const response = await fetch(`${BASE_URL}/teachingInfos`);
    return response.json();
  },

  updateTeachingInfo: async (teachingInfoId, teachingInfoRequestDto) => {
    const response = await fetch(`${BASE_URL}/teachingInfo/${teachingInfoId}`, {
      method: 'PUT',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(teachingInfoRequestDto),
    });
    return response.json();
  },

  deleteTeachingInfo: async (teachingInfoId) => {
    const response = await fetch(`${BASE_URL}/teachingInfo/${teachingInfoId}`, {
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
    const response = await fetch(`${BASE_URL}/specificClass/${classId}/teachingInfo`);
    return response.json();
  },
};
