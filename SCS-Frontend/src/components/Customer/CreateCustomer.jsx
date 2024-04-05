import React, { useState, useEffect } from "react";
// import { CustomerService } from "../api/CustomerService"; //uncomment if you use service isntead of hooks
import useFetch from "../../api/useFetch";

export default function CreateCustomer() {

    //states
    const [customer, setCustomer] = useState({
        id: -1,
        name: '',
        email: '',
        password: ''
    });

    //update of each field
    const handleChange = (event) => {
        setCustomer({
            ...customer,
            [event.target.name]: event.target.value
        });
    }

    //get customers on load
    useEffect(() => {
        getCustomers();
    }, []);


    // useFetch hook
    // data: return value from the fetch
    // loading: use to render once loading stops
    // error: string
    // fetchData(url: string, options: {method: string, headers: {Content-Type: string}, body: string })
    const { data, loading, error, fetchData } = useFetch();  //instantiates the hook

    const getCustomers = async () => {
        await fetchData('http://localhost:8080/customers', {});
    }

    const getCustomerById = async () => {
        console.log(customer.id);
        await fetchData(`http://localhost:8080/customers/${customer.id}`, {}); //make base URL a constant
    }

    const createCustomer = async (event) => {
        event.preventDefault(); //prevents reload //will be changed later
        await fetchData('http://localhost:8080/customers', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(customer)
        });
        if (error != null) console.log(error);
    }

    const deleteCustomerById = async () => {
        console.log("deleting", customer.id);    //for testing
        await fetchData(`http://localhost:8080/customers/${customer.id}`, {
            method: 'DELETE',
            headers: {
                'Content-Type': 'application/json'
            },
        });
    }

    const updateCustomerById = async (event) => {
        event.preventDefault();
        await fetchData(`http://localhost:8080/customers/${customer.id}`, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(customer)
        });
    }


    return (
        <div className="border border-solid-black w-80">
            <h1>Mock Component</h1>
            <button onClick={getCustomers} className="border-4 border-blue-400">Get All</button>
            <input type="number" name="id" id="" value={customer.id} onChange={handleChange} placeholder="Enter id" />
            <button onClick={getCustomerById} className="border-4 border-green-500">Find By ID</button>
            <button onClick={deleteCustomerById} className="border-4 border-red-500">Delete By ID</button>


            {/* Method 2 */}
            <form onSubmit={createCustomer} className="border-4 border-solid border-green-300"> CREATE
                <input type="text" name="name" value={customer.name} onChange={handleChange} placeholder="Name" required />
                <input type="email" name="email" value={customer.email} onChange={handleChange} placeholder="Email" required />
                <input type="password" name="password" value={customer.password} onChange={handleChange} placeholder="Password" required />
                <button type="submit">Create Customer</button>
            </form>

            {/* Update */}
            <form onSubmit={updateCustomerById} className="border-4 border-solid border-orange-300"> UPDATE
                <input type="text" name="name" value={customer.name} onChange={handleChange} placeholder="Name" required />
                <input type="email" name="email" value={customer.email} onChange={handleChange} placeholder="Email" required />
                <input type="password" name="password" value={customer.password} onChange={handleChange} placeholder="Password" required />
                <button type="submit">Update Customer</button>
            </form>
                <h1>Object in form</h1>
                {Object.keys(customer).map(function (key) { return <div key={key}>Key: {key}, Value: {customer[key]}</div>; })}
            <div className="border-4 border-solid border-red-300">
                <h1>Response</h1>
                <div> {loading ? "Loading" : JSON.stringify(data)}</div>
                <div> {loading ? "Loading" : JSON.stringify(error)}</div>
            </div>

        </div>

    );
}