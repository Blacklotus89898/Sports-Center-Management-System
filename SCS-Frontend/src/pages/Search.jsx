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

            {/* class types list */}
            <ClassTypeList />

            {/* all classes + filter */}
            <ClassList />
        </PageProvider>
    );
}