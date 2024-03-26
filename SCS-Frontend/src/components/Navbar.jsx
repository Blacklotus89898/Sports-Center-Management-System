import React from 'react';
import { Link } from "react-router-dom";


const Navbar = () => {
    return (
        <nav className='border border-solid-blue'>
            <ul>
                <li><Link to="/">Home</Link></li>
                <li><Link to="/temp">Temporary</Link></li>
                <li><Link to="/Service">Services</Link></li>
                <li><Link to="/Contact">Contact</Link></li>
                <li><Link to="/Sandbox">Sandbox</Link></li>
            </ul>
        </nav>
    );
}

export default Navbar;