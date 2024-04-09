import React, { useState, useEffect } from "react";

import DashboardSearchComponent from "../DashboardSearchComponent";
import DashboardListComponent from "../DashboardListComponent";
import FilterSetting from "../../SearchComponents/FilterSetting";
import Modal from "../../Modal";

import useFetch from "../../../api/useFetch";

export default function SpecificSchedule({year}) {
    const [currentFocus, setCurrentFocus] = useState(null);
    const [fetching, setFetching] = useState(false);
    const [search, setSearch] = useState("");
    const [openingHours, setOpeningHours] = useState([]);
    const [customHours, setCustomHours] = useState([]);

    const API_URL = 'http://localhost:8080';
    const { data, loading, error, fetchData, reset } = useFetch();


    function FormatUpdateContent(customHour) {
        return (
            <>h</>
        )
    }

    function FormatAddContent() {
        return (
            <>
                <h1>Add new schedule</h1>
            </>
        )
    }

    function buildTitle(customHour) {
        return (
            <>

            </>
        )
    }

    function FilterContent() {
        return (
            <>
                <h1>Filter</h1>
            </>
        )
    }

    useEffect(() => {
        // get all opening hours (http://localhost:8080/schedules/{year}/openingHours)
        fetchData(`${API_URL}/schedules/${year}/openingHours`, {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json'
            }
        }, (data) => {
            setOpeningHours(data);
        });

        // get all custom hours (http://localhost:8080/schedules/{year}/customHours)
        fetchData(`${API_URL}/schedules/${year}/customHours`, {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json'
            }
        }, (data) => {
            // only add if schedule.year === year
            setCustomHours(data.customHours.filter(schedule => schedule.year === year));
        });
    }, []);

    return (
        <>
            editing schedule for year {year}

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