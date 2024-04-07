import React, { useState, useEffect } from "react";

import { getUserRole } from "../../../utils/auth";
import useFetch from "../../../api/useFetch";
import DashboardListComponent from "../DashboardListComponent";
import DashboardSearchComponent from "../DashboardSearchComponent";
import Modal from "../../Modal";
import EmojiPicker from "../../EmojiPickerComponents/EmojiPicker";
import AddUpdateInputFieldComponent from "../AddUpdateInputFieldComponent";

export default function Category() {
    const [search, setSearch] = React.useState("");
    const [filterCriteria, setFilterCriteria] = React.useState({});
    const [categories, setCategories] = React.useState([]);
    
    const API_URL = 'http://localhost:8080';
    const { data, loading, error, fetchData, reset } = useFetch();

    function FormatUpdateContent(category) {
        return (
            <div>
                <div className="text-red-400">{category.className}</div>
                <div>{category.description}</div>
                <div>{category.isApproved}</div>
            </div>
        );
    }

    async function AddCategory() {
        let icon = Array.from(document.getElementById('emoji').innerText)[0];
        let className = document.getElementById('addClassName').value;
        let description = document.getElementById('addDescription').value;
        let isApproved = document.getElementById('addApproved').checked;

        await fetchData(`${API_URL}/classType`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                icon: icon,
                className: className,
                description: description,
                isApproved: isApproved
            })
        }, (newClassType) => {
            if (newClassType) {
                document.getElementById('add_modal').close();
                document.getElementById('add_modal').querySelectorAll('input').forEach(input => input.value = '');
                setCategories([...categories, newClassType]);
            }
        });

        console.log(icon, className, description, isApproved);
    }

    function FormatAddContent() {
        return (
            <div>
                <div className="text-sm">Choose an icon:</div>
                <EmojiPicker />

                <div className="py-2" />

                <AddUpdateInputFieldComponent id="addClassName" title="Class type name" placeholder="" type="text" />

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

                <div className="py-2" />

                {/* buttons */}
                <div className="flex flex-row w-full space-x-2">
                    <button 
                        className="btn w-1/2"
                        onClick={() => {
                            document.getElementById('add_modal').close();
                            document.getElementById('add_modal').querySelectorAll('input').forEach(input => input.value = '');
                        }}
                    >
                        Cancel
                    </button>
                    <button 
                        className="btn w-1/2 btn-primary"
                        onClick={() => {AddCategory()}}
                    >
                        Create
                    </button>
                </div>
            </div>
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
                <FormatAddContent />
            </Modal>
        </>
    );
}