import React, { useState, useEffect } from "react";
// import { CustomerService } from "../api/CustomerService"; //uncomment if you use service isntead of hooks
import useFetch from "../../api/useFetch";

const API_URL = 'http://localhost:8080/';

export default function CreateCustomer() {

    //states
    const [customer, setCustomer] = useState({
        id: -1,
        name: '',
        email: '',
        password: '',
        image: null,
    });

    //update of each field
    const handleChange = async (event) => {
        if (event.target.name === "image") {
            const file = event.target.files[0];
            const reader = new FileReader();
            
            reader.onload = function(e) {
                let base64Image = e.target.result;
                base64Image = base64Image.split(',')[1]; // Remove the data type from the Base64 string
              setCustomer({
                ...customer,
                ["image"]: base64Image // Remove the data type from the Base64 string
              });
            }
          
            reader.readAsDataURL(file);
          }
             else {
                setCustomer({
                    ...customer,
                    [event.target.name]: event.target.value
                });
            }
        }

        //fetch customers on load
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
            await fetchData(`${API_URL}customers`, {});
            console.log(data);
        }

        const getCustomerById = async () => {
            console.log(customer.id);
            await fetchData(`${API_URL}customers/${customer.id}`, {}); //make base URL a constant
        }

        const createCustomer = async (event) => {
            event.preventDefault(); //prevents reload //will be changed later
            // console.log(customer);
            await fetchData(`${API_URL}customers`, {
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
            await fetchData(`${API_URL}customers/${customer.id}`, {
                method: 'DELETE',
                headers: {
                    'Content-Type': 'application/json'
                },
            });
        }

        const updateCustomerById = async (event) => {
            event.preventDefault();
            await fetchData(`${API_URL}customers/${customer.id}`, {
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
                    <input type="file" name="image" onChange={handleChange} placeholder="Drop image" />
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
                {/* {Object.keys(customer).map(function (key) { return <div key={key}>Key: {key}, Value: {customer[key]}</div>; })} */}
                <div className="border-4 border-solid border-red-300">
                    <h1>Response</h1>
                    {/* <div> {loading ? "Loading" : JSON.stringify(data)}</div> */}
                    <div> {loading ? "Loading" : JSON.stringify(error)}</div>
                    <div  >

            {/* {data != null && data.constructor !== Array &&
                    <img src={`data:image/jpeg;base64,${data.image}`} alt="Customer" />
            } */}

                    </div>
                </div>

            </div>

        );
    }