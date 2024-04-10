import React, { useRef, useState, useEffect } from "react";

import ClassTypeButton from "./ClassTypeButton";

import { FiArrowLeftCircle, FiArrowRightCircle, FiSliders } from "react-icons/fi";
import FilterSetting from "./FilterSetting";

import useFetch from "../../api/useFetch";

export default function ClassTypeList() {
    const [isOverflowing, setIsOverflowing] = useState(false);
    const scrollRef = useRef(null);
    const scrollAmount = 200;

    const API_URL = 'http://localhost:8080';
    const { data, loading, error, fetchData, reset } = useFetch();

    // class types from server
    const [classTypes, setClassTypes] = useState([]);

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

    useEffect(() => {
        // fetch class types from the server
        fetchData(`${API_URL}/classTypes`, {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json'
            }
        }, (data) => {
            // set class types
            setClassTypes(data.classTypes);
        });
    }, []);

    return (
        <div className="flex flex-row justify-center items-center mx-5 md:mx-20">
            {/* your ClassTypeButton components */}
            {true && <button className="pr-5" onClick={() => handleScroll(-scrollAmount)}>
                <FiArrowLeftCircle className="text-2xl" />
            </button>}

            {/* list of class types */}
            <div
                ref={scrollRef}
                className="flex flex-row grow overflow-x-hidden"
            >   
                <ClassTypeButton classTypeName={"Home"} classTypeIcon={"🏠"} />
                {/* class list icons here */}
                {classTypes.map((classType, index) => (
                    classType.isApproved && (
                        <>
                            <ClassTypeButton 
                                key={index}
                                classTypeName={classType.className}
                                classTypeIcon={classType.icon}
                            />
                        </>
                    )
                ))}
            </div>
            {true && <button className="px-5" onClick={() => handleScroll(scrollAmount)}>
                <FiArrowRightCircle className="text-2xl" />
            </button>}
            
            {/* Filters button */}
            <button 
                className="btn text-lg"
                onClick={()=>document.getElementById('filter_modal').showModal()}
            >
                <FiSliders className="text-2xl sm" />
            </button>
        </div>
    );
}