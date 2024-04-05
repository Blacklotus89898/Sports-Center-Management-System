import React, { useState } from "react";
import { useNavigate } from "react-router-dom";

import { FiFolder, FiFile, FiUsers, FiClock, FiCalendar, FiUser, FiLifeBuoy } from "react-icons/fi";
import { getUserRole } from "../../utils/jotai";

// owner
//  - class types
//  - classes
//  - users
//  - schedule

// instructor
//  - class types
//  - classes

// customer
//  - classes
//  - history

// button for sidebar w/ icons
function SidebarButton({ path, title, showTitle, children }) {
    let navigate = useNavigate();

    // Dynamic classes based on `showTitle` state
    const textClass = `transform transition-all duration-300 ${
        showTitle ? "translate-x-0 opacity-100" : "-translate-x-10 opacity-0"
    }`;

    return (
        <div 
            className="flex flex-row items-center hover:cursor-pointer"
            onClick={() => {navigate(path)}}
        >
            <button className="btn m-1 z-10 rounded-xl bg-base-100">
                {children}
            </button>
            <div
                className={`flex pl-2 items-center overflow-hidden ${textClass}`}
                style={{
                    transition: 'transform 300ms ease-in-out, opacity 300ms ease-in-out'
                }}
            >
                {title}
            </div>
        </div>
    );
}

export default function Sidebar() {
    const [showTitle, setShowTitle] = useState(false);

    return (
        <div 
            className="fixed pl-3 z-20 flex justify-center items-center"
            onMouseEnter={() => setShowTitle(true)}
            onMouseLeave={() => setShowTitle(false)}
        >
            <div className="flex flex-col">
                {/* owner */}
                {/* class types */}
                {getUserRole() === "OWNER" && 
                    <>
                        <SidebarButton title={"Class Categories"} showTitle={showTitle}>
                            <FiFolder />
                        </SidebarButton>
                        <SidebarButton title={"Classes"} showTitle={showTitle}>
                            <FiFile  />
                        </SidebarButton>
                        <SidebarButton title={"Users"} showTitle={showTitle}>
                            <FiUsers />
                        </SidebarButton>
                        <SidebarButton title={"Schedule"} showTitle={showTitle}>
                            <FiClock />
                        </SidebarButton>
                    </>
                }

                {/* instructor */}
                {getUserRole() === "INSTRUCTOR" &&
                    <>
                        <SidebarButton title={"Class Categories"} showTitle={showTitle}>
                            <FiFolder />
                        </SidebarButton>
                        <SidebarButton title={"Classes"} showTitle={showTitle}>
                            <FiFile  />
                        </SidebarButton>
                    </>
                }

                {/* customer */}
                {getUserRole() === "CUSTOMER" &&
                    <>
                        <SidebarButton title={"Classes"} showTitle={showTitle}>
                            <FiCalendar />
                        </SidebarButton>
                        <SidebarButton title={"Past Classes"} showTitle={showTitle}>
                            <FiClock />
                        </SidebarButton>
                    </>
                }

                {/* divider */}
                <hr className="w-1/4 my-5" />

                {/* all */}
                <SidebarButton path={"/dashboard/profile"} title={"Profile"} showTitle={showTitle}>
                    <FiUser />
                </SidebarButton>
                <SidebarButton title={"Themes"} showTitle={showTitle}>
                    <FiLifeBuoy />
                </SidebarButton>
            </div>
        </div>
    );
}