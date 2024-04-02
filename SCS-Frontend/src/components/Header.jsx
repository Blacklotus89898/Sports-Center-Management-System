import React, { useState, useEffect } from 'react';
import { Link, useLocation } from "react-router-dom";
import Logo from './Logo';
import HeaderItem from './HeaderItem';

// header link components
const HeaderLink = ({name, link}) => {
    const location = useLocation();

    // check if pageExention and name is same
    // bold the text if so
    const isActive = location.pathname === link;

    return (
        <Link 
            className={`flex-none px-4 py-2 hover:bg-base-200 hover:text-primary-500 rounded-full ${isActive ? 'font-medium' : ''}`}
            to={link} 
        >
            {name}
        </Link>
    );
};

const Header = () => {
    const [isScrolled, setIsScrolled] = useState(false);

    useEffect(() => {
        const handleScroll = () => {
            setIsScrolled(window.scrollY > 0);
        };

        window.addEventListener('scroll', handleScroll);

        return () => {
            window.removeEventListener('scroll', handleScroll);
        };
    }, []);

    return (
        <div className={`sticky top-0 flex flex-col md:flex-row w-full items-center z-10 ${isScrolled ? 'p-2 invisible' : 'p-5'}`}
            style={{
                transition: 'padding 0.25s ease-in-out'
            }}
        >
            {/* logo */}
            <Logo />

            {/* Your header content goes here */}
            <div className='flex flex-row w-1/3 min-w-1/3 justify-center items-center'>
                <HeaderLink name='Home' link='/search' />
                <HeaderLink name='Schedule' link='/schedule' />
                <HeaderLink name='Dashboard' link='/dashboard' />
            </div>

            {/* header items */}
            <HeaderItem />
        </div>
    );
};

export default Header;