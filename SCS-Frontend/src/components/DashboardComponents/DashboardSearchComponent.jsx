import React from "react";

import FilterSetting from "../SearchComponents/FilterSetting";

import { FiSearch, FiSliders,  FiPlus } from "react-icons/fi";

export default function DashboardSearchComponent({ setSearch, contents, showAdd = true }) {
    return (
        <div className="flex flex-row w-full justify-center content-center">
            {/* search bar */}
            <div className="relative grow justify-center content-center">
                <span className="absolute inset-y-0 left-0 flex justify-center items-center pl-3">
                    <div className="p-1">
                        <FiSearch />
                    </div>
                </span>
                <input
                    className="input input-bordered pl-10 w-full rounded-xl shadow"
                    type="text"
                    onChange={(e) => { setSearch(e.target.value) }}
                    placeholder="looking for something specific? 👀"
                />
            </div>

            {/* padding */}
            <div className="px-1" />

            {/* filter button */}
            <div className="flex w-fit">
                {showAdd && <button 
                    className="btn rounded-xl text-lg"
                    onClick={()=>document.getElementById('add_modal').showModal()}
                >
                    <FiPlus className="text-2xl sm" />
                </button>}

                {/* seperation padding */}
                <div className="px-1" />

                <button 
                    className="btn rounded-xl text-lg"
                    onClick={()=>document.getElementById('filter_modal').showModal()}
                >
                    <FiSliders className="text-2xl sm" />
                </button>
            </div>
        </div>
    )
}