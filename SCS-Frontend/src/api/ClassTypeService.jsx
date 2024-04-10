const BASE_URL = "http://localhost:8080";

//diferent implementatiopn of fetch noe error catching, no response.json() checking
export const ClassTypeService = {
    createClassType: async function(classTypeDto) {
        return fetch(`${BASE_URL}/classType`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(classTypeDto),
        })
        .then(response => response.json());
    },

    getClassTypeByName: async function(className) {
        return fetch(`${BASE_URL}/classType/${className}`)
        .then(response => response.json());
    },

    getAllClassTypes: async function() {
        return fetch('${BASE_URL}/classTypes')
        .then(response => response.json());
    },

    updateClassTypeByName: async function(className, classTypeDto) {
        return fetch(`${BASE_URL}/classTypes/${className}`, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({ classTypeDto }),
        })
        .then(response => response.json());
    },

    deleteClassType: async function(className) {
        return fetch(`${BASE_URL}/classType/${className}`, {
            method: 'DELETE',
        });
    },

    deleteAllClassTypes: async function() {
        return fetch(`${BASE_URL}/classTypes`, {
            method: 'DELETE',
        });
    },

    getAllClassTypesByIsApproved: async function(isApproved) {
        return fetch(`${BASE_URL}/classTypes/approved/${isApproved}`)
        .then(response => response.json());
    },

    changeClassTypeApprovedStatus: async function(className, isApproved) {
        return fetch(`${BASE_URL}/classTypes/${className}/approved/${isApproved}`, {
            method: 'PUT',
        })
        .then(response => response.json());
    },
};