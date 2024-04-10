
const BASE_URL = 'http://localhost:8080';

export const CustomHoursService = {
  createCustomHours: async (customHoursDto) => {
    const response = await fetch(`${BASE_URL}/customHours`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify({ customHoursDto }),
    });
    return response.json();
  },

  getCustomHours: async (name, year) => {
    const response = await fetch(`${BASE_URL}/customHours/${name}/${year}`);
    return response.json();
  },

  getCustomHoursByDate: async (date) => {
    const response = await fetch(`${BASE_URL}/customHours/${date}`);
    return response.json();
  },

  updateCustomHours: async (name, year, customHoursDto ) => {
    const response = await fetch(`${BASE_URL}/customHours/${name}/${year}`, {
      method: 'PUT',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify({ customHoursDto }),
    });
    return response.json();
  },

  getAllCustomHours: async (year) => {
    const response = await fetch(`${BASE_URL}/customHours/${year}`);
    return response.json();
  },

  deleteCustomHours: async (name, year) => {
    const response = await fetch(`${BASE_URL}/customHours/${name}/${year}`, {
      method: 'DELETE',
    });
    return response.status;
  },

  deleteAllCustomHours: async (year) => {
    const response = await fetch(`${BASE_URL}/customHours/${year}`, {
      method: 'DELETE',
    });
    return response.status;
  },
};

