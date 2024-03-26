const API_URL = 'http://localhost:8080';

export const getAllCustomers= async () => { // tested
    const response = await fetch(`${API_URL}/customers`);
    return response.json();
}


export const getCustomerById = async (id) => { // tested
    const response = await fetch(`${API_URL}/customers/${id}`);
    return response.json();
}

export const createCustomer = async (customerDTO) => { // tested
    const response = await fetch(`${API_URL}/customers`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(customerDTO)
    });
    return response.json();
}

export const updateCustomerById = async (id, customerDTO) => { //not tested
    const response = await fetch(`${API_URL}/customers/${id}`, {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(customerDTO)
    });
    return response.json();
}

export const deleteCustomerById = async (id) => { //not tested //works but no response
    const response = await fetch(`${API_URL}/customers/${id}`, {
        method: 'DELETE',
    })
    //return response.json();
}

export const deleteAllCustomers = async () => { //not tested
    const response = await fetch(`${API_URL}/customers`, {
        method: 'DELETE',
    });
    return response.json();
}