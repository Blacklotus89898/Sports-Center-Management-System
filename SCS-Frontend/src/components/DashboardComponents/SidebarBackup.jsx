import React, { useState } from "react";
import { useNavigate } from "react-router-dom";

import { FiFolder, FiFile, FiUsers, FiClock, FiCalendar, FiUser, FiLifeBuoy } from "react-icons/fi";
import { getUserRole } from "../../utils/auth";

// owner
//  - class types   "/categories"
//  - classes       "/classes"
//  - users         "/users"
//  - schedule      "/schedule"

// instructor
//  - class types   "/categories"
//  - classes       "/classes"

// customer
//  - classes       "/classes"
//  - history       "/history"

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
            <button className="btn aspect-square rounded-xl bg-base-100">
                <div className="text-xl">
                    {children}
                </div>
            </button>
            <div
                className={`flex items-center overflow-hidden ${textClass} ${showTitle ? "pl-3" : ""}`}
                style={{
                    transition: 'transform 300ms ease-in-out, opacity 300ms ease-in-out'
                }}
            >
                {showTitle ? title : ""}
            </div>
        </div>
    );
}

export default function Sidebar() {
    const [showTitle, setShowTitle] = useState(false);

    return (
        <div 
            className="fixed w-1/6 z-20 flex justify-center items-center rounded-lg"
            onMouseEnter={() => setShowTitle(true)}
            onMouseLeave={() => setShowTitle(false)}
            style={{
                backdropFilter: showTitle ? "blur(15px)" : "none",
                transition: "backdrop-filter 300ms ease-in-out"
            }}
        >
            <div className="flex flex-col space-y-1">
                {/* owner */}
                {/* class types */}
                {getUserRole() === "OWNER" && 
                    <>
                        <SidebarButton path={"/dashboard/categories"} title={"Class Categories"} showTitle={showTitle}>
                            <FiFolder />
                        </SidebarButton>
                        <SidebarButton path={"/dashboard/classes"} title={"Classes"} showTitle={showTitle}>
                            <FiFile  />
                        </SidebarButton>
                        <SidebarButton path={"/dashboard/users"} title={"Users"} showTitle={showTitle}>
                            <FiUsers />
                        </SidebarButton>
                        <SidebarButton path={"/dashboard/schedule"} title={"Schedule"} showTitle={showTitle}>
                            <FiClock />
                        </SidebarButton>
                    </>
                }

                {/* instructor */}
                {getUserRole() === "INSTRUCTOR" &&
                    <>
                        <SidebarButton path={"/dashboard/categories"} title={"Class Categories"} showTitle={showTitle}>
                            <FiFolder />
                        </SidebarButton>
                        <SidebarButton path={"/dashboard/classes"} title={"Classes"} showTitle={showTitle}>
                            <FiFile  />
                        </SidebarButton>
                    </>
                }

                {/* customer */}
                {getUserRole() === "CUSTOMER" &&
                    <>
                        <SidebarButton path={"/dashboard/classes"} title={"Classes"} showTitle={showTitle}>
                            <FiCalendar />
                        </SidebarButton>
                        <SidebarButton path={"/dashboard/history"} title={"Past Classes"} showTitle={showTitle}>
                            <FiClock />
                        </SidebarButton>
                    </>
                }

                <p className="pt-4 text-sm"></p>

                {/* all */}
                <SidebarButton path={"/dashboard/profile"} title={"Profile"} showTitle={showTitle}>
                    <FiUser />
                </SidebarButton>
                <SidebarButton path={"/dashboard/themes"} title={"Themes"} showTitle={showTitle}>
                    <FiLifeBuoy />
                </SidebarButton>
            </div>
        </div>
    );
}