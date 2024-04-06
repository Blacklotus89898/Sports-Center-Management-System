import React from "react";
import { useParams } from "react-router-dom";
import { PageProvider } from "../providers/PageProvider";
import Sidebar from "../components/DashboardComponents/Sidebar";

import Profile from "../components/DashboardComponents/ProfileComponents/Profile";
import Category from "../components/DashboardComponents/CategoryComponents/Category";
import Theme from "../components/DashboardComponents/ThemeComponents/Theme";

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
                <div className="w-5/6 pr-5">
                    {path === undefined && <>dashboard home</>}
                    {path === "categories" && <Category />}
                    {path === "classes" && <>class</>}
                    {path === "users" && <>users</>}
                    {path === "schedule" && <>schedule</>}

                    {path === "history" && <>history</>}

                    {/* setting paths */}
                    {path === "profile" && <Profile />}
                    {path === "themes" && <Theme />}
                </div>
            </div>
            
        </PageProvider>
    );
}