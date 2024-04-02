import React from "react";

export default function ClassTypeButton({ classTypeName, classTypeIcon,  onClick }) {
    return (
        <div
            className="flex flex-col p-2 justify-center items-center whitespace-nowrap hover:cursor-pointer"
        >   
            {/* icon */}
            <span className="mr-1">{classTypeIcon}</span>

            {/* class type name */}
            {classTypeName}
        </div>
    );
}