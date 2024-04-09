import React, { useEffect, useState } from "react";

import ClassListItem from "./ClassListItem";

import useFetch from "../../api/useFetch";

export default function ClassList() {
    const [fetching, setFetching] = useState(true);
    const [schedules, setSchedules] = useState([]);
    const [classes, setClasses] = useState([]);
    const [instructors, setInstructors] = useState({});

    const API_URL = 'http://localhost:8080';
    const { data, loading, error, fetchData, reset } = useFetch();

    function getStatus(classItem) {
        if (classItem.currentCapacity >= classItem.maxCapacity) {
            return 'full';
        } else if (classItem.date < new Date()) {
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
    }, []);

    return (
        <>
            {/* fetching loading */}
            {fetching &&
                <div className="flex w-full justify-center content-center">
                    <span className="loading loading-ring loading-lg" />
                </div>
            }
            <div className="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 lg:grid-cols-4 xl:grid-cols-5 gap-4 mx-5 md:mx-20 justify-center">
                {
                    classes.map((classItem) => {
                        // Convert date and time
                        let dateTimeStr = `${classItem.date}T${classItem.startTime}`;
                        
                        let date = new Date(dateTimeStr);
                        let days = ["Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"];
                        
                        let dayOfWeek = days[date.getDay()];
                        let dateOfMonth = date.getDate();
                        
                        let hours = date.getHours();
                        let minutes = date.getMinutes();

                        let period = hours >= 12 ? 'PM' : 'AM';
                        hours = hours % 12;
                        hours = hours ? hours : 12;
                        hours = hours < 10 ? '0' + hours : hours;
                        minutes = minutes < 10 ? '0' + minutes : minutes;

                        let time = `${hours}:${minutes} ${period}`;

                        // if classItem.classId not in instrcutors as key, fetch instructor
                        if (!instructors[classItem.classId]) {
                            getClassInstructor(classItem);
                        }

                        return (
                            <>
                                {ClassListItem({
                                    imageSrc: classItem.image,
                                    status: getStatus(classItem),
                                    name: classItem.className,
                                    description: classItem.description,
                                    date: `${dayOfWeek}, ${dateOfMonth}`,
                                    time: time,
                                    lengthInHrs: classItem.lengthInHrs,
                                    instructor: instructors[classItem.classId]
                                })}
                            </>
                        );
                    })
                }
            </div>
        </>
    );
}