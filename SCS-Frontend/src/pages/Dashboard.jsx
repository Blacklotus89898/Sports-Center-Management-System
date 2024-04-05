import React from "react";
import { useParams } from "react-router-dom";
import { PageProvider } from "../providers/PageProvider";
import Sidebar from "../components/DashboardComponents/Sidebar";

import Profile from "../components/DashboardComponents/ProfileComponents/Profile";

export default function Dashboard() {
    const { path } = useParams()

    console.log("path", path);

    return (
        <PageProvider>
            <div className="flex flex-row">
                {/* sidebar */}
                <div className="w-1/6 min-w-1/6">
                    <Sidebar />
                </div>

                {/* paths */}
                {path === undefined && <>dashboard home</>}
                {path === "categories" && <>categoers</>}
                {path === "classes" && <>class</>}
                {path === "users" && <>users</>}
                {path === "schedule" && <>schedule</>}

                {path === "history" && <>history</>}

                {/* setting paths */}
                {path === "profile" && <Profile />}
                {path === "themes" && <>themes</>}
            </div>
            
        </PageProvider>
    );
}