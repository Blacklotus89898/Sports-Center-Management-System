import React, { useState, useEffect } from "react";

import useFetch from "../../../api/useFetch";

import DashboardSearchComponent from "../DashboardSearchComponent";
import DashboardListComponent from "../DashboardListComponent";
import FilterSetting from "../../SearchComponents/FilterSetting";
import Modal from "../../Modal";
import ClassListItem from "../../SearchComponents/ClassListItem";

export default function CustomerClasses() {
    const [currentFocus, setCurrentFocus] = useState(null);
    const [fetching, setFetching] = useState(false);
    const [search, setSearch] = useState("");
    const [classes, setClasses] = useState([]);
    const [instructors, setInstructors] = useState([]);

    // filter classes
    const [startDateRange, setStartDateRange] = useState("");
    const [endDateRange, setEndDateRange] = useState("");
    const [isEnded, setIsEnded] = useState(true);
    const [isFull, setIsFull] = useState(true);
    const [isNotFull, setIsNotFull] = useState(true);
    const [searchInstructor, setSearchInstructor] = useState("");

    const API_URL = 'http://localhost:8080';
    const { data, loading, error, fetchData, reset } = useFetch();

    function FormatUpdateContent(klass) {
        // set current capacity
        let status = "";
        if (klass.currentCapacity >= klass.maxCapacity) {
            status = "full";
        } else if (new Date(klass.date) < new Date()) {
            status = "ended";
        } else {
            status = `${klass.currentCapacity}/${klass.maxCapacity} capacity`;
        }

        // Convert date and time
        let classTypeDate = klass.date;
        let dateTimeStr = `${classTypeDate}T${klass.startTime}`;
        
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

        return (
            <>
                <ClassListItem
                    id={klass.classId}
                    imageSrc={klass.image}
                    status={status}
                    name={klass.specificClassName}
                    description={klass.description}
                    date={dateTime}
                    time={time}
                    lengthInHrs={klass.hourDuration}
                    instructor={klass.teachingInfo.instructor.name}
                    registrationFee={klass.registrationFee}
                />
            </>
        )
    }

    function FilterContent() {
        return (
            <div className="pt-5 space-y-2">
                <div className="flex flex-row w-full">
                    <div className="text-sm">Filter by instructor</div>
                    <div className="grow" />
                    <select 
                        className="select w-full"
                        value={searchInstructor}
                        onChange={(e) => setSearchInstructor(e.target.value)}
                    >
                        <option value="">Select an instructor</option>
                        {instructors.map(instructor => {
                            return (
                                <option key={instructor.id} value={instructor.name}>{instructor.name}</option>
                            );
                        }
                        )}
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
            </div>
        );
    }

    function filter(klass) {
        let klassString = JSON.stringify(klass);

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
        const classDate = new Date(klass.klass.date);
        const today = new Date();

        const withinDateRange = fromInf && toInf || fromInf && classDate <= endDate || toInf && classDate >= startDate || classDate >= startDate && classDate <= endDate;
        // toInf || (startDateRange && endDateRange && classDate >= startDate && classDate <= endDate);
        const classEnded = classDate < today;
        const classFull = klass.klass.currentCapacity >= klass.klass.maxCapacity;
        const classNotFull = klass.klass.currentCapacity < klass.klass.maxCapacity && classDate >= today;

        const instructorFilter = searchInstructor === "" || klass.klass.teachingInfo.instructor.name === searchInstructor;

        const searchValid = search.length === 0 || klassString.toLowerCase().includes(search.toLowerCase());

        return searchValid && instructorFilter && withinDateRange && ((isEnded && classEnded) || (isFull && classFull) || (isNotFull && classNotFull))
    }   

    useEffect(() => {
        // fetch all instructors
        // get all instructors
        fetchData(`${API_URL}/instructors`, {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json'
            }
        }, (data) => {
            if (data) {
                setInstructors(data.instructors);
            }
        });

        // fetch all schedules, then fetch all classes, the fetch all class registrations for each class

        let user = JSON.parse(localStorage.getItem("currentUser"));

        // fetch all schedules (http://localhost:8080/schedules)
        setFetching(true);
        fetchData(`${API_URL}/schedules`, {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json'
            }
        }, (data) => {
            console.log("1", data);
            // for each data.schedule (http://localhost:8080/specificClass/year/{year})
            data.schedules.forEach(schedule => {
                fetchData(`${API_URL}/specificClass/year/${schedule.year}`, {
                    method: 'GET',
                    headers: {
                        'Content-Type': 'application/json'
                    }
                }, (data) => {
                    console.log("2", data);
                    // for each class in data.specificClasses, get the class registrations (http://localhost:8080/specificClass/{classId}/classRegistration)
                    data.specificClasses.forEach(klass => {
                        fetchData(`${API_URL}/specificClass/${klass.classId}/classRegistration`, {
                            method: 'GET',
                            headers: {
                                'Content-Type': 'application/json'
                            }
                        }, (data) => {
                            console.log("3", data);
                            // add class if user is in the class and class is not already in classes
                            data.classRegistrations.forEach(registration => {
                                if (registration.customer.id === user.id) {
                                    console.log("registration", registration);
                                    // check classId is not in the classes array
                                    if (!classes.some(c => c.classId === klass.classId)) {
                                        klass.classRegistration = registration;

                                        // get teaching info for the class, must exist (http://localhost:8080/specificClass/{classId}/teachingInfo)
                                        fetchData(`${API_URL}/specificClass/${klass.classId}/teachingInfo`, {
                                            method: 'GET',
                                            headers: {
                                                'Content-Type': 'application/json'
                                            }
                                        }, (data) => {
                                            klass.teachingInfo = data;
                                            setClasses(currentClasses => {
                                                // Check if the class is already included to prevent duplicates
                                                const isClassAlreadyIncluded = currentClasses.some(c => c.classId === klass.classId);
                                                if (!isClassAlreadyIncluded) {
                                                    return [...currentClasses, klass]; // Return a new array with the new class
                                                }
                                                return currentClasses; // Return the existing state if class is already included
                                            });
                                        });
                                    }
                                }
                            });
                        });
                    });
                });
            });
            setFetching(false);
        });
    
    }, []);

    console.log("4", classes)

    return (
        <>
            {/* search & filter */}
            <DashboardSearchComponent setSearch={setSearch} contents={classes} showAdd={false} />

            {/* filter modal/popup */}
            <FilterSetting>
                {FilterContent()}
            </FilterSetting>

            {/* line */}
            <hr className="my-5" />

            {/* fetching loading */}
            {fetching &&
                <div className="flex w-full justify-center content-center">
                    <span className="loading loading-ring loading-lg" />
                </div>
            }

            {/* list of ___ */}
            <div className="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 lg:grid-cols-4 xl:grid-cols-5 gap-4 justify-center">
                {
                    classes.map((klass) => {
                        return (
                            <>
                                {filter({klass}) && FormatUpdateContent(klass)}
                            </>
                        )
                    })
                }
            </div>
        </>
    );
}
