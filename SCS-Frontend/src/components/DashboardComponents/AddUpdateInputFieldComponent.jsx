import React from "react";

export default AddUpdateInputFieldComponent;

function AddUpdateInputFieldComponent({ id, title, placeholder, value, setValue, type, textfield = false, step = 1}) {
    return (
        <div>
            <div className="text-sm pb-1">{title}</div>
            {textfield ?
            <>
                <textarea  
                    id={id}
                    type={type}
                    value={value}
                    className="textarea textarea-bordered w-full"
                    placeholder={placeholder}
                    onChange={(e) => {setValue(e.target.value)}}
                >

                </textarea>
            </>
            :
            <>
                <input 
                    id={id}
                    type={type}
                    value={value}
                    className="input input-bordered w-full"
                    placeholder={placeholder}
                    step={type === "number" ? step : ""}
                    onChange={(e) => {type === "number" ? setValue(Math.max(0, e.target.value)) : setValue(e.target.value)}}
                />
            </>
            }
            
        </div>
    );
}
