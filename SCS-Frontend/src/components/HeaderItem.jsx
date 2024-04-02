import React, { useState } from "react";
import { FiMenu, FiUser } from "react-icons/fi";

// component used for right side of header items
// login, themes, etc
export default function HeaderItem() {
    return (
        <div className="flex w-1/3 min-w-1/3 justify-center md:justify-end">
            <div className="dropdown dropdown-bottom dropdown-end">
                <div 
                    tabIndex={0} 
                    role="button" 
                    className="btn m-1 rounded-full bg-base-100"
                >
                    <FiMenu className="text-primary text-lg" />
                    <FiUser className="text-primary text-lg" />
                </div>
                <ul tabIndex={0} className="dropdown-content z-[10] menu p-2 shadow bg-base-100 rounded-box w-52">
                    <li><a>sign up</a></li>
                    <li><a>log in</a></li>
                    <li><a>profile settings</a></li>
                    <li><a>logout</a></li>
                </ul>
            </div>
        </div>
    );
}