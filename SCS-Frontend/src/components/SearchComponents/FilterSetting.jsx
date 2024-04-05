import React from "react";

export default function FilterSetting({ children }) {
    return (
        <dialog id="filter_modal" className="modal">
        <div className="modal-box">
            <form method="dialog">
            <button className="btn btn-sm btn-circle btn-ghost absolute right-2 top-2">✕</button>
            </form>
            
            {/* filter settings */}
            {children}
        </div>
        </dialog>
    );
}