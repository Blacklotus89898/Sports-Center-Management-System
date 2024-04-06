import React from "react";
import { useNavigate } from "react-router-dom";

export default function ClassTypeButton({ classTypeName, classTypeIcon,  onClick }) {
    const navigate = useNavigate();

    return (
        <div
            className="flex flex-col p-2 justify-center items-center whitespace-nowrap hover:cursor-pointer"
            onClick={() => {navigate("/search?classType=" + classTypeName)}}
        >   
            {/* icon */}
            <span className="mr-1">{classTypeIcon}</span>

            {/* class type name */}
            {classTypeName}
        </div>
    );
}