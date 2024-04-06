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
        <div className="flex flex-row justify-center items-center mx-5 md:mx-20">
            {/* your ClassTypeButton components */}
            {isOverflowing && <button className="pr-5" onClick={() => handleScroll(-scrollAmount)}>
                <FiArrowLeftCircle className="text-2xl" />
            </button>}

            {/* list of class types */}
            <div
                ref={scrollRef}
                className="flex flex-row grow overflow-x-hidden"
            >
                <ClassTypeButton classTypeName="Lecture 1" classTypeIcon="📚" />
                <ClassTypeButton classTypeName="Lecture 2" classTypeIcon="📚" />
                <ClassTypeButton classTypeName="Lecture 3" classTypeIcon="📚" />
                <ClassTypeButton classTypeName="Lecture 4" classTypeIcon="📚" />
                <ClassTypeButton classTypeName="Lecture 5" classTypeIcon="📚" />
                <ClassTypeButton classTypeName="Lecture 6" classTypeIcon="📚" />
                <ClassTypeButton classTypeName="Lecture 7" classTypeIcon="📚" />
                <ClassTypeButton classTypeName="Lectuaorsntoanrostnoarstnre 8" classTypeIcon="📚" />
                <ClassTypeButton classTypeName="Lecture 9" classTypeIcon="📚" />
                <ClassTypeButton classTypeName="Lecture 10" classTypeIcon="📚" />
                <ClassTypeButton classTypeName="Lecture 11" classTypeIcon="📚" />
                <ClassTypeButton classTypeName="Lecture 1" classTypeIcon="📚" />
                <ClassTypeButton classTypeName="Lecture 2" classTypeIcon="📚" />
                <ClassTypeButton classTypeName="Lecture 3" classTypeIcon="📚" />
                <ClassTypeButton classTypeName="Lecture 4" classTypeIcon="📚" />
                <ClassTypeButton classTypeName="Lecture 5" classTypeIcon="📚" />
                <ClassTypeButton classTypeName="Lecture 6" classTypeIcon="📚" />
                <ClassTypeButton classTypeName="Lecture 7" classTypeIcon="📚" />
                <ClassTypeButton classTypeName="Lecture 8" classTypeIcon="📚" />
                <ClassTypeButton classTypeName="Lecture 9" classTypeIcon="📚" />
                <ClassTypeButton classTypeName="Lecture 10" classTypeIcon="📚" />
                <ClassTypeButton classTypeName="Lecture 11" classTypeIcon="📚" />
                <ClassTypeButton classTypeName="Lecture 1" classTypeIcon="📚" />
                <ClassTypeButton classTypeName="Lecture 2" classTypeIcon="📚" />
                <ClassTypeButton classTypeName="Lecture 3" classTypeIcon="📚" />
                <ClassTypeButton classTypeName="Lecture 4" classTypeIcon="📚" />
                <ClassTypeButton classTypeName="Lecture 5" classTypeIcon="📚" />
                <ClassTypeButton classTypeName="Lecture 6" classTypeIcon="📚" />
                <ClassTypeButton classTypeName="Lecture 7" classTypeIcon="📚" />
                <ClassTypeButton classTypeName="Lecture 8" classTypeIcon="📚" />
                <ClassTypeButton classTypeName="Lecture 9" classTypeIcon="📚" />
                <ClassTypeButton classTypeName="Lecture 10" classTypeIcon="📚" />
                <ClassTypeButton classTypeName="Lecture 11" classTypeIcon="📚" />
                <ClassTypeButton classTypeName="Lecture 1" classTypeIcon="📚" />
                <ClassTypeButton classTypeName="Lecture 2" classTypeIcon="📚" />
                <ClassTypeButton classTypeName="Lecture 3" classTypeIcon="📚" />
                <ClassTypeButton classTypeName="Lecture 4" classTypeIcon="📚" />
                <ClassTypeButton classTypeName="Lecture 5" classTypeIcon="📚" />
                <ClassTypeButton classTypeName="Lecture 6" classTypeIcon="📚" />
                <ClassTypeButton classTypeName="Lecture 7" classTypeIcon="📚" />
                <ClassTypeButton classTypeName="Lecture 8" classTypeIcon="📚" />
                <ClassTypeButton classTypeName="Lecture 9" classTypeIcon="📚" />
                <ClassTypeButton classTypeName="Lecture 10" classTypeIcon="📚" />
                <ClassTypeButton classTypeName="Lecture 11" classTypeIcon="📚" />
            </div>

            {/* scroll right button */}
            {isOverflowing && <button className="px-5" onClick={() => handleScroll(scrollAmount)}>
                <FiArrowRightCircle className="text-2xl" />
            </button>}
            
            {/* Filters button */}
            <button 
                className="btn text-lg"
                onClick={()=>document.getElementById('filter_modal').showModal()}
            >
                <FiFilter className="text-2xl sm" />
            </button>

            {/* filter modal/popup */}
            <FilterSetting>
                hiii
            </ FilterSetting >
        </div>
    );
}