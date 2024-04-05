import React from "react";
import DashboardListItemComponent from "./DashboardListItemComponent";

// title: function that returns a string based on the content
// contents: array of objects
// search: string that is used to filter the contents
// filter: function? that is used to filter the contents
// format: function that returns a string based on the content (react component)
export default function DashboardListComponent({ title, contents, search, filter, format }) {
    return (
        <div className="flex flex-col w-full space-y-2 px-5">
            {/* list of contents */}
            {contents.map((content, index) => {
                if (filter(content)) {
                    return (
                        <DashboardListItemComponent title={title(content)} content={content} format={format} />
                    );
                }
            })}
        </div>
    );
}