import React from "react";
import DashboardListComponent from "../DashboardListComponent";

export default function Category() {
    const [search, setSearch] = React.useState("");
    const [filterCriteria, setFilterCriteria] = React.useState({});

    function formatContent(category) {
        console.log("category", category)
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
            "className": "classtype 5",
            "description": "description 5",
            "isApproved": true
        }
    ]

    return (
        <>
            {/* search & filter */}

            {/* list of categories */}
            <DashboardListComponent title={buildTitle} contents={categories} search={search} filter={filter} format={formatContent} />
        </>
    );
}