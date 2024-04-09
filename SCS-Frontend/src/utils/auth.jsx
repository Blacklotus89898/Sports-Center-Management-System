import React from "react";
import { useAtom } from "jotai";
import { currentUserAtom } from "../utils/jotai";

const API_URL = 'http://localhost:8080';

// logout
export function useLogout() {
    const [, setCurrentUser] = useAtom(currentUserAtom);

    const logout = () => {
        setCurrentUser({
            "id": -1,
            "role": null,
            "name": null,
            "email": null,
            "password": null,
            "creationDate": null,
            "image": null
        });
    };

    return logout;
}

// This function is used to check if the user is logged in
export function isUserLoggedIn() {
    const [currentUser, ] = useAtom(currentUserAtom);
    return currentUser.id !== -1;
}

// This function is used to get the user role
export function getUserRole() {
    // const [currentUser, ] = useAtom(currentUserAtom);
    // return currentUser.role;
    return 'INSTRUCTOR'; // OWNER, INSTRUCTOR, CUSTOMER
}