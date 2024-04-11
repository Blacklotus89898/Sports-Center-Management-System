import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import { FiMenu, FiUser } from "react-icons/fi";

import { isUserLoggedIn, useLogout } from "../../utils/auth";

// component used for right side of header items
// login, themes, etc
export default function HeaderItem() {
    let navigate = useNavigate();
    let logout = useLogout();

    return (
        <div className="flex w-1/3 min-w-1/3 justify-center md:justify-end">
            {/* drop down component */}
            <div className="dropdown dropdown-bottom dropdown-end">
                <div 
                    tabIndex={0} 
                    role="button" 
                    className="btn m-1 rounded-full bg-base-100"
                >
                    {/* button icons */}
                    <FiMenu className="text-primary-500 text-lg" />
                    <FiUser className="text-primary-500 text-lg" />
                </div>
                <ul tabIndex={0} className="dropdown-content z-[10] menu p-2 shadow bg-base-100 rounded-box w-52">
                    {isUserLoggedIn() ?  
                        <>
                            <li
                                onClick={() => navigate('/dashboard/profile')}
                            ><a>profile settings</a></li>
                            <li
                                onClick={() => {logout(); navigate('/')}}
                            ><a>logout</a></li>
                        </>
                    :
                        <>
                            <li 
                                onClick={()=>document.getElementById('sign_up_modal').showModal()}
                            ><a>sign up</a></li>
                            <li 
                                onClick={()=>document.getElementById('log_in_modal').showModal()}
                            ><a>log in</a></li>
                        </>
                    }
                </ul>
            </div>
        </div>
    );
}