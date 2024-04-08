import React, { useState, useEffect } from "react";

import useFetch from "../../../api/useFetch";

import DashboardSearchComponent from "../DashboardSearchComponent";
import DashboardListComponent from "../DashboardListComponent";
import FilterSetting from "../../SearchComponents/FilterSetting";
import Modal from "../../Modal";
import AddUpdateInputFieldComponent from "../AddUpdateInputFieldComponent";

import { FiMinus, FiTrash2 } from "react-icons/fi";

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

    const API_URL = 'http://localhost:8080';
    const { data, loading, error, fetchData, reset } = useFetch();

    function FormatUpdateContent(displayClass) {
        if (!displayClass) {
            return;
        }
        console.log("format update", displayClass);

        return (
            <div>
                {/* buttons */}
                <div className="flex flex-row w-full space-x-2">
                    <button 
                        className="btn btn-error text-lg"
                        onClick={() => {
                        }}
                    >
                        <FiTrash2 />
                    </button>
                    <div className="grow"/>
                    <button 
                        className="btn btn-primary"
                        onClick={() => {
                            
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
                {error && <div className='py-1 text-error text-center'>{data?.errors?.toString()}</div>}

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
                        })}}
                    >
                        Create
                    </button>
                </div>
            </div>
        );
    }

    function buildTitle(klass) {
        console.log(klass);
        if (!klass) {
            return "";
        }
        return "ID: " + klass.classId + " - " + klass.specificClassName;
    }

    function FilterContent() {
            
    }

    function filter(classes) {
        return true;
    }   

    useEffect(() => {
        fetchData(`${API_URL}/schedules`, {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json'
            }
        }, (data) => {
            setSchedules(data.schedules);
            // loop through schedules and get classes by year
            data.schedules.forEach(schedule => {
                console.log(schedule.year);
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
