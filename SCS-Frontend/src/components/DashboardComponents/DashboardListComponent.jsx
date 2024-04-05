import React from "react";
import DashboardListItemComponent from "./DashboardListItemComponent";

function searchFilter(content, search) {
    try {
        if (search === "" || search === undefined || search === null) {
            return true;
        }

        let contentString = JSON.stringify(content).toLowerCase();
        let searchString = search.toLowerCase();

        return contentString.includes(searchString);
    } catch (e) {
        return false;
    }
}

// title: function that returns a string based on the content
// contents: array of objects
// search: string that is used to filter the contents
// filter: function? that is used to filter the contents
// format: function that returns a string based on the content (react component)
export default function DashboardListComponent({ title, contents, search, filter, format }) {
    return (
        <div className="flex flex-col w-full space-y-2">
            {/* list of contents */}
            {contents.map((content, index) => {
                if (filter(content) && searchFilter(content, search)) {
                    return (
                        <DashboardListItemComponent title={title(content)} content={content} format={format} />
                    );
                }
            })}
        </div>
    );
}