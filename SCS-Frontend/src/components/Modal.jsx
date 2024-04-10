import React from "react";

export default function Modal({ id, children, width }) {
    return (
        <dialog id={id} className="modal">
        <div className={`modal-box ${width ? width : ""}`}>
            <form method="dialog">
            <button className="btn btn-sm btn-circle btn-ghost absolute right-2 top-2">✕</button>
            </form>
            
            {children}
        </div>
        </dialog>
    );
}