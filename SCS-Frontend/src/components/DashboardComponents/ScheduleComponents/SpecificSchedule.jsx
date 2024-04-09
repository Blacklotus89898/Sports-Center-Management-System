import React, { useState, useEffect, useRef } from "react";

import DashboardSearchComponent from "../DashboardSearchComponent";
import DashboardListComponent from "../DashboardListComponent";
import FilterSetting from "../../SearchComponents/FilterSetting";
import Modal from "../../Modal";

import AddUpdateInputFieldComponent from "../AddUpdateInputFieldComponent";

import useFetch from "../../../api/useFetch";

import { FiTrash2 } from "react-icons/fi";

export default function SpecificSchedule({year}) {
    const [currentFocus, setCurrentFocus] = useState(null);
    const [fetching, setFetching] = useState(false);
    const [search, setSearch] = useState("");
    const [openingHours, setOpeningHours] = useState([]);
    const [customHours, setCustomHours] = useState([]);

    // add custom hour states
    const [addName, setAddName] = useState("");
    const [addDescription, setAddDescription] = useState("");
    const [addDate, setAddDate] = useState("");
    const [addOpenTime, setAddOpenTime] = useState("");
    const [addCloseTime, setAddCloseTime] = useState("");

    // filter states
    const [startDateRange, setStartDateRange] = useState("");
    const [endDateRange, setEndDateRange] = useState("");
    const [showOpen, setShowOpen] = useState(true);
    const [showClosed, setShowClosed] = useState(true);

    const API_URL = 'http://localhost:8080';
    const { data, loading, error, fetchData, reset } = useFetch();

    // update opening hours
    const dayOfWeekRef = useRef({
        SUNDAY: React.createRef(),
        MONDAY: React.createRef(),
        TUESDAY: React.createRef(),
        WEDNESDAY: React.createRef(),
        THURSDAY: React.createRef(),
        FRIDAY: React.createRef(),
        SATURDAY: React.createRef()
    });

    async function updateOpeningHour(dayOfWeek, openTime, closeTime) {
        // http://localhost:8080/schedules/{year}/openingHours/{day}

        let newOpenTime = openTime;
        let newCloseTime = closeTime;
        if ((newOpenTime === undefined && newCloseTime === undefined) || (newOpenTime === "" && newCloseTime === "")) {
            newOpenTime = "00:00:00";
            newCloseTime = "00:00:00";
        }

        fetchData(`${API_URL}/schedules/${year}/openingHours/${dayOfWeek}`, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                dayOfWeek: dayOfWeek,
                openTime: newOpenTime,
                closeTime: newCloseTime,
                year: year
            })
        }, (data) => {
            setOpeningHours(openingHours.map(oh => oh.dayOfWeek === dayOfWeek ? data : oh));
        });
    }

    function DisplayOpeningHour({ dayOfWeek }) {
        const openingHour = openingHours.find(oh => oh.dayOfWeek === dayOfWeek);

        const [openTime, setOpenTime] = useState(openingHour?.openTime);
        const [closeTime, setCloseTime] = useState(openingHour?.closeTime);

        if (openTime === "00:00:00" && closeTime === "00:00:00") {
            setOpenTime("");
            setCloseTime("");
        }

        const isOpen = openingHour && openingHour.openTime !== "00:00:00" && openingHour.closeTime !== "00:00:00";

        return (
            <div className="w-full">
                {isOpen ? 
                <div className="flex flex-col">
                    <div className="text-sm font-semibold">{dayOfWeek}</div>
                    <AddUpdateInputFieldComponent id="add_open_time" title="Open Time" placeholder="" value={openTime} setValue={setOpenTime} type="time" disabled />

                    <div className="py-2" />

                    <AddUpdateInputFieldComponent id="add_close_time" title="Close Time" placeholder="" value={closeTime} setValue={setCloseTime} type="time" disabled />
                </div> :
                <div className="flex flex-col">
                    <div className="text-sm font-semibold">{dayOfWeek}</div>
                    <AddUpdateInputFieldComponent id="add_open_time" title="Open Time" placeholder="" value={openTime} setValue={setOpenTime} type="time" disabled />

                    <div className="py-2" />

                    <AddUpdateInputFieldComponent id="add_close_time" title="Close Time" placeholder="" value={closeTime} setValue={setCloseTime} type="time" disabled />
                </div>
            }
            <button 
                id={dayOfWeek + "_BUTTON"} 
                className="btn hidden"
                ref={dayOfWeekRef.current[dayOfWeek]}
                onClick={() => {
                    updateOpeningHour(dayOfWeek, openTime, closeTime);
                    setCurrentFocus("dayOfWeek_update" + dayOfWeek);
                }}
            >
            {/* error msg */}
            {(error && currentFocus === "dayOfWeek_update" + dayOfWeek && !data?.errors?.toString().includes("already exists")) && <div className='py-1 text-error text-center'>{data?.errors?.toString()}</div>}
                
            </button>
            </div>
        );
    }

    async function deleteCustomHour(customHour) {
        // http://localhost:8080/schedules/{year}/customHours/{name}
        fetchData(`${API_URL}/schedules/${year}/customHours/${customHour.name}`, {
            method: 'DELETE',
            headers: {
                'Content-Type': 'application/json'
            }
        }, () => {
            // remove from customHours
            setCustomHours(customHours.filter(ch => ch.name !== customHour.name));
        });
    }

    async function updateCustomHour(customHour) {

        let openTime = customHour.openTime;
        let closeTime = customHour.closeTime;
        if (openTime === "" && closeTime === "") {
            openTime = "00:00:00";
            closeTime = "00:00:00";
        }

        // update custom hour (http://localhost:8080/customHours/{name})
        fetchData(`${API_URL}/customHours/${customHour.name}`, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                description: customHour.description,
                date: customHour.date,
                openTime: openTime,
                closeTime: closeTime,
                schedule: {
                    year: year
                }
            })
        }, (data) => {
            if (data) {
                // update custom hours
                setCustomHours(customHours.map(ch => ch.name === customHour.name ? data : ch));
            }
        });
    }

    function FormatUpdateContent(customHour) {
        const [updateDescription, setUpdateDescription] = useState(customHour.description);
        const [updateDate, setUpdateDate] = useState(customHour.date);
        const [updateOpenTime, setUpdateOpenTime] = useState(customHour.openTime);
        const [updateCloseTime, setUpdateCloseTime] = useState(customHour.closeTime);
        
        if (updateOpenTime === "00:00:00" && updateCloseTime === "00:00:00") {
            setUpdateOpenTime("");
            setUpdateCloseTime("");
        }

        return (
            <>
                <AddUpdateInputFieldComponent id="add_description" title="Description" placeholder="" value={updateDescription} setValue={setUpdateDescription} type="text" textfield />

                <div className="py-2" />

                <AddUpdateInputFieldComponent id="add_date" title="Date" placeholder="" value={updateDate} setValue={setUpdateDate} type="date" />
                
                <div className="py-2" />

                <AddUpdateInputFieldComponent id="add_open_time" title="Open Time" placeholder="" value={updateOpenTime} setValue={setUpdateOpenTime} type="time" />
                
                <div className="py-2" />

                <AddUpdateInputFieldComponent id="add_close_time" title="Close Time" placeholder="" value={updateCloseTime} setValue={setUpdateCloseTime} type="time" />

                <div className="py-2" />

                {/* error message */}
                {(error && currentFocus === "") && <div className='py-1 text-error text-center'>{data?.errors?.toString()}</div>}

                {/* buttons */}
                <div className="flex flex-row w-full space-x-2">
                    <button 
                        className="btn btn-error text-lg"
                        onClick={() => {
                            deleteCustomHour(customHour);
                        }}
                    >
                        <FiTrash2 />
                    </button>
                    <div className="grow"/>
                    <button 
                        className="btn btn-primary"
                        onClick={() => {
                            updateCustomHour({
                                name: customHour.name,
                                description: updateDescription,
                                date: updateDate,
                                openTime: updateOpenTime,
                                closeTime: updateCloseTime
                            });
                            setCurrentFocus(customHour.name);
                        }}
                    >
                        Update
                    </button>
                </div>
            </>
        )
    }

    async function addCustomHour() {
        // add custom hour (http://localhost:8080/customHours)

        let openTime = addOpenTime;
        let closeTime = addCloseTime;
        if (openTime === "" && closeTime === "") {
            openTime = "00:00:00";
            closeTime = "00:00:00";
        }

        fetchData(`${API_URL}/customHours`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                name: addName,
                description: addDescription,
                date: addDate,
                openTime: openTime,
                closeTime: closeTime,
                schedule: {
                    year: year
                }
            })
        }, (data) => {
            if (data) {
                // add to customHours
                setCustomHours([...customHours, data]);

                // close modal
                document.getElementById('add_modal').close();

                // reset input fields
                setAddName("");
                setAddDescription("");
                setAddDate("");
                setAddOpenTime("");
                setAddCloseTime("");
            }
        });
    
    }

    function FormatAddContent() {
        return (
            <>
                <AddUpdateInputFieldComponent id="add_name" title="Name" placeholder="" value={addName} setValue={setAddName} type="text" />

                <div className="py-2" />

                <AddUpdateInputFieldComponent id="add_description" title="Description" placeholder="" value={addDescription} setValue={setAddDescription} type="text" textfield />

                <div className="py-2" />

                <AddUpdateInputFieldComponent id="add_date" title="Date" placeholder="" value={addDate} setValue={setAddDate} type="date" />
                
                <div className="py-2" />

                <AddUpdateInputFieldComponent id="add_open_time" title="Open Time" placeholder="" value={addOpenTime} setValue={setAddOpenTime} type="time" />
                
                <div className="py-2" />

                <AddUpdateInputFieldComponent id="add_close_time" title="Close Time" placeholder="" value={addCloseTime} setValue={setAddCloseTime} type="time" />

                <div className="py-2" />

                {/* error message */}
                {(error && currentFocus === "") && <div className='py-1 text-error text-center'>{data?.errors?.toString()}</div>}

                {/* buttons */}
                <div className="flex flex-row w-full space-x-2">
                    <button 
                        className="btn w-1/2"
                        onClick={() => {
                            document.getElementById('add_modal').close();
                            document.getElementById('add_modal').querySelectorAll('input').forEach(input => input.value = '');

                            setAddName("");
                            setAddDescription("");
                            setAddDate("");
                            setAddOpenTime("");
                            setAddCloseTime("");
                            reset();
                        }}
                    >
                        Cancel
                    </button>
                    <button 
                        className="btn w-1/2 btn-primary"
                        onClick={() => {addCustomHour(); setCurrentFocus("")}}
                    >
                        Create
                    </button>
                </div>
            </>
        )
    }

    function buildTitle(customHour) {
        return (
            <>
                <h1>[{customHour.date}]: {customHour.name}</h1>
            </>
        )
    }

    function FilterContent() {
        return (
            <div className="pt-5 space-y-2">
                <div className="flex flex-row w-full items-center">
                    <div className="text-sm">Show custom hours in range</div>
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
                    <div className="text-sm">Show gym open custom hours</div>
                    <div className="grow" />
                    <input 
                        id="addApproved"
                        type="checkbox" 
                        className="checkbox"
                        checked={showOpen}
                        onChange={(e) => setShowOpen(e.target.checked)}
                    />
                </div>
                <div className="flex flex-row w-full">
                    <div className="text-sm">Show gym closed custom hours</div>
                    <div className="grow" />
                    <input 
                        id="addApproved"
                        type="checkbox" 
                        className="checkbox"
                        checked={showClosed}
                        onChange={(e) => setShowClosed(e.target.checked)}
                    />
                </div>
            </div>
        );
    }

    function filter(customHour) {
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
        const classDate = new Date(customHour.date);
        const today = new Date();

        const withinDateRange = fromInf && toInf || fromInf && classDate <= endDate || toInf && classDate >= startDate || classDate >= startDate && classDate <= endDate;

        const closed = customHour.openTime === "00:00:00" && customHour.closeTime === "00:00:00";

        return withinDateRange && (showOpen && !closed || showClosed && closed);
    }

    useEffect(() => {
        // create opening hour (http://localhost:8080/schedules/{year}/openingHours)
        ['SUNDAY', 'MONDAY', 'TUESDAY', 'WEDNESDAY', 'THURSDAY', 'FRIDAY', 'SATURDAY'].forEach(dayOfWeek => {
            const newOpenTime = '00:00:00'; // replace with your desired open time
            const newCloseTime = '10:00:00'; // replace with your desired close time

            fetchData(`${API_URL}/schedules/${year}/openingHours`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({
                    dayOfWeek: dayOfWeek,
                    openTime: newOpenTime,
                    closeTime: newCloseTime,
                    year: year
                })
            }, (data) => {
                // update opening hours
                setOpeningHours(openingHours.map(oh => oh.dayOfWeek === dayOfWeek ? data : oh));
            });
        });

        // get all opening hours (http://localhost:8080/schedules/{year}/openingHours)
        fetchData(`${API_URL}/schedules/${year}/openingHours`, {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json'
            }
        }, (data) => {
            setOpeningHours(data.sort((a, b) => {
                const daysOfWeek = ['SUNDAY', 'MONDAY', 'TUESDAY', 'WEDNESDAY', 'THURSDAY', 'FRIDAY', 'SATURDAY'];
                return daysOfWeek.indexOf(a.dayOfWeek) - daysOfWeek.indexOf(b.dayOfWeek);
            }));
        });

        // get all custom hours (http://localhost:8080/schedules/{year}/customHours)
        fetchData(`${API_URL}/schedules/${year}/customHours`, {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json'
            }
        }, (data) => {
            // only add if schedule.year === year
            setCustomHours(data.customHours.filter(customHour => Number(customHour.schedule.year) === Number(year)));
        });
    }, []);

    return (
        <>
            <div className="text-lg font-semibold">
                Operating Hours
            </div>

            {/* edit weekly hours */}
            <div className="grid md:grid-cols-2 lg:grid-cols-4 xl:grid-cols-7 gap-3">
                {openingHours.map(oh => <DisplayOpeningHour key={oh.dayOfWeek} dayOfWeek={oh.dayOfWeek} />)}
            </div>

            <div className="py-2"></div>

            <div className="py-2" />

            {/* update weekly hours button */}
            <div className="flex flex-row w-full justify-end">
                <button 
                    className="btn btn-primary"
                    onClick={() => {
                        // click all buttons
                        Object.values(dayOfWeekRef.current).forEach(ref => {
                            ref.current.click();
                        });
                    }}

                >
                    Save
                </button>
            </div>

            {/* line */}
            <hr className="my-5" />

            {/* editing custom hours */}
            <div className="text-lg font-semibold">
                Custom Hours
            </div>

            {/* search & filter */}
            <DashboardSearchComponent setSearch={setSearch} contents={customHours} />

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
            <DashboardListComponent title={buildTitle} contents={customHours} search={search} filter={filter} format={FormatUpdateContent} />

            {/* add item modal */}
            <Modal id="add_modal">
                {FormatAddContent()}
            </Modal>
        </>
    );
}