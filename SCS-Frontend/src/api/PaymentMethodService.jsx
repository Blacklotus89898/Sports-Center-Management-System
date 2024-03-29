const API_URL = 'http://localhost:8080';


//no error catching only barebone implementation
export const PaymentMethodService = {
    getAllPaymentMethod: async () => {
        const response = await fetch(`${API_URL}/paymentMethod`);
        return response.json();
    },
    getPaymentMethodById: async (id) => {
        const response = await fetch(`${API_URL}/paymentMethod/${id}`);
        return response.json();
    },
    getPaymentMethodByAccountId: async (accountId) => {
        const response = await fetch(`${API_URL}/customers/${accountId}/paymentMethod`);
        return response.json();
    },
    createPaymentMethod: async (paymentMethodDTO) => {
        const response = await fetch(`${API_URL}/paymentMethod`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(paymentMethodDTO)
        });
        return response.json();
    },
    updatePaymentMethodById: async (id, paymentMethodDTO) => {
        const response = await fetch(`${API_URL}/paymentMethod/${id}`, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(paymentMethodDTO)
        });
        return response.json();
    },
    deletePaymentMethodById: async (id) => {
        const response = await fetch(`${API_URL}/paymentMethod/${id}`, {
            method: 'DELETE',
        });
        try {
            return response.json();
        } catch (error) {
            console.log("Error: ", error);
        }
    },
    deleteAllPaymentMethod: async () => {
        const response = await fetch(`${API_URL}/paymentMethod`, {
            method: 'DELETE',
        });
        return response.json();
    }
};