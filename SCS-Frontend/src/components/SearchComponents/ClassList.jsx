import React, { useEffect, useState } from "react";
import { useLocation } from "react-router-dom";

import ClassListItem from "./ClassListItem";

import useFetch from "../../api/useFetch";
import FilterSetting from "./FilterSetting";

export default function ClassList({ search }) {
    const [fetching, setFetching] = useState(true);
    const [schedules, setSchedules] = useState([]);
    const [classes, setClasses] = useState([]);
    const [instructors, setInstructors] = useState({});
    const [availableInstructors, setAvailableInstructors] = useState([]);

    const API_URL = 'http://localhost:8080';
    const { data, loading, error, fetchData, reset } = useFetch();

    // filter states
    const [startDateRange, setStartDateRange] = useState("");
    const [endDateRange, setEndDateRange] = useState("");
    const [isEnded, setIsEnded] = useState(false);
    const [isFull, setIsFull] = useState(true);
    const [isNotFull, setIsNotFull] = useState(true);
    const [selectedInstructor, setSelectedInstructor] = useState("All Instructors");

    // get classType from URL (filter for classType)
    const location = useLocation();
    const classTypeParam = new URLSearchParams(location.search).get("classType");
    const filterClassType = classTypeParam ? classTypeParam : "";

    function getStatus(classItem) {
        if (classItem.currentCapacity >= classItem.maxCapacity) {
            return 'full';
        } else if (new Date(classItem.date) < new Date()) {
            return 'ended';
        } else {
            return `${classItem.currentCapacity}/${classItem.maxCapacity} capacity`;
        }
    }
    
    function getClassInstructor(classItem) {
        const url = `${API_URL}/specificClass/${classItem.classId}/teachingInfo`;
        
        fetch(url, {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json'
            }
        })
        .then(response => response.json())
        .then(data => {
            if (data && data.instructor) {
                // save to instructors with {'classId': 'instructorName'}
                setInstructors(prevInstructors => {
                    return {...prevInstructors, [classItem.classId]: data.instructor.name};
                });
            } else {
                setInstructors(prevInstructors => {
                    return {...prevInstructors, [classItem.classId]: "Instructor TBD"};
                });
            }
        });
    }

    function filter({ classItem, status, time, date, instructor, classTypeDate }) {        
        // check date within range
        let startDate = null;
        let endDate = null;

        if (startDateRange && !isNaN(Date.parse(startDateRange))) {
            startDate = new Date(startDateRange);
        }

        if (endDateRange && !isNaN(Date.parse(endDateRange))) {
            endDate = new Date(endDateRange);
        }

        const fromInf = !startDateRange;
        const toInf = !endDateRange;
        const classDate = new Date(classTypeDate);
        const today = new Date();

        const withinDateRange = fromInf && toInf || fromInf && classDate <= endDate || toInf && classDate >= startDate || classDate >= startDate && classDate <= endDate;

        // check if classType is valid
        const matchFilter = !filterClassType
            || (filterClassType !== "" && classItem.classType.className === filterClassType);
        
        // create a full stringify of a nested object
        const image = classItem.image
        classItem.image = ""
        const classItemString = JSON.stringify(classItem).toLowerCase();
        classItem.image = image

        const matchSearch = (classItemString + JSON.stringify(status + time + date + instructor).toLowerCase()).includes(search.toLowerCase());

        const classEnded = classDate < today;
        const classFull = classItem.currentCapacity >= classItem.maxCapacity;
        const classNotFull = classItem.currentCapacity < classItem.maxCapacity && classDate >= today;   // class ongoing

        const matchInstructor = selectedInstructor === "All Instructors" || instructor === selectedInstructor;

        return ((isEnded && classEnded) || (isFull && classFull) || (isNotFull && classNotFull)) && matchSearch && matchFilter && withinDateRange && matchInstructor;
    }

    function FilterContent() {
        return (
            <div className="pt-5 space-y-2">
                <div className="flex flex-row w-full">
                    <div className="text-sm">Instructor</div>
                    <div className="grow" />
                    <select 
                        id="addApproved"
                        className="select select-bordered"
                        value={selectedInstructor}
                        onChange={(e) => setSelectedInstructor(e.target.value)}
                    >
                        <option value="All Instructors">All Instructors</option>
                        {availableInstructors.map((instructor, index) => (
                            <option key={index} value={instructor.name}>{instructor.name}</option>
                        ))}
                    </select>
                </div>
                <div className="flex flex-row w-full items-center">
                    <div className="text-sm">Show classes in range</div>
                    <div className="grow" />
                    
                    {/* date range select */}
                    <div className="flex flex-col justify-center items-center content-center">
                        <input 
                            id="addApproved"
                            type="date" 
                            className="input"
                            value={startDateRange}
                            onChange={(e) => setStartDateRange(e.target.value)}
                        />
                        <div className="">to</div>
                        <input 
                            id="addApproved"
                            type="date" 
                            className="input"
                            value={endDateRange}
                            onChange={(e) => setEndDateRange(e.target.value)}
                        />
                    </div>
                </div>
                <div className="flex flex-row w-full">
                    <div className="text-sm">Show ongoing classes.</div>
                    <div className="grow" />
                    <input 
                        id="addApproved"
                        type="checkbox" 
                        className="checkbox"
                        checked={isNotFull}
                        onChange={(e) => setIsNotFull(e.target.checked)}
                    />
                </div>
                <div className="flex flex-row w-full">
                    <div className="text-sm">Show full classes.</div>
                    <div className="grow" />
                    <input 
                        id="addApproved"
                        type="checkbox" 
                        className="checkbox"
                        checked={isFull}
                        onChange={(e) => setIsFull(e.target.checked)}
                    />
                </div>
                <div className="flex flex-row w-full">
                    <div className="text-sm">Show ended classes.</div>
                    <div className="grow" />
                    <input 
                        id="addApproved"
                        type="checkbox" 
                        className="checkbox"
                        checked={isEnded}
                        onChange={(e) => setIsEnded(e.target.checked)}
                    />
                </div>
            </div>
        );
    }

    useEffect(() => {
        // fetch class types from the server
        setFetching(true);
        fetchData(`${API_URL}/schedules`, {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json'
            }
        }, (data) => {
            setSchedules(data.schedules);
            // loop through schedules and get classes by year
            data.schedules.forEach(schedule => {
                fetchData(`${API_URL}/specificClass/year/${schedule.year}`, {
                    method: 'GET',
                    headers: {
                        'Content-Type': 'application/json'
                    }
                }, (data) => {
                    setClasses(prevClasses => {
                        const newClasses = data.specificClasses.filter(newClass => !prevClasses.some(prevClass => prevClass.classId === newClass.classId));
                        return [...prevClasses, ...newClasses];
                    });
                    setFetching(false);
                });
            }); 
        });

        // get all the instructors
        fetchData(`${API_URL}/instructors`, {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json'
            }
        }, (data) => {
            if (data && data.instructors) {
                setAvailableInstructors(data.instructors);
            }
        });
    }, []);

    return (
        <>
            {/* filter modal/popup */}
            <FilterSetting>
                {FilterContent()}
            </ FilterSetting >

            {/* fetching loading */}
            {fetching &&
                <div className="flex w-full justify-center content-center">
                    <span className="loading loading-ring loading-lg" />
                </div>
            }
            <div className="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 lg:grid-cols-4 xl:grid-cols-5 gap-4 mx-5 md:mx-20 justify-center">
                {
                    classes.map((classItem) => {
                        let status = getStatus(classItem);
                        
                        // Convert date and time
                        let classTypeDate = classItem.date;
                        let dateTimeStr = `${classTypeDate}T${classItem.startTime}`;
                        
                        let date = new Date(dateTimeStr);
                        let days = ["Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"];
                        
                        let dayOfWeek = days[date.getDay()];
                        let dateOfMonth = date.getDate();
                        let month = date.getMonth();
                        let months = ["Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"];
                        month = months[month];

                        let year = date.getFullYear();
                        
                        let hours = date.getHours();
                        let minutes = date.getMinutes();

                        let period = hours >= 12 ? 'PM' : 'AM';
                        hours = hours % 12;
                        hours = hours ? hours : 12;
                        hours = hours < 10 ? '0' + hours : hours;
                        minutes = minutes < 10 ? '0' + minutes : minutes;

                        let time = `${hours}:${minutes} ${period}`;
                        let dateTime = `${month} ${dateOfMonth}, ${year}`

                        // if classItem.classId not in instrcutors as key, fetch instructor
                        if (!instructors[classItem.classId]) {
                            getClassInstructor(classItem);
                        }

                        let instructor = instructors[classItem.classId];

                        return (
                            <>
                                {filter({classItem, status, time, date, instructor, classTypeDate }) && ClassListItem({
                                    imageSrc: classItem.image,
                                    status: status,
                                    name: classItem.specificClassName,
                                    description: classItem.description,
                                    date: dateTime,
                                    time: time,
                                    lengthInHrs: classItem.hourDuration,
                                    instructor: instructor
                                })}
                            </>
                        );
                    })
                }
            </div>
        </>
    );
}