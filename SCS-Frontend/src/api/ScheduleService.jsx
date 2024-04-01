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