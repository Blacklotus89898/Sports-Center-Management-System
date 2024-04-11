import React, { createContext } from 'react';

import Footer from '../components/Footer';
import Header from '../components/HeaderComponents/Header'

// Create the provider component


const PageProvider = ({ children }) => {
    // Define the divs that can be inherited

    return (
        <div className='flex flex-col min-h-screen'>
            <Header />
            <div className='grow flex flex-col h-full'>
                {children}
            </div>
            <Footer />
        </div>
    );
};

export { PageProvider };