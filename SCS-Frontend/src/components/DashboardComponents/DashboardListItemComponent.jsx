import React from "react";

// title: string
// contents: array of objects
// format: function that returns a string based on the content (react component)
export default function DashboardListItemComponent({ title, content, format }) {
    return (
        <div className="collapse bg-base-200">
            <input type="checkbox" /> 
            <div className="collapse-title text-xl font-medium">
                {title}
            </div>
            <div className="collapse-content"> 
                {format(content)}
            </div>
        </div>
    );
}