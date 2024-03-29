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
    try {
        return response.json();
    } catch (error) { //may occur if the response is not JSON or empty
        console.log("Error: ", error);
    }
}

export const deleteAllCustomers = async () => { //not tested
    const response = await fetch(`${API_URL}/customers`, {
        method: 'DELETE',
    });
    
    return response.json();
}

//downside: more computation, delegates object creation to the caller
// upside: more flexibility, less code written
//ex: reuestObject = {url: '/customers/', method: 'GET', id: ''}
export const customParser = async (requestObject) => {
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