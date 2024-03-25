import React from "react";
import MockComponent from "../components/MockComponent"; //import here

export default function Home() {
    return (
        <div>
            <h1>Home</h1>
            <p>Welcome to the SCS-Frontend application!</p>
            <MockComponent /> {/* add here */}
        </div>
    );
}