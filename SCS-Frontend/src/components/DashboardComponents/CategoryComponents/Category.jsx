import React from "react";
import DashboardListComponent from "../DashboardListComponent";
import DashboardSearchComponent from "../DashboardSearchComponent";

export default function Category() {
    const [search, setSearch] = React.useState("");
    const [filterCriteria, setFilterCriteria] = React.useState({});

    function formatContent(category) {
        return (
            <div>
                <div className="text-red-400">{category.className}</div>
                <div>{category.description}</div>
                <div>{category.isApproved}</div>
            </div>
        );
    }
    
    function buildTitle(category) {
        return category.icon + "  " + category.className;
    }
    
    function filter(category) {
        // use filterCriteria to filter the content

       return true;
    }

    let categories = [
        {
            "icon": "🗼",
            "className": "classtype 1",
            "description": "description 1",
            "isApproved": true
        },
        {
            "icon": "🗼",
            "className": "classtype 2",
            "description": "description 2",
            "isApproved": false
        },
        {
            "icon": "🗼",
            "className": "classtype 3",
            "description": "description 3",
            "isApproved": true
        },
        {
            "icon": "🗼",
            "className": "classtype 4",
            "description": "description 4",
            "isApproved": false
        },
        {
            "icon": "🗼",
            "className": "classtype 2",
            "description": "description 2",
            "isApproved": false
        },
        {
            "icon": "🗼",
            "className": "classtype 3",
            "description": "description 3",
            "isApproved": true
        },
        {
            "icon": "🗼",
            "className": "classtype 4",
            "description": "description 4",
            "isApproved": false
        },
        {
            "icon": "🗼",
            "className": "classtype 2",
            "description": "description 2",
            "isApproved": false
        }
    ]

    return (
        <>
            {/* search & filter */}
            <DashboardSearchComponent setSearch={setSearch} setFilter={setFilterCriteria} contents={categories} />

            {/* line */}
            <hr className="my-5" />

            {/* list of categories */}
            <DashboardListComponent title={buildTitle} contents={categories} search={search} filter={filter} format={formatContent} />
        </>
    );
}