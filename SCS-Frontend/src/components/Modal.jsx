import React from "react";

export default function Modal({ id, children }) {
    return (
        <dialog id={id} className="modal">
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