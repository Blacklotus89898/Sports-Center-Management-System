import React, { useState, useEffect } from "react";

import { useAtom } from "jotai";
import { currentUserAtom } from "../../utils/jotai";

import useFetch from "../../api/useFetch";

import { StatusBadge } from "./ClassListItem";
import { getUserRole } from "../../utils/auth";

const noImageUrl = 'https://orbis-alliance.com/wp-content/themes/consultix/images/no-image-found-360x260.png';

export default function UserClassBox({ id, imageSrc, status, name, description, date ,time, lengthInHrs, instructor }) {
    
    const API_URL = 'http://localhost:8080';
    const { data, loading, error, fetchData, reset } = useFetch();
    const [classRegistration, setClassRegistration] = useState({}); // for current user
    const [userRegistered, setUserRegistered] = useState(false);

    const [updateStatus, setUpdateStatus] = useState("");

    const [currentUser, ] = useAtom(currentUserAtom);

    useEffect(() => {
        // fetch class registration (/specificClass/{classId}/classRegistration)
        fetchData(`${API_URL}/specificClass/${id}/classRegistration`, {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
            }
        }, (data) => {
            if (data) {
                // check if user is registered (customer.id !=== currentUserId)
                const currentUserID = currentUser.id;
                const isUserRegistered = data.classRegistrations.some(registration => registration.customer.id === currentUserID);

                setUserRegistered(isUserRegistered);

                // set class registration for current user
                const userRegistration = data.classRegistrations.find(registration => registration.customer.id === currentUserID);
                setClassRegistration(userRegistration);

                // set current capacity
            }
        });

        updateClassStatus();
    }, []);

    async function updateClassStatus() {
        // fetch class
        fetchData(`${API_URL}/specificClass/${id}`, {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
            }
        }, (data) => {
            if (data) {
                // set current capacity
                const currentCapacity = data.currentCapacity;
                const maxCapacity = data.maxCapacity;
                let intStatus = "";

                if (currentCapacity >= maxCapacity) {
                    intStatus = "full";
                } else if (new Date(data.date) < new Date()) {
                    intStatus = "ended";
                } else {
                    intStatus = `${currentCapacity}/${maxCapacity} capacity`;
                }

                setUpdateStatus(intStatus);
            }
        });
    }

    async function leaveClass() {
        // delete @ /classRegistration/{registrationId}
        fetchData(`${API_URL}/classRegistration/${classRegistration.registrationId}`, {
            method: 'DELETE',
            headers: {
                'Content-Type': 'application/json',
            }
        }, (data) => {
            // update class registration for current user
            setClassRegistration({});
            setUserRegistered(false);

            updateClassStatus()
        });
    }

    async function joinClass() {
        // TODO: check if user's payment method exists

        await fetchData(`${API_URL}/classRegistration`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({
                classId: id,
                accountId: currentUser.id
            })
        }, (data) => {
            if (data) {
                // update class registrations
                setClassRegistration(data);
                setUserRegistered(true);

                // update current capacity
                updateClassStatus()
            }
        });
    }

    return (
        <div className="flex flex-col pt-5 space-y-2 md:flex-row w-full justify-center items-center md:items-stretch">
            <div className="flex w-10/12 md:w-2/5 justify-center items-center">
                {/* image */}
                {imageSrc && <img 
                    className="w-full rounded-xl object-cover aspect-[9/8]"
                    src={`data:image/jpeg;base64,${imageSrc}`}
                />}
                {!imageSrc && <img 
                    className="w-full rounded-xl object-cover aspect-[9/8]"
                    src={noImageUrl}
                />}
            </div>

            <div className="py-3 md:px-3" />

            <div className="flex flex-col w-10/12 md:w-3/5 justify-between">
                <div className="flex flex-col w-full">
                    <div className="flex justify-between items-center font-semibold">
                        <div className="text-primary-500 text-2xl overflow-hidden overflow-ellipsis whitespace-nowrap">
                            {name}
                        </div>
                        <div className="text-right">
                            {/* <StatusBadge status={status} /> */}
                            <StatusBadge status={updateStatus} />
                        </div>
                    </div>

                    <p className="text-primary-500 text-sm overflow-hidden max-w-full">ID: {id}</p>

                    <div className="py-2" />

                    {/* description */}
                    <p className="text-primary-500 text-base overflow-hidden max-w-full">{description}</p>

                    <div className="py-2" />

                    {/* date, time, length, instructor */}
                    <div className="text-primary-100 text-sm justify-end">
                        <p>{date} - {time} - {lengthInHrs} {lengthInHrs != 1 ? " hrs" : " hr"}</p>
                        <p>{instructor === "Instructor TBD" ? instructor : "Instructed by " + instructor}</p>
                    </div>
                </div>
                
                {userRegistered && <div className="flex w-full justify-end">
                    <button 
                        className="btn btn-error"
                        disabled={status === "full" || status === "ended"}
                        onClick={() => {
                            leaveClass();
                        }}
                    >
                        Leave Class
                    </button>
                </div>}
                {!userRegistered && <div className="flex w-full justify-end">
                    <button 
                        className="btn btn-primary"
                        disabled={updateStatus === "full" || updateStatus === "ended" || getUserRole() !== "CUSTOMER"}
                        onClick={() => {
                            joinClass();
                        }}
                    >
                        Join Class
                    </button>
                </div>}
            </div>
        </div>
    )
}