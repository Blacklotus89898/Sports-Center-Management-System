import React, { useState, useEffect } from "react";

import { FiMinus, FiTrash2 } from "react-icons/fi";

import useFetch from "../../../api/useFetch";

import DashboardSearchComponent from "../DashboardSearchComponent";
import DashboardListComponent from "../DashboardListComponent";
import FilterSetting from "../../SearchComponents/FilterSetting";
import Modal from "../../Modal";
import AddUpdateInputFieldComponent from "../AddUpdateInputFieldComponent";

export default function Users() {
    const [currentFocus, setCurrentFocus] = useState(null);
    const [fetching, setFetching] = useState(false);
    const [search, setSearch] = useState("");
    const [users, setUsers] = useState([]);

    // state for add modal
    const [addImage, setAddImage] = useState("");
    const [addName, setAddName] = useState("");
    const [addPassword, setAddPassword] = useState("");
    const [addEmail, setAddEmail] = useState("");
    const [addRole, setAddRole] = useState("");

    // filter states
    const [showInstructors, setShowInstructors] = useState(true);
    const [showCustomers, setShowCustomers] = useState(true);

    const API_URL = 'http://localhost:8080';
    const { data, loading, error, fetchData, reset } = useFetch();

    async function deleteUser(user) {
        if (user.role === "CUSTOMER") {
            fetchData(`${API_URL}/customers/${user.id}`, {
                method: 'DELETE',
                headers: {
                    'Content-Type': 'application/json'
                }
            }, () => {
                setUsers(users.filter(u => u.id !== user.id));
            });
        } else if (user.role === "INSTRUCTOR") {
            fetchData(`${API_URL}/instructors/${user.id}`, {
                method: 'DELETE',
                headers: {
                    'Content-Type': 'application/json'
                }
            }, () => {
                setUsers(users.filter(u => u.id !== user.id));
            });
        }
    }

    async function updateUser(user) {        
        if (user.role === "CUSTOMER") {
            fetchData(`${API_URL}/customers/${user.id}`, {
                method: 'PUT',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({
                    image: user.image,
                    name: user.name,
                    email: user.email,
                    password: user.password,
                })
            }, (data) => {
                if (data) {
                    data.role = "CUSTOMER";
                    setUsers(users.map(u => u.id === data.id ? data : u));
                    document.getElementById('success_modal').showModal();
                }
            });
        } else if (user.role === "INSTRUCTOR") {
            fetchData(`${API_URL}/instructors/${user.id}`, {
                method: 'PUT',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({
                    image: user.image,
                    name: user.name,
                    email: user.email,
                    password: user.password,
                })
            }, (data) => {
                if (data) {
                    data.role = "INSTRUCTOR";
                    setUsers(users.map(u => u.id === data.id ? data : u));
                    document.getElementById('success_modal').showModal();

                }
            });
        }
    }


    function FormatUpdateContent(user) {
        const [updateImage, setUpdateImage] = useState(user.image);
        const [updateName, setUpdateName] = useState(user.name);
        const [updateEmail, setUpdateEmail] = useState(user.email);
        const [updatePassword, setUpdatePassword] = useState(user.password);
        const [updateRole, setUpdateRole] = useState(user.role);

        return (
            <>
                {/* pfp */}
                <div className="text-sm pb-1">Profile picture</div>
                <div className="flex flex-col md:flex-row justify-center items-center">
                {updateImage && (
                    <div className="relative flex justify-center items-center w-2/12 aspect-square">
                        <img
                            loading="lazy"
                            src={`data:image/jpeg;base64,${updateImage}`}
                            alt="Uploaded Image"
                            className="absolute inset-0 object-cover aspect-square rounded-lg"
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

                    <div className="flex w-8/12 justify-center items-center">
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

                {/* name */}
                <AddUpdateInputFieldComponent id="name" title="Name" placeholder="" value={updateName} setValue={setUpdateName} type="text" />

                <div className="py-2" />

                {/* email */}
                <AddUpdateInputFieldComponent id="email" title="Email" placeholder="" value={updateEmail} setValue={setUpdateEmail} type="email" />

                <div className="py-2" />

                {/* password */}
                <AddUpdateInputFieldComponent id="password" title="Password" placeholder="" value={updatePassword} setValue={setUpdatePassword} type="password" />

                <div className="py-2" />

                {/* role */}
                <div className="text-sm pb-1">Role (Cannot be changed.)</div>
                <select
                    className="select select-bordered w-full"
                    value={updateRole}
                >
                    <option>
                        {user.role === "CUSTOMER" ? "Customer" : "Instructor"}
                    </option>
                </select>

                <div className="py-2" />

                {/* error message */}
                {(error && currentFocus === user.id) && <div className='py-1 text-error text-center'>{data?.errors?.toString()}</div>}

                {/* buttons */}
                <div className="flex flex-row w-full space-x-2">
                    <button 
                        className="btn btn-error text-lg"
                        onClick={() => {
                            deleteUser(user);
                        }}
                    >
                        <FiTrash2 />
                    </button>
                    <div className="grow"/>
                    <button 
                        className="btn btn-primary"
                        onClick={() => {
                            updateUser({
                                id: user.id,
                                image: updateImage,
                                name: updateName,
                                email: updateEmail,
                                password: updatePassword,
                                role: updateRole
                            })
                            setCurrentFocus(user.id);
                        }}
                    >
                        Update
                    </button>
                </div>
            </>
        )

    }

    async function addUser(user) {
        const img = user.image.split(',')[1];

        if (user.role === "CUSTOMER") {
            fetchData(`${API_URL}/customers`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({
                    image: img,
                    name: user.name,
                    email: user.email,
                    password: user.password,
                })
            }, (data) => {
                if (data) {
                    data.role = "CUSTOMER";
                    setUsers([...users, data]);

                    // close modal
                    document.getElementById('add_modal').close();

                    // reset fields
                    document.getElementById('add_modal').querySelectorAll('input').forEach(input => input.value = '');
                    setAddImage("");
                    setAddName("");
                    setAddEmail("");
                    setAddPassword("");
                    setAddRole("");

                    document.getElementById('success_modal').showModal();

                }
            }); 
        } else if (user.role === "INSTRUCTOR") {
            fetchData(`${API_URL}/instructors`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({
                    image: img,
                    name: user.name,
                    email: user.email,
                    password: user.password,
                })
            }, (data) => {
                if (data) {
                    data.role = "INSTRUCTOR";
                    setUsers([...users, data]);

                    // close modal
                    document.getElementById('add_modal').close();

                    // reset fields
                    document.getElementById('add_modal').querySelectorAll('input').forEach(input => input.value = '');
                    setAddImage("");
                    setAddName("");
                    setAddEmail("");
                    setAddPassword("");
                    setAddRole("");
                
                    document.getElementById('success_modal').showModal();

                }
            });

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
                            setCurrentFocus(displayClass.classId);
                        }}
                    >
                        Update
                    </button>
                </div>
        }
    }

    function FormatAddContent() {
        return (
            <div>
                {/* pfp */}
                <div className="text-sm pb-1">Profile picture</div>
                <div className="flex flex-col md:flex-row justify-center items-center">
                {addImage && (
                    <div className="relative flex justify-center items-center w-2/12 aspect-square">
                        <img
                            loading="lazy"
                            src={addImage}
                            alt="Uploaded Image"
                            className="absolute inset-0 object-cover aspect-square rounded-lg"
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

                {/* name */}
                <AddUpdateInputFieldComponent id="name" title="Name" placeholder="" value={addName} setValue={setAddName} type="text" />

                <div className="py-2" />

                {/* email */}
                <AddUpdateInputFieldComponent id="email" title="Email" placeholder="" value={addEmail} setValue={setAddEmail} type="email" />

                <div className="py-2" />

                {/* password */}
                <AddUpdateInputFieldComponent id="password" title="Password" placeholder="" value={addPassword} setValue={setAddPassword} type="password" />

                <div className="py-2" />

                {/* role */}
                <div className="text-sm pb-1">Role</div>
                <select 
                    id="role" 
                    className="select select-bordered w-full"
                    value={addRole}
                    onChange={(e) => setAddRole(e.target.value)}
                >
                    <option value="">Select a role</option>
                    <option value="CUSTOMER">Customer</option>
                    <option value="INSTRUCTOR">Instructor</option>
                </select>

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
                        disabled={addName === "" || addEmail === "" || addPassword === "" || addRole === ""}
                        onClick={() => {
                            addUser({
                                image: addImage,
                                name: addName,
                                email: addEmail,
                                password: addPassword,
                                role: addRole
                            });
                            setCurrentFocus("");
                        }}
                    >
                        Create
                    </button>
                </div>
            </div>
        );
    }

    function buildTitle(user) {
        return "[ID: " + user.id + " - " + user.role + "]: " + user.email;
    }

    function FilterContent() {
        return (
            <div className="pt-5 space-y-2">
                {/* removed until functinoality is implemented */}
                <div className="flex flex-row w-full">
                    <div className="text-sm">Show instructors</div>
                    <div className="grow" />
                    <input 
                        id="addApproved"
                        type="checkbox" 
                        className="checkbox"
                        checked={showInstructors}
                        onChange={(e) => setShowInstructors(e.target.checked)}
                    />
                </div>
                <div className="flex flex-row w-full">
                    <div className="text-sm">Show customers</div>
                    <div className="grow" />
                    <input 
                        id="addApproved"
                        type="checkbox" 
                        className="checkbox"
                        checked={showCustomers}
                        onChange={(e) => setShowCustomers(e.target.checked)}
                    />
                </div>
            </div>
        )
    }

    function filter(user) {
        return (showInstructors && user.role === "INSTRUCTOR") || (showCustomers && user.role === "CUSTOMER");
    }   

    useEffect(() => {
        let instructors = [];
        let customers = [];
        setFetching(true);
        fetchData(`${API_URL}/customers`, {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json'
            }
        }, (data1) => {
            customers = data1.customers.map(customer => ({ ...customer, role: "CUSTOMER" }));
            
            fetchData(`${API_URL}/instructors`, {
                method: 'GET',
                headers: {
                    'Content-Type': 'application/json'
                }
            }, (data2) => {
                instructors = data2.instructors.map(instructor => ({ ...instructor, role: "INSTRUCTOR" }));
                setUsers([...customers, ...instructors].sort((a, b) => a.id - b.id));
                setFetching(false);
            });            
        });
    }, []);

    return (
        <>
            {/* search & filter */}
            <DashboardSearchComponent setSearch={setSearch} contents={users} />

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
            <DashboardListComponent title={buildTitle} contents={users} search={search} filter={filter} format={FormatUpdateContent} />

            {/* add item modal */}
            <Modal id="add_modal">
                {FormatAddContent()}
            </Modal>
 
            <Modal id="success_modal" width="w-60">
                <div className="text-green-400 text-center">
                    Success
                </div>
            </Modal>
            


        </>
    );
}
