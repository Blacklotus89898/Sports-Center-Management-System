import React, { useState, useEffect } from "react";
// import { createCustomer, customParser, deleteCustomerById, getAllCustomers, getCustomerById, updateCustomerById } from "../api/CustomerService";
import { CustomerService } from "../api/CustomerService";
import useFetch from "../api/useFetch";

export default function MockComponent() {

    // method 1, different state for each field
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

    const [requestObject, setRequestObject] = useState({
        url: '/customers/',
        method: null,
        id: null,
        body: null,
    });

    // const checkRequestObject = (customer) => {
    //     console.log(customer);
    //     setRequestObject( (customer) => {{
    //         ...requestObject,
    //         method: 'GET',
    //         id: customer.id,
    //         body: customer
    //     }});
    //     console.log(requestObject);
    // }
    const loadRequestObject = async () => {
        setRequestObject({
            ...requestObject,
            method: 'GET',
            id: CustomerId,
            body: customer
        });
        customParser(requestObject); //issue, delay in updating the state
    }
    const checkRequestObject = async () => { 
        // loadRequestObject();
        console.log(requestObject);
    }

    //modifing object of method 2
    const handleChange = (event) => {
        setCustomer({
            ...customer,
            [event.target.name]: event.target.value
        });
    }

    const handleSubmit = async (event) => {
        event.preventDefault();
        const customer = { name, email, password };
        const response = await createCustomer(customer); //local variable
        console.log(response);
    }

    //method2
    const createCustomer = async (event) => {
        event.preventDefault(); //prevenrts refresh
        // const response = await CustomerService.createCustomer(customer);//gloabl variable
        await fetchData( 'http://localhost:8080/customers', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(customer)
        });
        if (error != null) console.log(error);
        console.log(data);
    }

    const findAllCustomers = () => {
        console.log("Button clicked");
        CustomerService.getAllCustomers().then(data => console.log(data));
    }

    const findCustomerById = () => {
        console.log("Button clicked");
        CustomerService.getCustomerById(CustomerId).then(data =>  console.log(data) );
    }

    const deleteById = async () => {
        console.log("deleting", CustomerId);
        // CustomerService.deleteCustomerById(CustomerId);
        await fetchData( `http://localhost:8080/customers/${CustomerId}`, {
            method: 'DELETE',
            headers: {
                'Content-Type': 'application/json'
            },
        });
        console.log(error); //error works
        console.log(data);
    }

    const create = () => {}
    const updateCustomer = async (event) => {
        event.preventDefault();
        CustomerService.updateCustomerById(CustomerId, customer).then(data => {
            console.log(data);});
    }

    // //trigger the the hook, hooks are not callable, sad
    // const [triggerFetch, setTriggerFetch] = useState(false);

    // const { data: customerData, loading: isLoading, error: fetchError } = useFetch(
    //     'http://localhost:8080/customers',
    //     {
    //         method: 'GET',
    //         headers: {
    //             'Content-Type': 'application/json',
    //         },
    //     },
    //     [triggerFetch] 
    // );

    // const fetchData2 = () => {
    //     setTriggerFetch(prevState => !prevState);
    //     console.log(customerData);
    //     console.log(fetchError);
    //     console.log(isLoading);
    // };

    // const {data, loading, error, fetchData} = useFetch2('http://localhost:8080/customers', {})
    const {data, loading, error, fetchData} = useFetch()
    
    const fetchDatat = async () => {
        await fetchData('http://localhost:8080/customers', {});
        // console.log(data);
        // console.log(error);
        console.log(data);

        // fetchData(`http://localhost:8080/customers/${CustomerId}`, {}); //make base URL a constant
        // console.log(data);
        // console.log(error);
        // console.log(loading);

    }

    const fetchDataID = async () => {
        await fetchData(`http://localhost:8080/customers/${CustomerId}`, {}); //make base URL a constant
        // console.log(data);
        // console.log(error);
        // console.log(data);

    }


    

    // useEffect(() => {
    //     if (isLoading) {
    //         console.log('Loading...');
    //     } else if (fetchError) {
    //         console.log('Error:', fetchError);
    //     } else {
    //         console.log('Data:', customerData);
    //     }
    // }, [customerData]);

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
            <form onSubmit={createCustomer} className="border-4 border-solid border-green-300"> Method 2:
                <input type="text" name="name" value={customer.name} onChange={handleChange} placeholder="Name" required />
                <input type="email" name="email" value={customer.email} onChange={handleChange} placeholder="Email" required />
                <input type="password" name="password" value={customer.password} onChange={handleChange} placeholder="Password" required />
                <button type="submit">Create Customer</button>
            </form>

            {/* Update */}
            <form onSubmit={updateCustomer} className="border-4 border-solid border-green-300"> UPDATE
                <input type="text" name="name" value={customer.name} onChange={handleChange} placeholder="Name" required />
                <input type="email" name="email" value={customer.email} onChange={handleChange} placeholder="Email" required />
                <input type="password" name="password" value={customer.password} onChange={handleChange} placeholder="Password" required />
                <button type="submit">Update Customer</button>
            </form>

            <p>V1: {name}</p>
            <p>V2: {customer.name}</p>
            <button className="border-8 border-blue-300" onClick={loadRequestObject} disabled>Load the request body</button>
            <button className="border-8 border-blue-300" onClick={checkRequestObject} disabled>Check the request body</button>
            {/* <textarea name="" id="" cols="30" rows="10">{customer}</textarea>  */}

            {/* <button onClick={create}>Create</button> */}

                        {Object.keys(customer).map(function(key) { return <div>Key: {key}, Value: {customer[key]}</div>; })}
    
                        <button onClick={fetchDatat}>HOoking</button>
                        <button onClick={fetchDataID}>HOokingID</button>
                        <div> {loading ? "Loading" : JSON.stringify(data)}</div>
                        <div> {loading ? "Loading" : JSON.stringify(error)}</div>

                    </div>

            );
}