import React, { useState, useEffect } from "react";

import { getUserRole } from "../../../utils/auth";
import useFetch from "../../../api/useFetch";
import DashboardListComponent from "../DashboardListComponent";
import DashboardSearchComponent from "../DashboardSearchComponent";
import Modal from "../../Modal";
import EmojiPicker from "../../EmojiPickerComponents/EmojiPicker";
import AddUpdateInputFieldComponent from "../AddUpdateInputFieldComponent";

export default function Category() {
    const [search, setSearch] = useState("");
    const [filterCriteria, setFilterCriteria] = useState({});
    const [categories, setCategories] = useState([]);

    const [addIcon, setAddIcon] = useState('');
    const [addClassName, setAddClassName] = useState("");
    const [addDescription, setAddDescription] = useState("");
    const [addApproved, setAddApproved] = useState(false);

    const API_URL = 'http://localhost:8080';
    const { data, loading, error, fetchData, reset } = useFetch();

    async function updateCategory(category) {
        // let icon = Array.from(document.getElementById('emoji').innerText)[0];
        // let className = document.getElementById('updateClassName').value;
        // let description = document.getElementById('updateDescription').value;
        // let isApproved = document.getElementById('updateApproved').checked;

        // await fetchData(`${API_URL}/classType/${category.id}`, {
        //     method: 'PUT',
        //     headers: {
        //         'Content-Type': 'application/json'
        //     },
        //     body: JSON.stringify({
        //         icon: icon,
        //         className: className,
        //         description: description,
        //         isApproved: isApproved
        //     })
        // }, (updatedClassType) => {
        //     if (updatedClassType) {
        //         document.getElementById('update_modal').close();
        //         setCategories(categories.map(c => c.id === category.id ? updatedClassType : c));
        //     }
        // });

        // console.log(icon, className, description, isApproved);
    }

    function FormatUpdateContent(category) {
        return (
            <>
                <div className="flex flex-row">
                    <div className="flex flex-col w-full justify-center items-center content-center">
                        <div className="text-sm">Choose an icon:</div>
                        <EmojiPicker col={true} />
                    </div>

                    <div className="w-full">
                        <AddUpdateInputFieldComponent id="addClassName" title="Class type name" placeholder="" type="text" setValue={setAddClassName} />

                        <div className="py-2" />

                        <AddUpdateInputFieldComponent id="addDescription" title="Description" placeholder="" type="text" />

                        <div className="py-2" />

                        {getUserRole() && 
                            <>
                                <div className="flex flex-row w-full">
                                    <div className="text-sm">Approved</div>
                                    <div className="grow" />
                                    <input 
                                        id="addApproved"
                                        type="checkbox" 
                                        className="checkbox"
                                    />
                                </div>
                            </>
                        }
                    </div>
                </div>
                    
                <div className="py-2" />

                {/* buttons */}
                <div className="flex flex-row w-full space-x-2">
                    <button 
                        className="btn w-1/2"
                        onClick={() => {
                        }}
                    >
                        Delete
                    </button>
                    <button 
                        className="btn w-1/2 btn-primary"
                        onClick={() => {}}
                    >
                        Create
                    </button>
                </div>
            </>
        );
    }

    async function addCategory() {
        await fetchData(`${API_URL}/classType`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                icon: addIcon,
                className: addClassName,
                description: addDescription,
                isApproved: addApproved
            })
        }, (newClassType) => {
            if (newClassType) {
                document.getElementById('add_modal').close();
                document.getElementById('add_modal').querySelectorAll('input').forEach(input => input.value = '');
                setCategories([...categories, newClassType]);
                setAddIcon('');
                reset();
            }
        });

        console.log(addIcon, addClassName, addDescription, addApproved);
    }

    function FormatAddContent() {
        return (
            <>
                <div className="text-sm">Choose an icon:</div>
                <EmojiPicker selectedEmoji={addIcon} setSelectedEmoji={setAddIcon} />

                <div className="py-2" />
                                
                <AddUpdateInputFieldComponent id="addClassName" title="Class type name" placeholder="" type="text" setValue={setAddClassName} />

                <div className="py-2" />

                <AddUpdateInputFieldComponent id="addDescription" title="Description" placeholder="" type="text" setValue={setAddDescription} />

                <div className="py-2" />

                {getUserRole() && 
                    <>
                        <div className="flex flex-row w-full">
                            <div className="text-sm">Approved</div>
                            <div className="grow" />
                            <input 
                                id="addApproved"
                                type="checkbox" 
                                className="checkbox"
                                onChange={(e) => setAddApproved(e.target.checked)}
                            />
                        </div>
                    </>
                }

                <div className="py-2" />

                {/* error message */}
                {error && <div className='py-1 text-error text-center'>{data.errors.toString()}</div>}

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
                        onClick={() => {addCategory();}}
                    >
                        Create
                    </button>
                </div>
            </>
        );
    }
    
    function buildTitle(category) {
        return category.icon + "  " + category.className;
    }
    
    function filter(category) {
        // use filterCriteria to filter the content

       return true;
    }

    useEffect(() => {
        fetchData(`${API_URL}/classTypes`, {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json'
            }
        }, (data) => {
            setCategories(data.classTypes);
        });
    }, []);

    return (
        <>
            {/* search & filter */}
            <DashboardSearchComponent setSearch={setSearch} setFilter={setFilterCriteria} contents={categories} />

            {/* line */}
            <hr className="my-5" />

            {/* list of categories */}
            <DashboardListComponent title={buildTitle} contents={categories} search={search} filter={filter} format={FormatUpdateContent} />

            {/* add item modal */}
            <Modal id="add_modal">
                {FormatAddContent()}
            </Modal>
        </>
    );
}