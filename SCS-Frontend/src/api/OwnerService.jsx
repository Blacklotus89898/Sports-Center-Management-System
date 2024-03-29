const API_URL = 'http://localhost:8080'; // replace with your API base URL

//not tested
//error catching, maybe we should return the error message as str so no error in the console
const OwnerService = {
    getOwnerById: async (id) => {
        const response = await fetch(`${API_URL}/owner/${id}`);
        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }
        return response.json();
    },

    createOwner: async (ownerRequestDto) => {
        const response = await fetch(`${API_URL}/owner`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(ownerRequestDto),
        });

        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }

        return response.json();
    },


  updateOwner: async (id, ownerRequestDto) => {
    const response = await fetch(`${API_URL}/owner/${id}`, {
      method: 'PUT',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(ownerRequestDto),
    });

    if (!response.ok) {
      throw new Error(`HTTP error! status: ${response.status}`);
    }

    return response.json();
  },

  deleteOwner: async (id) => {
    const response = await fetch(`${API_URL}/owner/${id}`, {
      method: 'DELETE',
    });

    if (!response.ok) {
      throw new Error(`HTTP error! status: ${response.status}`);
    }
  },
};

export default OwnerService;