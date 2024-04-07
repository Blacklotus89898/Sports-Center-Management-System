import React, { useState, useEffect } from 'react';
import { Link, useLocation } from "react-router-dom";
import Logo from './Logo';
import HeaderItem from './HeaderItem';
import { isUserLoggedIn } from '../../utils/jotai';

import { FiLock } from "react-icons/fi";

export function SignInLogInModal({ children }) {
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [rememberMe, setRememberMe] = useState(false);

    const handleSubmit = (event) => {
        event.preventDefault();
        // Handle the login logic here
        console.log({ email, password, rememberMe });
    };

    return (
        <div className="flex flex-col w-full items-center justify-center">
            {/* lock icon */}
            <div className="relative">
                {/* bottom circle */}
                <div className="w-14 aspect-[1] bg-primary bg-opacity-50 rounded-full" />

                {/* top circle */}
                <div className="absolute top-2 left-2 w-10 aspect-[1] bg-primary bg-opacity-50 rounded-full flex items-center justify-center">
                    <FiLock className='text-xl' />
                </div>
            </div>

            {/* divder */}
            <div className="py-2"></div>

            {/* form */}
            <form 
                className='flex flex-col w-full items-center justify-center space-y-2'
                onSubmit={handleSubmit}
            >
                {children}
            </form>
        </div>
    )
}

// header link components
const HeaderLink = ({ name, link }) => {
    const location = useLocation();

    // Check if the current location matches the link to highlight the active link
    const isActive = location.pathname === link;

    return (
        <Link
            to={link}
            className={`flex-none px-4 py-2 rounded-full ${isActive ? 'font-medium' : ''} hover:bg-base-200 hover:text-primary-500`}
        >
            {name}
        </Link>
    );
};

const Header = () => {
    const [isScrolled, setIsScrolled] = useState(false);
    const modalInputClassNames = "input input-base-200 input-bordered focus:input-primary w-full mb-2";

    const location = useLocation();
    const isDashboard = location.pathname.toLowerCase().includes('dashboard');
    const headerPaddingClassNames = isDashboard ? "py-5 px-5" : "py-5 px-5 md:px-20";

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
            className={`sticky top-0 flex flex-col md:flex-row w-full items-center z-10 ${isScrolled ? 'invisible' : headerPaddingClassNames}`}
            style={{
                transition: 'padding 0.25s ease-in',
            }}
        >
            {/* logo */}
            <Logo />

            {/* header content goes here */}
            <div 
                className='flex flex-row w-1/3 min-w-1/3 justify-center items-center'
            >
                <HeaderLink name='Home' link='/search' />
                <HeaderLink name='Schedule' link='/schedule' />
                {isUserLoggedIn() && <HeaderLink name='Dashboard' link='/dashboard' />}
            </div>

            {/* header items */}
            <HeaderItem />

            {/* sign up panel */}
            {/* TODO: needs to be reworked */}
            <dialog id="sign_up_modal" className="modal">
            <div className="modal-box flex flex-col justify-center items-center content-center">
                <form method="dialog">
                <button className="btn btn-sm btn-circle btn-ghost absolute right-2 top-2">✕</button>
                </form>

                <SignInLogInModal>
                    <h3 className="font-bold text-lg">Welcome!</h3>
                    <p className="py-3 text-sm">Enter your details below to sign up!</p>
                    <div className='text-sm w-full justify-start'>Name</div>
                    <input type="text" placeholder="Name" className={modalInputClassNames} />

                    <div className='text-sm w-full justify-start pt-2'>Email</div>
                    <input type="text" placeholder="Email" className={modalInputClassNames} />

                    <div className='text-sm w-full justify-start pt-2'>Password</div>
                    <input type="password" placeholder="Password" className={modalInputClassNames} />
                    
                    {/* buttons */}
                    <div className="flex flex-row w-full py-2 space-x-2">
                        <button 
                            className="btn w-1/2"
                            onClick={() => {document.getElementById('sign_up_modal').close()}}
                        >
                            Cancel
                        </button>
                        <button className="btn w-1/2 btn-primary">Sign Up</button>
                    </div>

                    {/* switch account access modes */}
                    <div 
                        className='text-sm hover:cursor-pointer hover:underline'
                        onClick={()=>{
                            document.getElementById('sign_up_modal').close();
                            document.getElementById('log_in_modal').showModal();
                        }}
                    >
                        Don't have an account?
                    </div>
                </SignInLogInModal>
            </div>
            </dialog>

            {/* log in panel */}
            <dialog id="log_in_modal" className="modal">
            <div className="modal-box flex flex-col justify-center items-center content-center">
                <form method="dialog">
                <button className="btn btn-sm btn-circle btn-ghost absolute right-2 top-2">✕</button>
                </form>

                {/* login form */}
                <SignInLogInModal>
                    <h3 className="font-bold text-lg">Welcome back!</h3>
                    <p className="py-3 text-sm">Enter your details below to log in!</p>

                    <div className='text-sm w-full justify-start pt-2'>Email</div>
                    <input type="text" placeholder="Email" className={modalInputClassNames} />

                    <div className='text-sm w-full justify-start pt-2'>Password</div>
                    <input type="password" placeholder="Password" className={modalInputClassNames} />
                    
                    {/* buttons */}
                    <div className="flex flex-row w-full py-2 space-x-2">
                        <button 
                            className="btn w-1/2"
                            onClick={() => {document.getElementById('sign_up_modal').close()}}
                        >
                            Cancel
                        </button>
                        <button className="btn w-1/2 btn-primary">Log in</button>
                    </div>

                    {/* switch account access modes */}
                    <p 
                        className='text-sm hover:cursor-pointer hover:underline'
                        onClick={()=>{
                            document.getElementById('log_in_modal').close();
                            document.getElementById('sign_up_modal').showModal();
                        }}
                    >
                        Already have an account?
                    </p>
                </SignInLogInModal>
            </div>
            </dialog>
        </div>
    );
};

export default Header;