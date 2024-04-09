import React, { useState, useEffect } from "react";
// import { SpecificClassService } from "../api/SpecificClassService"; //uncomment if you use service isntead of hooks
import useFetch from "../../api/useFetch";

const API_URL = 'http://localhost:8080/';

export const getSpecificClasss = async (startDate, endDate, year) => {
  const { data, error, fetchData } = useFetch();

  await fetchData(`${API_URL}specificClass/year/${year}`, {});

  if (error) {
    throw new Error('Error fetching specific classes');
  }

  return data;
};

export default function SpecificClass() {

    //states
    const [SpecificClass, setSpecificClass] = useState({
        classId: -1,
        classType: '',
        year: 2022,
        specificClassName: '',
        description: '',
        date: '2022-10-12', //YYYY-MM-DD
        startTime: '11:59:59', // "HH:MM:SS"
        hourDuration: 1,
        maxCapacity: 1,
        currentCapacity: 1,
        registrationFee: 1,
        image: null,
    });

//     let date = new Date();

// // Convert to a string in the format "MM/DD/YYYY, HH:MM:SS AM/PM"
// let dateTimeString = date.toLocaleString();

// // Convert to a string in the format "MM/DD/YYYY"
// let dateString = date.toLocaleDateString();

// // Convert to a string in the format "HH:MM:SS AM/PM"
// let timeString = date.toLocaleTimeString();

// console.log(dateTimeString);  // Outputs something like "12/31/2022, 11:59:59 PM"
// console.log(dateString);  // Outputs something like "12/31/2022"
// console.log(timeString);  // Outputs something like "11:59:59 PM"


    //update of each field
    const handleChange = async (event) => {
        if (event.target.name === "image") {
            const file = event.target.files[0];
            const reader = new FileReader();

            reader.onload = function (e) {
                let base64Image = e.target.result;
                base64Image = base64Image.split(',')[1]; // Remove the data type from the Base64 string
                setSpecificClass({
                    ...SpecificClass,
                    ["image"]: base64Image // Remove the data type from the Base64 string
                });
            }

            reader.readAsDataURL(file);
        }
        else {
            setSpecificClass({
                ...SpecificClass,
                [event.target.name]: event.target.value
            });
        }
    }

    //fetch SpecificClasss on load
    useEffect(() => {
        getSpecificClasss();
    }, []);


    // useFetch hook
    // data: return value from the fetch
    // loading: use to render once loading stops
    // error: string
    // fetchData(url: string, options: {method: string, headers: {Content-Type: string}, body: string })
    const { data, loading, error, fetchData } = useFetch();  //instantiates the hook

    const getSpecificClasss = async () => {
        await fetchData(`${API_URL}specificClass/year/${SpecificClass.year}`, {});
        console.log(data);
    }

    const getSpecificClassById = async () => {
        console.log(SpecificClass.classId);
        await fetchData(`${API_URL}specificClass/${SpecificClass.classId}`, {}); //make base URL a constant
    }


    const createSpecificClass = async (event) => {
        console.log("creating", SpecificClass); //for testing
        console.log("creating", JSON.stringify(SpecificClass)); //for testing
        event.preventDefault(); //prevents reload //will be changed later
        // console.log(SpecificClass);
        await fetchData(`${API_URL}specificClass`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(SpecificClass)
        });
        if (error != null) console.log(error);

    }

    const deleteSpecificClassById = async () => {
        console.log("deleting", SpecificClass.id);    //for testing
        await fetchData(`${API_URL}SpecificClasss/${SpecificClass.id}`, {
            method: 'DELETE',
            headers: {
                'Content-Type': 'application/json'
            },
        });
    }

    const updateSpecificClassById = async (event) => {
        event.preventDefault();
        await fetchData(`${API_URL}SpecificClasss/${SpecificClass.id}`, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(SpecificClass)
        });
    }


    return (
        <div className="border border-solid-black w-80">
            <h1>Mock Component</h1>
            <button onClick={getSpecificClasss} className="border-4 border-blue-400">Get All</button>

            <input type="number" name="classId" id="" value={SpecificClass.classId} onChange={handleChange} placeholder="Enter id" />
            <button onClick={getSpecificClassById} className="border-4 border-green-500">Find By ID</button>
            <button onClick={deleteSpecificClassById} className="border-4 border-red-500">Delete By ID</button>

                // Input form for creation
            <div>
                <form onSubmit={createSpecificClass}>
                    <input
                        type="text"
                        name="classType"
                        value={SpecificClass.classType}
                        onChange={handleChange}
                        placeholder="ClasType"
                        required
                    />
                    <input
                        type="text"
                        name="specificClassName"
                        value={SpecificClass.specificClassName}
                        onChange={handleChange}
                        placeholder="Specific Class Name"
                        required
                    />
                    <input
                        type="number"
                        name="year"
                        value={SpecificClass.year}
                        onChange={handleChange}
                        placeholder="Year"
                        required
                    />
                    <input
                        type="text"
                        name="date"
                        value={SpecificClass.date}
                        onChange={handleChange}
                        placeholder="Date"
                        required
                    />
                    <input
                        type="text"
                        name="description"
                        value={SpecificClass.description}
                        onChange={handleChange}
                        placeholder="Description"
                        required
                    />
                    <input
                        type="text"
                        name="startTime"
                        value={SpecificClass.startTime}
                        onChange={handleChange}
                        placeholder="Start Time"
                        required
                    />
                    <input
                        type="number"
                        name="hourDuration"
                        value={SpecificClass.hourDuration}
                        onChange={handleChange}
                        placeholder="Hour Duration"
                        required
                    />
                    <input
                        type="number"
                        name="maxCapacity"
                        value={SpecificClass.maxCapacity}
                        onChange={handleChange}
                        placeholder="Max Capacity"
                        required
                    />
                    <input
                        type="number"
                        name="currentCapacity"
                        value={SpecificClass.currentCapacity}
                        onChange={handleChange}
                        placeholder="Current Capacity"
                        required
                    />
                    <input
                        type="number"
                        name="registrationFee"
                        value={SpecificClass.registrationFee}
                        onChange={handleChange}
                        placeholder="Registration Fee"
                        required
                    />
                    <input
                        type="file"
                        name="image"
                        onChange={handleChange}
                        placeholder="Drop image"
                    />
                    <button type="submit">Create SpecificClass</button>
                </form>
            </div>

            {/* Method 2 */}
            {/* <form onSubmit={createSpecificClass} className="border-4 border-solid border-green-300"> CREATE
                    <input type="text" name="name" value={SpecificClass.name} onChange={handleChange} placeholder="Name" required />
                    <input type="email" name="email" value={SpecificClass.email} onChange={handleChange} placeholder="Email" required />
                    <input type="password" name="password" value={SpecificClass.password} onChange={handleChange} placeholder="Password" required />
                    <input type="file" name="image" onChange={handleChange} placeholder="Drop image" />
                    <button type="submit">Create SpecificClass</button>
                </form> */}

            {/* Update */}
            {/* <form onSubmit={updateSpecificClassById} className="border-4 border-solid border-orange-300"> UPDATE
                    <input type="text" name="name" value={SpecificClass.name} onChange={handleChange} placeholder="Name" required />
                    <input type="email" name="email" value={SpecificClass.email} onChange={handleChange} placeholder="Email" required />
                    <input type="password" name="password" value={SpecificClass.password} onChange={handleChange} placeholder="Password" required />
                    <button type="submit">Update SpecificClass</button>
                </form> */}
            <h1>Object in form</h1>
            {Object.keys(SpecificClass).map(function (key) { return <div key={key}>Key: {key}, Value: {SpecificClass[key]}</div>; })}
            <div className="border-4 border-solid border-red-300">
                <h1>Response</h1>
                {/* <div> {loading ? "Loading" : JSON.stringify(data)}</div> */}
                <div> {loading ? "Loading" : JSON.stringify(error)}</div>
                <div  >

                    {data != null && data.constructor !== Array &&
                    <img src={`data:image/jpeg;base64,${data.image}`} alt="SpecificClass" />
            }

                </div>
            </div>

        </div>

    );
}