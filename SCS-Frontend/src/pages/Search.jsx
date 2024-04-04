import React from "react";
import { useLocation } from 'react-router-dom';
import { PageProvider } from "../providers/PageProvider";

import ClassSearch from "../components/ClassSearch";
import ClassTypeList from "../components/ClassTypeList";
import ClassList from "../components/ClassList";

export default function Search() {
    const location = useLocation();
    const queryParams = new URLSearchParams(location.search);
    const classType = queryParams.get('classType');

    console.log(classType);

    return (
        <PageProvider>
            {/* search bar */}
            <ClassSearch />

            {/* horizontal line */}
            <div className="p-8">
                <hr className="border-base-200"/>
            </div>

            {/* class types list */}
            <ClassTypeList />

            {/* padding */}
            <div className="p-4" />

            {/* all classes + filter */}
            <ClassList />
        </PageProvider>
    );
}