import React, { useState, useEffect } from "react";
import { FiSearch } from "react-icons/fi";
import Logo from "../HeaderComponents/Logo";
import HeaderItem from "../HeaderComponents/HeaderItem";

export default function Search() {
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
        <div 
            className={`sticky top-0 flex flex-row justify-center content-center bg-base-100 ${isScrolled ? 'py-5 px-5 md:px-20' : ''}`}
            style={{
                transition: 'padding 0.25s ease-in',
            }}
        >
            {/* faux logo here when scrolling */}
            { isScrolled && <Logo /> }

            {/* search bar goes here */}
            <div className="relative w-4/5 max-w-lg justify-center content-center">
                <span className="absolute inset-y-0 left-0 flex justify-center items-center pl-3">
                    <div className="p-1">
                        <FiSearch />
                    </div>
                </span>
                <input
                    className="input input-bordered pl-10 w-full rounded-full shadow"
                    type="text" 
                    placeholder="looking for a class? 👀"
                />
            </div>

            {/* faux login form thing here yaya */}
            { isScrolled && <HeaderItem /> }
        </div>
    );
}