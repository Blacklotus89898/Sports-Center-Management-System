import React from "react";
import { useParams } from "react-router-dom";
import { PageProvider } from "../providers/PageProvider";
import Sidebar from "../components/DashboardComponents/Sidebar";

import Profile from "../components/DashboardComponents/ProfileComponents/Profile";

export default function Dashboard() {
    const { path } = useParams()

    return (
        <PageProvider>
            <div className="flex flex-row">
                {/* sidebar */}
                <div className="w-1/6 min-w-1/6">
                    <Sidebar />
                </div>

                {/* main content */}
                {path === "profile" && <Profile />}
            </div>
            
        </PageProvider>
    );
}