export async function getCustomer() {
    return fetch('http://localhost:8080/customers')
        .then(response => response.json())
        .then(data => data);
}