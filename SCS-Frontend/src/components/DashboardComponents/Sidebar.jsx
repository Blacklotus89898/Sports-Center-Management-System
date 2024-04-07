import React, { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";

import { FiFolder, FiFile, FiUsers, FiClock, FiCalendar, FiUser, FiLifeBuoy } from "react-icons/fi";
import { getUserRole } from "../../utils/jotai";

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

    return (
        <div 
            className='flex flex-row items-center rounded-xl hover:cursor-pointer hover:bg-base-200'
            onClick={() => {navigate(path)}}
        >
            <button className="btn aspect-square rounded-xl bg-base-100">
                <div className="text-xl">
                    {children}
                </div>
            </button>
            <div
                className={`flex items-center overflow-hidden ${showTitle ? "px-3" : ""}`}
            >
                {showTitle ? title : ""}
            </div>
        </div>
    );
}

export default function Sidebar() {
    const [showTitle, setShowTitle] = useState(false);
    const debug = true;         // show all sidebar buttons for debugging (active high) 
    
    const handleResize = () => {
        if (window.innerWidth < 1000) {
            setShowTitle(false);
        } else {
            setShowTitle(true);
        }
    };

    useEffect(() => {
        handleResize();
        window.addEventListener("resize", handleResize);
        return () => {
            window.removeEventListener("resize", handleResize);
        };
    }, []);

    return (
        <div 
            className="fixed w-1/6 z-20 flex justify-center items-center rounded-lg"
        >
            {/* TODO: fix user role states and remove titles. */}
            <div className="flex flex-col space-y-1">
                {/* owner */}
                {debug && <>owner</>}
                {(getUserRole() === "OWNER" || debug) && 
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
                {debug && <>instructor</>}
                {(getUserRole() === "INSTRUCTOR" || debug) &&
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
                {debug && <>customer</>}
                {(getUserRole() === "CUSTOMER" || debug) &&
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