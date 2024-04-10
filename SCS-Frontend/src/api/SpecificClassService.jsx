const BASE_URL = 'http://localhost:8080';

const SpecificClassService = {
  createSpecificClass: async (specificClassRequestDto) => {
    const response = await fetch(`${BASE_URL}/specificClass`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(specificClassRequestDto),
    });
    return response.json();
  },

  getSpecificClass: async (classId) => {
    const response = await fetch(`${BASE_URL}/specificClass/${classId}`);
    return response.json();
  },

  updateSpecificClass: async (classId, specificClassRequestDto) => {
    const response = await fetch(`${BASE_URL}/specificClass/${classId}`, {
      method: 'PUT',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(specificClassRequestDto),
    });
    return response.json();
  },

  getAllSpecificClasses: async (year) => {
    const response = await fetch(`${BASE_URL}/specificClass/year/${year}`);
    return response.json();
  },

  deleteSpecificClass: async (classId) => {
    const response = await fetch(`${BASE_URL}/specificClass/${classId}`, {
      method: 'DELETE',
    });
    return response.status;
  },

  deleteAllSpecificClasses: async () => {
    const response = await fetch(`${BASE_URL}/specificClass`, {
      method: 'DELETE',
    });
    return response.status;
  },

  deleteAllSpecificClassesByYear: async (year) => {
    const response = await fetch(`${BASE_URL}/specificClass/year/${year}`, {
      method: 'DELETE',
    });
    return response.status;
  },
};
