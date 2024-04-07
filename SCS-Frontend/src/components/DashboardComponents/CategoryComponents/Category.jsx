import React, { useState, useEffect } from "react";
import { FiTrash2 } from "react-icons/fi";

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

    async function deleteCategory(category) {
        await fetchData(`${API_URL}/classType/${category.className}`, {
            method: 'DELETE',
            headers: {
                'Content-Type': 'application/json'
            }
        }, (deletedCategory) => {
            console.log("Deleted class type: ", category.className);
            setCategories(categories.filter((c) => c.className !== category.className));
        });

        console.log(category.icon, category.className, category.description, category.isApproved);
    }

    async function updateCategory(category) {
        await fetchData(`${API_URL}/classTypes/${category.className}`, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                icon: category.icon,
                description: category.description,
            })
        }, (updatedClassType) => {
            if (updatedClassType) {
                setCategories(categories.map((c) => c.className === updatedClassType.className ? updatedClassType : c));
            }
        });

        if (getUserRole() === "OWNER") {
            await fetchData(`${API_URL}/classTypes/${category.className}/approved/${category.isApproved}`, {
                method: 'PUT',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({})
            }, (updatedClassType) => {
                if (updatedClassType) {
                    setCategories(categories.map((c) => c.className === updatedClassType.className ? updatedClassType : c));
                }
            });
        }

        console.log(category.icon, category.className, category.description, category.isApproved);
    }

    function FormatUpdateContent(category) {
        const [updateIcon, setUpdateIcon] = useState(category.icon);
        const [updateDescription, setUpdateDescription] = useState(category.description);
        const [updateApproved, setUpdateApproved] = useState(category.isApproved);

        return (
            <>
                <div className="flex flex-col md:flex-row">
                    <div className="flex flex-col w-full">
                        <div className="text-sm">Choose an icon:</div>
                        <EmojiPicker selectedEmoji={updateIcon} setSelectedEmoji={setUpdateIcon} col={true} />
                    </div>

                    <div className="py-2 md:px-2" />

                    <div className="w-full">
                        <AddUpdateInputFieldComponent title="Description" placeholder="" type="text" value={updateDescription} setValue={setUpdateDescription} textfield={true} />

                        <div className="py-2" />

                        {getUserRole() === "OWNER" && 
                            <>
                                <div className="flex flex-row w-full">
                                    <div className="text-sm">Approved</div>
                                    <div className="grow" />
                                    <input 
                                        type="checkbox" 
                                        className="checkbox"
                                        checked={updateApproved}
                                        onChange={(e) => setUpdateApproved(e.target.checked)}
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
                        className="btn btn-error text-lg"
                        onClick={() => {
                            deleteCategory(category);
                        }}
                    >
                        <FiTrash2 />
                    </button>
                    <div className="grow"/>
                    <button 
                        className="btn btn-primary"
                        onClick={() => {
                            updateCategory({
                                icon: updateIcon,
                                className: category.className,
                                description: updateDescription,
                                isApproved: updateApproved
                            });
                        }}
                    >
                        Update
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
                document.getElementById('add_modal').querySelectorAll('textarea').forEach(input => input.value = '');
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

                <AddUpdateInputFieldComponent id="addDescription" title="Description" placeholder="" type="text" setValue={setAddDescription} textfield={true} />

                <div className="py-2" />

                {getUserRole() === "OWNER" && 
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