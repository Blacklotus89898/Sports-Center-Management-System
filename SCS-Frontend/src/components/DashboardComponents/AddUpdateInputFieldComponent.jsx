import React from "react";

export default AddUpdateInputFieldComponent;

function AddUpdateInputFieldComponent({ id, title, placeholder, type }) {
    return (
        <div>
            <div className="text-sm pb-1">{title}</div>
            <input 
                id={id}
                type={type}
                className="input input-bordered w-full"
                placeholder={placeholder}
            />
        </div>
    );
}
