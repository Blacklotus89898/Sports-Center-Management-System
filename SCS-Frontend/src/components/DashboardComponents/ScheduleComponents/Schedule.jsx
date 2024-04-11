import React, { useState, useEffect } from "react";
import { useNavigate, useLocation } from "react-router-dom";

import useFetch from "../../../api/useFetch";

import DashboardSearchComponent from "../DashboardSearchComponent";
import DashboardListComponent from "../DashboardListComponent";
import AddUpdateInputFieldComponent from "../AddUpdateInputFieldComponent";
import FilterSetting from "../../SearchComponents/FilterSetting";
import Modal from "../../Modal";
import SpecificSchedule from "./SpecificSchedule";

export default function Schedule() {
    const [currentFocus, setCurrentFocus] = useState(null);
    const [fetching, setFetching] = useState(false);
    const [search, setSearch] = useState("");
    const [schedules, setSchedules] = useState([]);

    const [addYear, setAddYear] = useState(new Date().getFullYear());

    const API_URL = 'http://localhost:8080';
    const { data, loading, error, fetchData, reset } = useFetch();

    let navigate = useNavigate();

    const [pageYear, setPageYear] = useState(null);
    const [render, setRender] = useState(0);

    const location = useLocation();
    const queryParams = new URLSearchParams(location.search);

    function FormatUpdateContent(schedule) {
        return (
            <div 
                className="card w-full bg-base-200 hover:cursor-pointer"
                onClick={() => {
                    navigate("?year=" + schedule.year)
                }}
            >
                <div className="card-body">
                    {schedule.year}
                </div>
            </div>
        );
    }

    async function createSchedule(schedule) {
        await fetchData(`${API_URL}/schedule`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(schedule)
        }, (data) => {
            if (data) {
                // save schedules and set new schedule
                setSchedules([...schedules, data]);
                setAddYear(new Date().getFullYear());

                document.getElementById('add_modal').close();

                // clear input fields
                document.getElementById('add_modal').querySelectorAll('input').forEach(input => input.value = '');
            }
        });
    }


    function FormatAddContent() {
          return (
            <div>
                <AddUpdateInputFieldComponent title="Year" placeholder="" value={addYear} setValue={setAddYear} type="number" />

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
                        onClick={() => {
                            createSchedule({
                                year: addYear
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

    function FilterContent() {
        return (
            <>
                No filter options available for this page.
            </>
        )
    }

    function filter(schedule) {
        if (search === "" || search === undefined || search === null) {
            return true;
        } else {
            let scheduleString = JSON.stringify(schedule).toLowerCase();
            let searchString = search.toLowerCase();
            return scheduleString.includes(searchString);
        }
    }   

    useEffect(() => {
        fetchData(`${API_URL}/schedules`, {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json'
            }
        }, (data) => {
            setSchedules(data.schedules);
        });
    }, []);

    useEffect(() => {
        if (queryParams.get("year")) {
            setPageYear(queryParams.get("year"));
        } else {
            setPageYear(null);
        }
    }, [queryParams.get("year")]);

    return (
        <>
            {!pageYear ?
            <>
                {/* search & filter */}
                <DashboardSearchComponent setSearch={setSearch} contents={schedules} />

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

                {/* list of schedules */}
                <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
                    {schedules.map((schedule, index) => {
                        if (filter(schedule)) {
                            return FormatUpdateContent(schedule);
                        }
                    })}
                </div>

                {/* add button */}
                <div className="fixed bottom-5 right-5">
                    <button 
                        className="btn btn-primary btn-circle"
                        onClick={() => {
                            document.getElementById('add_modal').showModal();
                        }}
                    >
                        +
                    </button>
                </div>

                {/* add modal */}
                <Modal id="add_modal" title="Add Schedule">
                    {FormatAddContent()}
                </Modal>

                {/* specific schedule */}
                {pageYear && <SpecificSchedule year={pageYear} />}

            </> :
                <SpecificSchedule year={pageYear} />
            }
        </>
    );
}