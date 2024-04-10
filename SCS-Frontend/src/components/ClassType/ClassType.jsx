import React, { useState, useEffect } from "react";
// import { classTypeService } from "../api/classTypeService"; //uncomment if you use service isntead of hooks
import useFetch from "../../api/useFetch";

const API_URL = 'http://localhost:8080/';

export default function ClassType() {

    //states
    const [classType, setclassType] = useState({
        id: -1,
        className: '',
        description: '',
        isApproved: true,
        icon: '',
    });

    //update of each field
    const handleChange = async (event) => {
        // if (event.target.name === "image") {
        //     const file = event.target.files[0];
        //     const reader = new FileReader();
            
        //     reader.onload = function(e) {
        //         let base64Image = e.target.result;
        //         base64Image = base64Image.split(',')[1]; // Remove the data type from the Base64 string
        //       setclassType({
        //         ...classType,
        //         ["image"]: base64Image // Remove the data type from the Base64 string
        //       });
        //     }
          
        //     reader.readAsDataURL(file);
        //   }
        //      else {
                setclassType({
                    ...classType,
                    [event.target.name]: event.target.value
                });
            // }
        }

        //fetch classTypes on load
        useEffect(() => {
            getclassTypes();
        }, []);


        // useFetch hook
        // data: return value from the fetch
        // loading: use to render once loading stops
        // error: string
        // fetchData(url: string, options: {method: string, headers: {Content-Type: string}, body: string })
        const { data, loading, error, fetchData } = useFetch();  //instantiates the hook

        const getclassTypes = async () => {
            await fetchData(`${API_URL}classTypes`, {});
            console.log(data);
        }

        const getclassTypeByClassName = async () => {
            console.log(classType.className);
            await fetchData(`${API_URL}classType/${classType.className}`, {}); //make base URL a constant
        }

        const createClassType = async (event) => {
            event.preventDefault(); //prevents reload //will be changed later
            // console.log(classType);
            await fetchData(`${API_URL}classType`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(classType)
            });
            if (error != null) console.log(error);
        }

        const deleteclassTypeByClassName = async () => {
            console.log("deleting", classType.className);    //for testing
            await fetchData(`${API_URL}classType/${classType.className}`, {
                method: 'DELETE',
                headers: {
                    'Content-Type': 'application/json'
                },
            });
        }

        const updateclassTypeById = async (event) => {
            event.preventDefault();
            await fetchData(`${API_URL}classType/${classType.id}`, {
                method: 'PUT',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(classType)
            });
        }


        return (
            <div className="border border-solid-black w-80">
                <h1>Mock Component</h1>
                <button onClick={getclassTypes} className="border-4 border-blue-400">Get All</button>
                <input type="text" name="className" id="" value={classType.className} onChange={handleChange} placeholder="Enter id" />
                <button onClick={getclassTypeByClassName} className="border-4 border-green-500">Find By ClassName</button>
                <button onClick={deleteclassTypeByClassName} className="border-4 border-red-500">Delete By ClassName</button>


                {/* Method 2 */}
                <form onSubmit={createClassType} className="border-4 border-solid border-green-300"> CREATE
                    <input type="text" name="className" value={classType.className} onChange={handleChange} placeholder="Name" required />
                    <input type="text" name="description" value={classType.description} onChange={handleChange} placeholder="Description" required />
                    <input type="text" name="icon" value={classType.icon} onChange={handleChange} placeholder="Icon" />
                    <input type="checkbox" name="isApproved" value={classType.isApproved} onChange={handleChange} placeholder="Is approved" />
                    <button type="submit">Create classType</button>
                </form>

                {/* Update */}
                {/* <form onSubmit={updateclassTypeById} className="border-4 border-solid border-orange-300"> UPDATE
                    <input type="text" name="name" value={classType.name} onChange={handleChange} placeholder="Name" required />
                    <input type="email" name="email" value={classType.email} onChange={handleChange} placeholder="Email" required />
                    <input type="password" name="password" value={classType.password} onChange={handleChange} placeholder="Password" required />
                    <button type="submit">Update classType</button>
                </form> */}
                <h1>Object in form</h1>
                {Object.keys(classType).map(function (key) { return <div key={key}>Key: {key}, Value: {classType[key]}</div>; })}
                <div className="border-4 border-solid border-red-300">
                    <h1>Response</h1>
                    <div> {loading ? "Loading" : JSON.stringify(data)}</div>
                    <div> {loading ? "Loading" : JSON.stringify(error)}</div>
                    <div  >


                    </div>
                </div>

            </div>

        );
    }