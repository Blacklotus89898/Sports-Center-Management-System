const BASE_URL = 'http://localhost:8080';

export const OpeningHoursService = {
  createOpeningHours: async (year, openingHoursDto) => {
    const response = await fetch(`${BASE_URL}/schedules/${year}/openingHours`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(openingHoursDto),
    });
    return response.json();
  },

  getOpeningHoursByDay: async (year, day) => {
    const response = await fetch(`${BASE_URL}/schedules/${year}/openingHours/${day}`);
    return response.json();
  },

  updateOpeningHours: async (year, day, openingHoursDto) => {
    const response = await fetch(`${BASE_URL}/schedules/${year}/openingHours/${day}`, {
      method: 'PUT',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(openingHoursDto),
    });
    return response.json();
  },

  getAllOpeningHours: async (year) => {
    const response = await fetch(`${BASE_URL}/schedules/${year}/openingHours`);
    return response.json();
  },

  deleteOpeningHours: async (year, day) => {
    const response = await fetch(`${BASE_URL}/schedules/${year}/openingHours/${day}`, {
      method: 'DELETE',
    });
    return response.status;
  },

  deleteAllOpeningHours: async (year) => {
    const response = await fetch(`${BASE_URL}/schedules/${year}/openingHours`, {
      method: 'DELETE',
    });
    return response.status;
  },
};
