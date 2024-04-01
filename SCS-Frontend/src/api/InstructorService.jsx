const BASE_URL = 'http://localhost:8080';

const InstructorService = {
  createInstructor: async (instructorRequestDto) => {
    const response = await fetch(`${BASE_URL}/instructors`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(instructorRequestDto),
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

  updateInstructor: async (id, instructorDto) => {
    const response = await fetch(`${BASE_URL}/instructors/${id}`, {
      method: 'PUT',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(instructorDto),
    });
    return response.json();
  },

  deleteInstructor: async (id) => {
    const response = await fetch(`${BASE_URL}/instructors/${id}`, {
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
