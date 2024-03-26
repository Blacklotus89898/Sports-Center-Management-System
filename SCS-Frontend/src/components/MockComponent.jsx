import React, { useState } from "react";
import { createCustomer, deleteCustomerById, getAllCustomers, getCustomerById } from "../services/CustomerService";

export default function MockComponent() {
    const [CustomerId, setCustomerId] = useState(-1);
    const [name, setName] = useState('');
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');

    //method 2
    const [customer, setCustomer] = useState({
        id: -1,
        name: '',
        email: '',
        password: ''
    });

    const handleChange = (event) => {
        setCustomer({
            ...customer,
            [event.target.name]: event.target.value
        });
    }

    const handleSubmit = async (event) => {
        event.preventDefault();
        const customer = { name, email, password };
        const response = await createCustomer(customer);
        console.log(response);
    }

    const handleSubmit2 = async (event) => {
        event.preventDefault();
        const response = await createCustomer(customer);
        console.log(response);
    }

    const findAllCustomers = () => {
        console.log("Button clicked");
        getAllCustomers().then(data => console.log(data));
    }

    const findCustomerById = () => {
        console.log("Button clicked");
        getCustomerById(CustomerId).then(data => console.log(data));
    }

    const deleteById = () => {
        console.log("deleting", CustomerId);
        deleteCustomerById(CustomerId);
    }

    const create = () => {}
    const updateCustomer = () => {}

    return (
        <div className="border border-solid-black w-60">
            <h1>Mock Component</h1>
            <button onClick={findAllCustomers} className="border-4 border-blue-400">All</button>
            <input type="number" name="" id="" onChange={(e) => setCustomerId(e.target.value)} placeholder="Enter id"/>
            <button onClick={findCustomerById} className="border-4 border-green-500">Find By ID</button>
            <button onClick={deleteById} className="border-4 border-red-500">delete By ID</button>


            <form onSubmit={handleSubmit} className="border-4  border-solid border-red-300">
            <input type="text" value={name} onChange={e => setName(e.target.value)} placeholder="Name" required />
            <input type="email" value={email} onChange={e => setEmail(e.target.value)} placeholder="Email" required />
            <input type="password" value={password} onChange={e => setPassword(e.target.value)} placeholder="Password" required />
            <button type="submit">Create Customer</button>
            </form>

            {/* Method 2 */}
            <form onSubmit={handleSubmit2} className="border-4 border-solid border-green-300"> Method 2:
                <input type="text" name="name" value={customer.name} onChange={handleChange} placeholder="Name" required />
                <input type="email" name="email" value={customer.email} onChange={handleChange} placeholder="Email" required />
                <input type="password" name="password" value={customer.password} onChange={handleChange} placeholder="Password" required />
                <button type="submit">Create Customer</button>
            </form>

            <p>V1: {name}</p>
            <p>V2: {customer.name}</p>
            {/* <textarea name="" id="" cols="30" rows="10">{customer}</textarea>  */}

            {/* <button onClick={create}>Create</button> */}
            {/* <button onClick={updateCustomer}>Update</button> */}
            
        </div>
    );
}