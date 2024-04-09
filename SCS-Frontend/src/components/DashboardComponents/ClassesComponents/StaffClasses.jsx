import React, { useState, useEffect } from "react";

import useFetch from "../../../api/useFetch";

import DashboardSearchComponent from "../DashboardSearchComponent";
import DashboardListComponent from "../DashboardListComponent";
import FilterSetting from "../../SearchComponents/FilterSetting";
import Modal from "../../Modal";
import AddUpdateInputFieldComponent from "../AddUpdateInputFieldComponent";

import { FiMinus, FiTrash2 } from "react-icons/fi";
import { getUserRole } from "../../../utils/auth";

import { useAtom } from "jotai";
import { currentUserAtom } from "../../../utils/jotai";

export default function StaffClasses() {
    const [fetching, setFetching] = useState(true);
    const [search, setSearch] = useState("");
    const [classes, setClasses] = useState([]);
    const [schedules, setSchedules] = useState([]);
    const [classTypes, setClassTypes] = useState([]);

    // add class states
    const [addImage, setAddImage] = useState("");
    const [addClassName, setAddClassName] = useState("");
    const [addClassType, setAddClassType] = useState(classTypes.length > 0 ? classTypes[0].className : "");
    const [addClassDescription, setAddClassDescription] = useState("");
    const [addClassRegistrationFee, setAddClassRegistrationFee] = useState("");
    const [addClassCurrentCapacity, ] = useState(0);
    const [addClassMaxCapacity, setAddClassMaxCapacity] = useState("");
    const [addClassStartTime, setAddClassStartTime] = useState("");
    const [addClassHours, setAddClassHours] = useState("");
    const [addClassDate, setAddClassDate] = useState("");

    // upate class states
    const [currentFocus, setCurrentFocus] = useState("");
    const [instructors, setInstructors] = useState([]);

    // filter states
    const [startDateRange, setStartDateRange] = useState("");
    const [endDateRange, setEndDateRange] = useState("");
    const [isEnded, setIsEnded] = useState(true);
    const [isFull, setIsFull] = useState(true);
    const [isNotFull, setIsNotFull] = useState(true);

    // current user id
    const [currentUser, ] = useAtom(currentUserAtom);

    const API_URL = 'http://localhost:8080';
    const { data, loading, error, fetchData, reset } = useFetch();

    async function deleteClass({ classId }) {
        fetchData(`${API_URL}/specificClass/${classId}`, {
            method: 'DELETE',
            headers: {
                'Content-Type': 'application/json'
            }
        }, () => {
            setClasses(prevClasses => prevClasses.filter(klass => klass.classId !== classId));
        });
    }

    async function deleteTeachingInfo({ teachingInfoId, setUpdateInstructor }) {
        fetchData(`${API_URL}/teachingInfo/${teachingInfoId}`, {
            method: 'DELETE',
            headers: {
                'Content-Type': 'application/json'
            }
        }, () => {
            console.log("Teaching info deleted");
            setUpdateInstructor(null);
        });
    }

    async function createTeachingInfo({ createTeachingInfo, setUpdateInstructor }) {
        fetchData(`${API_URL}/teachingInfo`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                classId: createTeachingInfo.classId,
                accountId: createTeachingInfo.accountId
            })
        }, (data) => {
            if (data) {
                console.log(data);

                // update the class
                setUpdateInstructor(data.instructor);
            }
        });
    }

    async function updateExistingTeachingInfo({ updateTeachingInfoInfo, setUpdateInstructor }) {
        fetchData(`${API_URL}/teachingInfo/${updateTeachingInfoInfo.teachingInfoId}`, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                classId: updateTeachingInfoInfo.classId,
                accountId: updateTeachingInfoInfo.accountId
            })
        }, (data) => {
            if (data) {
                console.log(data);
                setUpdateInstructor(data.instructor);
            }
        });
    }


    async function updateClass({ updateClass }) {
        fetchData(`${API_URL}/specificClass/${updateClass.classId}`, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                classType: updateClass.classType,
                currentCapacity: updateClass.currentCapacity,
                date: updateClass.date,
                description: updateClass.description,
                hourDuration: updateClass.hourDuration,
                image: updateClass.image,
                maxCapacity: updateClass.maxCapacity,
                registrationFee: updateClass.registrationFee,
                specificClassName: updateClass.specificClassName,
                startTime: updateClass.startTime,
                year: updateClass.year
            })
        }, (data) => {
            if (data) {
                setClasses(prevClasses => {
                    const index = prevClasses.findIndex(klass => klass.classId === data.classId);
                    const newClasses = [...prevClasses];
                    newClasses[index] = data;
                    return newClasses;
                });
            }
        });
    }

    function FormatUpdateContent(displayClass) {
        const [updateImage, setUpdateImage] = useState(displayClass.image);
        const [updateClassName, setUpdateClassName] = useState(displayClass.specificClassName);
        const [updateClassType, setUpdateClassType] = useState(displayClass.classType.className);
        const [updateClassDescription, setUpdateClassDescription] = useState(displayClass.description);
        const [updateClassRegistrationFee, setUpdateClassRegistrationFee] = useState(displayClass.registrationFee);
        const [updateClassMaxCapacity, setUpdateClassMaxCapacity] = useState(displayClass.maxCapacity);
        const [updateClassStartTime, setUpdateClassStartTime] = useState(displayClass.startTime);
        const [updateClassHours, setUpdateClassHours] = useState(displayClass.hourDuration);
        const [updateClassDate, setUpdateClassDate] = useState(displayClass.date);
        const [updateInstructor, setUpdateInstructor] = useState(null);
        const [updateTeachingInfo, setUpdateTeachingInfo] = useState(null);
        
        const [fetchedTeachingInfo, setFetchedTeachingInfo] = useState(false);

        // get teaching info
        if (!fetchedTeachingInfo) {
            fetchData(`${API_URL}/specificClass/${displayClass.classId}/teachingInfo`, {
                method: 'GET',
                headers: {
                    'Content-Type': 'application/json'
                }
            }, (data) => {
                if (data) {
                    console.log(data);
                    setUpdateTeachingInfo(data.teachingInfoId);
                    setUpdateInstructor(data.instructor);
                }
            });
            setFetchedTeachingInfo(true);
        }

        const emojiMap = {};
        classTypes.forEach(classType => {
            emojiMap[classType.className] = classType.icon + " " + classType.className;
        });

        return (
            <div>
                {getUserRole() === "INSTRUCTOR" ? 
                    <div className="text-sm pb-1">Assigned instructor: {updateInstructor ? updateInstructor.name : "none"}</div>
                    : 
                    <>
                        <div className="text-sm pb-1">Instructor: </div>
                        <select 
                            className="select w-full"
                            value={updateInstructor?.id}
                            onChange={(e) => setUpdateInstructor(instructors.find(instructor => instructor.id === parseInt(e.target.value)))}
                        >
                            <option value="">Select an instructor</option>
                            {instructors.map(instructor => {
                                return (
                                    <option key={instructor.id} value={instructor.id}>{instructor.name}</option>
                                );
                            })}
                        </select>
                    </>
                }
                
                <div className="py-2" />

                {/* // image */}
                <div className="text-sm pb-1">Display image</div>
                <div className="flex flex-col md:flex-row justify-center items-center">
                {updateImage && (
                    <div className="relative flex justify-center items-center w-2/12 aspect-[9/8]">
                        <img
                            loading="lazy"
                            src={`data:image/jpeg;base64,${updateImage}`}
                            alt="Uploaded Image"
                            className="absolute inset-0 object-cover aspect-[9/8] rounded-lg"
                        />
                        <div className="absolute top-0 right-0">
                            <button 
                                className="aspect-square bg-error text-base-200 rounded-full m-1"
                                onClick={() => setUpdateImage("")}
                            >
                                <FiMinus />
                            </button>
                        </div>
                    </div>
                )}

                    <div className="py-2 md:px-2" />

                    <div className="flex w-8/12 justify-end items-center">
                        <input
                            type="file"
                            className="file-input"
                            accept="image/*"
                            onChange={(e) => {
                                const file = e.target.files[0];
                                const reader = new FileReader();
                                reader.onloadend = () => {
                                    setUpdateImage(reader.result.split(',')[1]);
                                };
                                reader.readAsDataURL(file);
                            }}
                        />
                    </div>
                </div>

                <div className="py-2" />

                {/* class type */}
                <div className="text-sm pb-1">Choose class type</div>
                <select
                    id="class_type"
                    className="select select-bordered w-full"
                    value={emojiMap[updateClassType]}
                    onChange={(e) => setUpdateClassType(e.target.value.replace(/^.+?\s/, ''))}
                >
                    <option value="">Select a class type</option>
                    {classTypes.map(classType => {
                        if (!classType.isApproved) {
                            return;
                        }

                        return (
                            <option 
                                key={classType.id} 
                                value={classType.id} 
                            >
                                {classType.icon} {classType.className}
                            </option>
                        );
                    })}
                </select>

                <div className="py-2" />

                {/* // specific class name */}
                <AddUpdateInputFieldComponent title="Class Name" placeholder="" value={updateClassName} setValue={setUpdateClassName} type="text" />

                <div className="py-2" />

                {/* // description */}
                <AddUpdateInputFieldComponent title="Description" placeholder="" value={updateClassDescription} setValue={setUpdateClassDescription} type="text" textfield={true} />

                <div className="py-2" />

                {/* // registriation fee */}
                <AddUpdateInputFieldComponent title="Registration Fee" placeholder="" value={updateClassRegistrationFee} setValue={setUpdateClassRegistrationFee} type="number" />

                <div className="py-2" />

                {/* // max capacity */}
                <AddUpdateInputFieldComponent title="Max Capacity" placeholder="" value={updateClassMaxCapacity} setValue={setUpdateClassMaxCapacity} type="number" />

                <div className="py-2" />

                {/* // date */}
                <AddUpdateInputFieldComponent title="Date" placeholder="" value={updateClassDate} setValue={setUpdateClassDate} type="date" />

                <div className="py-2" />

                {/* // start time */}
                <AddUpdateInputFieldComponent title="Start Time" placeholder="" value={updateClassStartTime} setValue={setUpdateClassStartTime} type="time" />

                <div className="py-2" />

                {/* // hours */}
                <AddUpdateInputFieldComponent title="Hours" placeholder="" value={updateClassHours} setValue={setUpdateClassHours} type="number" step="0.01" />

                <div className="py-2" />

                {/* error message */}
                {(error && currentFocus === displayClass.classId) && <div className='py-1 text-error text-center'>{data?.errors?.toString()}</div>}

                {/* buttons */}
                <div className="flex flex-row w-full space-x-2">
                    <button 
                        className="btn btn-error text-lg"
                        onClick={() => {
                            deleteClass({ classId: displayClass.classId });
                        }}
                    >
                        <FiTrash2 />
                    </button>
                    <div className="grow"/>
                    {(getUserRole() === "INSTRUCTOR" && currentUser.id !== updateInstructor?.id) && <button 
                        className="btn btn-info"
                        onClick={() => {
                            if (updateInstructor) { 
                                // instructor exists, update teaching info
                                updateExistingTeachingInfo({
                                    updateTeachingInfoInfo: {
                                        teachingInfoId: updateTeachingInfo,
                                        classId: displayClass.classId,
                                        accountId: currentUser.id
                                    },
                                    setUpdateInstructor: setUpdateInstructor
                                });
                            } else {
                                // instructor does not exist, create teaching info
                                createTeachingInfo({
                                    createTeachingInfo: {
                                        classId: displayClass.classId,
                                        accountId: currentUser.id
                                    },
                                    setUpdateInstructor: setUpdateInstructor
                                });
                            }

                            setCurrentFocus(displayClass.classId)
                        }}
                    >
                        Teach
                    </button>}
                    {(getUserRole() === "INSTRUCTOR" && currentUser.id === updateInstructor?.id) && <button 
                        className="btn btn-info"
                        onClick={() => {
                            // delete teaching info
                            deleteTeachingInfo({ 
                                teachingInfoId: updateTeachingInfo,
                                setUpdateInstructor: setUpdateInstructor
                            });
                            setCurrentFocus(displayClass.classId)
                        }}
                    >
                        Withdraw
                    </button>}
                    <button 
                        className="btn btn-primary"
                        onClick={() => {
                            updateClass({
                                updateClass: {
                                    classId: displayClass.classId,
                                    classType: updateClassType,
                                    currentCapacity: displayClass.currentCapacity,
                                    date: updateClassDate,
                                    description: updateClassDescription,
                                    hourDuration: updateClassHours,
                                    image: updateImage,
                                    maxCapacity: updateClassMaxCapacity,
                                    registrationFee: updateClassRegistrationFee,
                                    specificClassName: updateClassName,
                                    startTime: updateClassStartTime,
                                    year: parseInt(updateClassDate.substring(0, 4))
                                }
                            });

                            // create or update teaching info
                            if (updateInstructor) {
                                if (updateTeachingInfo) {
                                    updateExistingTeachingInfo({
                                        updateTeachingInfoInfo: {
                                            teachingInfoId: updateTeachingInfo,
                                            classId: displayClass.classId,
                                            accountId: updateInstructor.id
                                        },
                                        setUpdateInstructor: setUpdateInstructor
                                    });
                                } else {
                                    createTeachingInfo({
                                        createTeachingInfo: {
                                            classId: displayClass.classId,
                                            accountId: updateInstructor.id
                                        },
                                        setUpdateInstructor: setUpdateInstructor
                                    });
                                }
                            } else {
                                // delete if exists
                                if (updateTeachingInfo) {
                                    deleteTeachingInfo({ 
                                        teachingInfoId: updateTeachingInfo,
                                        setUpdateInstructor: setUpdateInstructor
                                    });
                                }
                            }
                            setCurrentFocus(displayClass.classId);
                        }}
                    >
                        Update
                    </button>
                </div>
            </div>
        );
    }

    async function addClass({ newClass }) {
        const img = newClass.image.split(',')[1];

        fetchData(`${API_URL}/specificClass`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                // classId: newClass.classId,
                classType: newClass.classType.substring(3),
                currentCapacity: newClass.currentCapacity,
                date: newClass.date,
                description: newClass.description,
                hourDuration: newClass.hourDuration,
                image: img,
                maxCapacity: newClass.maxCapacity,
                registrationFee: newClass.registrationFee,
                specificClassName: newClass.specificClassName,
                startTime: newClass.startTime,
                year: newClass.year
            })
        }, (data) => {
            if (data) {
                setClasses(prevClasses => [...prevClasses, data]);
                // close modal
                document.getElementById('add_modal').close();

                // clear input fields
                document.getElementById('add_modal').querySelectorAll('input').forEach(input => input.value = '');

                // reset states
                setAddImage("");
                setAddClassName("");
                setAddClassType("");
                setAddClassDescription("");
                setAddClassRegistrationFee("");
                setAddClassMaxCapacity("");
                setAddClassStartTime("");
                setAddClassHours("");
                setAddClassDate("");
            }
        });
    }

    function FormatAddContent() {
        return (
            <div>
                {/* // image */}
                <div className="text-sm pb-1">Add display image</div>
                <div className="flex flex-col md:flex-row justify-center items-center">
                {addImage && (
                    <div className="relative flex justify-center items-center w-2/12 aspect-[9/8]">
                        <img
                            loading="lazy"
                            src={addImage}
                            alt="Uploaded Image"
                            className="absolute inset-0 object-cover aspect-[9/8] rounded-lg"
                        />
                        <div className="absolute top-0 right-0">
                            <button 
                                className="aspect-square bg-error text-base-200 rounded-full m-1"
                                onClick={() => setAddImage("")}
                            >
                                <FiMinus />
                            </button>
                        </div>
                    </div>
                )}

                    <div className="py-2 md:px-2" />

                    <div className="flex w-8/12 justify-center items-center">
                        <input
                            type="file"
                            className="file-input"
                            accept="image/*"
                            onChange={(e) => {
                                const file = e.target.files[0];
                                const reader = new FileReader();
                                reader.onloadend = () => {
                                    setAddImage(reader.result);
                                };
                                reader.readAsDataURL(file);
                            }}
                        />
                    </div>
                </div>

                <div className="py-2" />

                {/* class type */}
                <div className="text-sm pb-1">Choose class type</div>
                <select
                    id="class_type"
                    className="select select-bordered w-full"
                    value={addClassType}
                    onChange={(e) => setAddClassType(e.target.value)}
                >
                    <option value="">Select a class type</option>
                    {classTypes.map(classType => {
                        if (!classType.isApproved) {
                            return;
                        }

                        return (
                            <option 
                                key={classType.id} 
                                value={classType.id} 
                            >
                                {classType.icon} {classType.className}
                            </option>
                        );
                    })}
                </select>

                <div className="py-2" />

                {/* // specific class name */}
                <AddUpdateInputFieldComponent title="Class Name" placeholder="" value={addClassName} setValue={setAddClassName} type="text" />

                <div className="py-2" />

                {/* // description */}
                <AddUpdateInputFieldComponent title="Description" placeholder="" value={addClassDescription} setValue={setAddClassDescription} type="text" textfield={true} />

                <div className="py-2" />

                {/* // registriation fee */}
                <AddUpdateInputFieldComponent title="Registration Fee" placeholder="" value={addClassRegistrationFee} setValue={setAddClassRegistrationFee} type="number" />

                <div className="py-2" />

                {/* // max capacity */}
                <AddUpdateInputFieldComponent title="Max Capacity" placeholder="" value={addClassMaxCapacity} setValue={setAddClassMaxCapacity} type="number" />

                <div className="py-2" />

                {/* // date */}
                <AddUpdateInputFieldComponent title="Date" placeholder="" value={addClassDate} setValue={setAddClassDate} type="date" />

                <div className="py-2" />

                {/* // start time */}
                <AddUpdateInputFieldComponent title="Start Time" placeholder="" value={addClassStartTime} setValue={setAddClassStartTime} type="time" />

                <div className="py-2" />

                {/* // hours */}
                <AddUpdateInputFieldComponent title="Hours" placeholder="" value={addClassHours} setValue={setAddClassHours} type="number" step="0.01" />

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
                            reset();
                        }}
                    >
                        Cancel
                    </button>
                    <button 
                        className="btn w-1/2 btn-primary"
                        onClick={() => {addClass({
                                newClass: {
                                    classType: addClassType,
                                    currentCapacity: addClassCurrentCapacity,
                                    date: addClassDate,
                                    description: addClassDescription,
                                    hourDuration: addClassHours,
                                    image: addImage,
                                    maxCapacity: addClassMaxCapacity,
                                    registrationFee: addClassRegistrationFee,
                                    specificClassName: addClassName,
                                    startTime: addClassStartTime,
                                    year: parseInt(addClassDate.substring(0, 4))
                                }
                            })
                            setCurrentFocus("");
                        }}
                    >
                        Create
                    </button>
                </div>
            </div>
        );
    }

    function buildTitle(klass) {
        if (!klass) {
            return "";
        }
        return "[ID: " + klass.classId + "]: " + klass.specificClassName;
    }

    function FilterContent() {
        return (
            <div className="pt-5 space-y-2">
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

    function filter(displayClass) {
        if (!displayClass) {
            return false;
        }

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
        const classDate = new Date(displayClass.date);
        const today = new Date();

        const withinDateRange = fromInf && toInf || fromInf && classDate <= endDate || toInf && classDate >= startDate || classDate >= startDate && classDate <= endDate;
        // toInf || (startDateRange && endDateRange && classDate >= startDate && classDate <= endDate);
        const classEnded = classDate < today;
        const classFull = displayClass.currentCapacity >= displayClass.maxCapacity;
        const classNotFull = displayClass.currentCapacity < displayClass.maxCapacity && classDate >= today;

        return withinDateRange && ((isEnded && classEnded) || (isFull && classFull) || (isNotFull && classNotFull));
    }   

    useEffect(() => {
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

        fetchData(`${API_URL}/classTypes`, {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json'
            }
        }, (data) => {
            setClassTypes(data.classTypes);
        });
    }, []);

    return (
        <>
            {/* search & filter */}
            <DashboardSearchComponent setSearch={setSearch} contents={classes} />

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

            {/* list of classes */}
            <DashboardListComponent title={buildTitle} contents={classes} search={search} filter={filter} format={FormatUpdateContent} />

            {/* add item modal */}
            <Modal id="add_modal">
                {FormatAddContent()}
            </Modal>
        </>
    );
}
