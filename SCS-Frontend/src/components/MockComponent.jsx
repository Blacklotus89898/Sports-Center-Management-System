import React from "react";
import { getCustomer } from "../services/CustomerService";

export default function MockComponent() {
    const getCustomerB = () => {
        console.log("Button clicked");
        getCustomer().then(data => console.log(data));
    }
    return (
        <div className="border border-solid-black w-40">
            <h1>Mock Component</h1>
            <button onClick={getCustomerB}>The button</button>
        </div>
    );
}