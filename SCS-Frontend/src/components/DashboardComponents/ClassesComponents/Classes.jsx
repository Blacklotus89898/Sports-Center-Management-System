import React from "react";

import { getUserRole } from "../../../utils/auth";

import StaffClasses from "./StaffClasses";
import CustomerClasses from "./CustomerClasses";

export default function Classes() {
    return (
        <>
            { getUserRole() === "CUSTOMER" ? 
                <>
                    <CustomerClasses />
                </> 
                :
                <>
                    <StaffClasses />
                </>
            }
        </>
    );
}