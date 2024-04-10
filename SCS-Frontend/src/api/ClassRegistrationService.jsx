// Base URL for the ClassRegistration API
const BASE_URL = "http://localhost:8080";

// ClassRegistration Service - not tested
const ClassRegistrationService = {
    // Function to create a new class registration
    createClassRegistration: async function(accountId, classId) {
        const response = await fetch(`${BASE_URL}/classRegistration`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({ accountId, classId })
        });
        return response.json();
    },

    // Function to get a class registration by id
    getClassRegistrationById: async function(registrationId) {
        const response = await fetch(`${BASE_URL}/classRegistration/${registrationId}`);
        return response.json();
    },

    // Function to get class registrations by class id
    getClassRegistrationByClassId: async function(classId) {
        const response = await fetch(`${BASE_URL}/specificClass/${classId}/classRegistration`);
        return response.json();
    },

    // Function to update a class registration by id
    updateClassRegistrationById: async function(registrationId, accountId, classId) {
        const response = await fetch(`${BASE_URL}/classRegistration/${registrationId}`, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({ accountId, classId })
        });
        return response.json();
    },

    // Function to delete a class registration by id
    deleteClassRegistrationById: async function(registrationId) {
        const response = await fetch(`${BASE_URL}/classRegistration/${registrationId}`, {
            method: 'DELETE'
        });
        return response.status;
    },

    // Function to delete all class registrations
    deleteAllClassRegistrations: async function() {
        const response = await fetch(`${BASE_URL}/classRegistrations`, {
            method: 'DELETE'
        });
        return response.status;
    }
}