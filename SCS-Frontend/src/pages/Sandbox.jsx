import React from 'react';
import MockComponent from '../components/MockComponent';
import CreateCustomer from '../components/Customer/Customer';

const Sandbox = () => {
    return (
        <div  className=''>
            <h1>Sandbox Page</h1>
            {/* <MockComponent /> */}
            <CreateCustomer></CreateCustomer>
            {/* Add your content here */}
        </div>
    );
};

export default Sandbox;