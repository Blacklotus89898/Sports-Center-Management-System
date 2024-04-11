import React, { useState } from "react";
import { useLocation } from 'react-router-dom';
import { PageProvider } from "../providers/PageProvider";

import ClassSearch from "../components/SearchComponents/ClassSearch";
import ClassTypeList from "../components/SearchComponents/ClassTypeList";
import ClassList from "../components/SearchComponents/ClassList";

export default function Search() {
    const [search, setSearch] = useState("");

    const location = useLocation();
    const queryParams = new URLSearchParams(location.search);
    const classType = queryParams.get('classType');

    return (
        <PageProvider>
            {/* search bar */}
            <ClassSearch setSearch={setSearch} />

            {/* horizontal line */}
            <div className="p-8">
                <hr className="border-base-200"/>
            </div>

            {/* class types list */}
            <ClassTypeList />

            {/* padding */}
            <div className="p-4" />

            {/* all classes + filter */}
            <ClassList search={search} />
        </PageProvider>
    );
}