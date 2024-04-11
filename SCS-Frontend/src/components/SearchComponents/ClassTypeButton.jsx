import React from "react";
import { useNavigate } from "react-router-dom";

export default function ClassTypeButton({ classTypeName, classTypeIcon }) {
    const navigate = useNavigate();

    return (
        // class type button (all the icons in ClassTYpeList)
        <div
            className="flex flex-col p-2 justify-center items-center whitespace-nowrap hover:cursor-pointer hover:bg-base-200 hover:text-primary-500 rounded-lg transition-colors duration-300 ease-in-out"
            onClick={() => {classTypeName === "Home" ? navigate("/") : navigate("/search?classType=" + classTypeName)}}
        >   
            {/* icon */}
            <span className="mr-1 text-xl">{classTypeIcon}</span>

            {/* class type name */}
            {classTypeName}
        </div>
    );
}