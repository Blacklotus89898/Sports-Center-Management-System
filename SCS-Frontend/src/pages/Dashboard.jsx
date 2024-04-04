import React from "react";
import { PageProvider } from "../providers/PageProvider";
import Sidebar from "../components/DashboardComponents/Sidebar";

export default function Dashboard() {
    return (
        <PageProvider>
            <div className="flex flex-row">
                {/* sidebar */}
                <div className="w-1/6 min-w-1/6">
                    <Sidebar />
                </div>

                {/* main content */}
                <div className="flex flex-col w-full">
                    <div className="h-screen bg-orange-300">
                        hi
                    </div>
                    <div className="h-screen bg-blue-300">
                        hi
                    </div>
                    <div className="h-screen bg-orange-300">
                        hi
                    </div>
                    <div className="h-screen bg-orange-300">
                        hi
                    </div>
                    <div className="h-screen bg-blue-300">
                        hi
                    </div>
                    <div className="h-screen bg-orange-300">
                        hi
                    </div>
                    <div className="h-screen bg-orange-300">
                        hi
                    </div>
                    <div className="h-screen bg-blue-300">
                        hi
                    </div>
                    <div className="h-screen bg-orange-300">
                        hi
                    </div>
                </div>
            </div>
            
        </PageProvider>
    );
}