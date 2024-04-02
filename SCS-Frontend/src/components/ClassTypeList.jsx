import React, { useRef, useState, useEffect } from "react";
import ClassTypeButton from "./ClassTypeButton";

import { FiArrowLeftCircle, FiArrowRightCircle, FiFilter } from "react-icons/fi";
import FilterSetting from "./FilterSetting";

export default function ClassTypeList() {
    const [isOverflowing, setIsOverflowing] = useState(false);
    const scrollRef = useRef(null);
    const scrollAmount = 200;

    useEffect(() => {
        const checkOverflow = () => {
            if (scrollRef.current) {
                setIsOverflowing(scrollRef.current.scrollWidth > scrollRef.current.clientWidth);
            }
        };

        checkOverflow();
        window.addEventListener('resize', checkOverflow);

        return () => window.removeEventListener('resize', checkOverflow);
    }, []);

    const handleScroll = (offset) => {
        if (scrollRef.current) {
            scrollRef.current.scrollLeft += offset;
        }
    };

    return (
        <div className="flex flex-row py-4">
            {/* your ClassTypeButton components */}
            {isOverflowing && <button className="px-5" onClick={() => handleScroll(-scrollAmount)}>
                <FiArrowLeftCircle className="text-2xl" />
            </button>}

            <div
                ref={scrollRef}
                className="flex flex-row grow overflow-x-hidden"
            >
                <ClassTypeButton classTypeName="Lecture" classTypeIcon="📚" />
                <ClassTypeButton classTypeName="Lecture" classTypeIcon="📚" />
                <ClassTypeButton classTypeName="Lecture" classTypeIcon="📚" />
                <ClassTypeButton classTypeName="Lecture" classTypeIcon="📚" />
                <ClassTypeButton classTypeName="Lecture" classTypeIcon="📚" />
                <ClassTypeButton classTypeName="Lecture" classTypeIcon="📚" />
                <ClassTypeButton classTypeName="Lecture" classTypeIcon="📚" />
                <ClassTypeButton classTypeName="Lecture" classTypeIcon="📚" />
                <ClassTypeButton classTypeName="Lecture" classTypeIcon="📚" />
                <ClassTypeButton classTypeName="Lecture" classTypeIcon="📚" />
                <ClassTypeButton classTypeName="Lecture" classTypeIcon="📚" />
                <ClassTypeButton classTypeName="Lecture" classTypeIcon="📚" />
                <ClassTypeButton classTypeName="Lecture" classTypeIcon="📚" />
                <ClassTypeButton classTypeName="Lecture" classTypeIcon="📚" />
                <ClassTypeButton classTypeName="Lecture" classTypeIcon="📚" />
                <ClassTypeButton classTypeName="Lecture" classTypeIcon="📚" />
                <ClassTypeButton classTypeName="Lecture" classTypeIcon="📚" />
                <ClassTypeButton classTypeName="Lecture" classTypeIcon="📚" />
                <ClassTypeButton classTypeName="Lecture" classTypeIcon="📚" />
                <ClassTypeButton classTypeName="Lecture" classTypeIcon="📚" />
                <ClassTypeButton classTypeName="Lecture" classTypeIcon="📚" />
                <ClassTypeButton classTypeName="Lecture" classTypeIcon="📚" />
                <ClassTypeButton classTypeName="Lecture" classTypeIcon="📚" />
                <ClassTypeButton classTypeName="Lecture" classTypeIcon="📚" />
                <ClassTypeButton classTypeName="Lecture" classTypeIcon="📚" />
                <ClassTypeButton classTypeName="Lecture" classTypeIcon="📚" />
                <ClassTypeButton classTypeName="Lecture" classTypeIcon="📚" />
            </div>

            {isOverflowing && <button className="px-5" onClick={() => handleScroll(scrollAmount)}>
                <FiArrowRightCircle className="text-2xl" />
            </button>}
            
            <button 
                className="pr-2"
                onClick={()=>document.getElementById('filter_modal').showModal()}
            >
                <FiFilter className="text-2xl" />
            </button>

            <dialog id="filter_modal" className="modal">
            <div className="modal-box">
                <form method="dialog">
                <button className="btn btn-sm btn-circle btn-ghost absolute right-2 top-2">✕</button>
                </form>
                
                {/* filter settings */}
                <FilterSetting />
            </div>
            </dialog>
        </div>
    );
}