const API_URL = 'http://localhost:8080';

export const CustomerService = {

    getAllCustomers: async () => { // tested
        const response = await fetch(`${API_URL}/customers`);
        return response.json();
    },


    getCustomerById: async (id) => { // tested
        const response = await fetch(`${API_URL}/customers/${id}`);
        return response.json();
    },

    createCustomer: async (customerDTO) => { // tested
        const response = await fetch(`${API_URL}/customers`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(customerDTO)
        });
        return response.json();
    },

updateCustomerById: async (id, customerDTO) => { //tested
        const response = await fetch(`${API_URL}/customers/${id}`, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(customerDTO)
        });
        return response.json();
    },

deleteCustomerById: async (id) => { //not tested //works but no response
        const response = await fetch(`${API_URL}/customers/${id}`, {
            method: 'DELETE',
        })
        try {
            return response.json();
        } catch (error) { //may occur if the response is not JSON or empty
            console.log("Error: ", error);
        }
    },

 deleteAllCustomers: async () => { //not tested
        const response = await fetch(`${API_URL}/customers`, {
            method: 'DELETE',
        });

        return response.json();
    },

//downside: more computation, delegates object creation to the caller
// upside: more flexibility, less code written
//ex: reuestObject = {url: '/customers/', method: 'GET', id: ''}
customParser : async (requestObject) => {
        const response = await fetch(`${API_URL}${requestObject.url}${requestObject.id}`, {
            method: requestObject.method,
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(requestObject.body)
        });
        try {
            return response.json();
        } catch (error) { //may occur if the response is not JSON or empty
            console.log("Error: ", error);
        }
    }
}
