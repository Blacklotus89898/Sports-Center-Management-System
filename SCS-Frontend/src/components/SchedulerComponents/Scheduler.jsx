import React, { useState, useEffect } from "react";
import useFetch from "../../api/useFetch";

const API_URL = 'http://localhost:8080/';

export default function SpecificClass() {
  // State for the specific class
  const [specificClass, setSpecificClass] = useState({
    classId: -1,
    classType: '',
    year: new Date().getFullYear(),
    specificClassName: '',
    description: '',
    date: '',
    startTime: '',
    hourDuration: 0,
    maxCapacity: 0,
    currentCapacity: 0,
    registrationFee: 0,
    image: null,
  });

  // Update each field
  const handleChange = (event) => {
    if (event.target.name === "image") {
      const file = event.target.files[0];
      const reader = new FileReader();

      reader.onload = function (e) {
        let base64Image = e.target.result;
        base64Image = base64Image.split(',')[1];
        setSpecificClass({
          ...specificClass,
          ["image"]: base64Image,
        });
      };

      reader.readAsDataURL(file);
    } else {
      setSpecificClass({
        ...specificClass,
        [event.target.name]: event.target.value,
      });
    }
  };

  // Fetch specific classes on load
  useEffect(() => {
    getSpecificClasses();
  }, []);

  // useFetch hook
  const { data, loading, error, fetchData } = useFetch();

  // Get all specific classes
  const getSpecificClasses = async () => {
    await fetchData(`${API_URL}specificClasses`, {});
    console.log(data);
  };

  // Get specific class by ID
  const getSpecificClassById = async () => {
    console.log(specificClass.classId);
    await fetchData(`${API_URL}specificClasses/${specificClass.classId}`, {});
  };

  // Create a specific class
  const createSpecificClass = async (event) => {
    event.preventDefault();
    await fetchData(`${API_URL}specificClasses`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(specificClass),
    });
    if (error != null) console.log(error);
  };

  // Delete a specific class by ID
  const deleteSpecificClassById = async () => {
    console.log("Deleting", specificClass.classId);
    await fetchData(`${API_URL}specificClasses/${specificClass.classId}`, {
      method: 'DELETE',
      headers: {
        'Content-Type': 'application/json',
      },
    });
  };

  // Update a specific class by ID
  const updateSpecificClassById = async (event) => {
    event.preventDefault();
    await fetchData(`${API_URL}specificClasses/${specificClass.classId}`, {
      method: 'PUT',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(specificClass),
    });
  };

  return (
    <div className="border border-solid-black w-80">
      <h1>Specific Class Component</h1>
      <button onClick={getSpecificClasses} className="border-4 border-blue-400">
        Get All
      </button>
      <input
        type="number"
        name="classId"
        value={specificClass.classId}
        onChange={handleChange}
        placeholder="Enter Class ID"
      />
      <button onClick={getSpecificClassById} className="border-4 border-green-500">
        Find By ID
      </button>
      <button onClick={deleteSpecificClassById} className="border-4 border-red-500">
        Delete By ID
      </button>

      {/* Create Specific Class */}
      <form onSubmit={createSpecificClass} className="border-4 border-solid border-green-300">
        CREATE
        <input
          type="text"
          name="classType"
          value={specificClass.classType}
          onChange={handleChange}
          placeholder="Class Type"
          required
        />
        <input
          type="number"
          name="year"
          value={specificClass.year}
          onChange={handleChange}
          placeholder="Year"
          required
        />
        <input
          type="text"
          name="specificClassName"
          value={specificClass.specificClassName}
          onChange={handleChange}
          placeholder="Specific Class Name"
          required
        />
        <input
          type="text"
          name="description"
          value={specificClass.description}
          onChange={handleChange}
          placeholder="Description"
          required
        />
        <input
          type="date"
          name="date"
          value={specificClass.date}
          onChange={handleChange}
          placeholder="Date"
          required
        />
        <input
          type="time"
          name="startTime"
          value={specificClass.startTime}
          onChange={handleChange}
          placeholder="Start Time"
          required
        />
        <input
          type="number"
          name="hourDuration"
          value={specificClass.hourDuration}
          onChange={handleChange}
          placeholder="Hour Duration"
          required
        />
        <input
          type="number"
          name="maxCapacity"
          value={specificClass.maxCapacity}
          onChange={handleChange}
          placeholder="Max Capacity"
          required
        />
        <input
          type="number"
          name="currentCapacity"
          value={specificClass.currentCapacity}
          onChange={handleChange}
          placeholder="Current Capacity"
          required
        />
        <input
          type="number"
          name="registrationFee"
          value={specificClass.registrationFee}
          onChange={handleChange}
          placeholder="Registration Fee"
          required
        />
        <input
          type="file"
          name="image"
          onChange={handleChange}
          placeholder="Drop Image"
        />
        <button type="submit">Create Specific Class</button>
      </form>

      {/* Update Specific Class */}
      <form onSubmit={updateSpecificClassById} className="border-4 border-solid border-orange-300">
        UPDATE
        <input
          type="text"
          name="classType"
          value={specificClass.classType}
          onChange={handleChange}
          placeholder="Class Type"
          required
        />
        <input
          type="number"
          name="year"
          value={specificClass.year}
          onChange={handleChange}
          placeholder="Year"
          required
        />
        <input
          type="text"
          name="specificClassName"
          value={specificClass.specificClassName}
          onChange={handleChange}
          placeholder="Specific Class Name"
          required
        />
        <input
          type="text"
          name="description"
          value={specificClass.description}
          onChange={handleChange}
          placeholder="Description"
          required
        />
        <input
          type="date"
          name="date"
          value={specificClass.date}
          onChange={handleChange}
          placeholder="Date"
          required
        />
        <input
          type="time"
          name="startTime"
          value={specificClass.startTime}
          onChange={handleChange}
          placeholder="Start Time"
          required
        />
        <input
          type="number"
          name="hourDuration"
          value={specificClass.hourDuration}
          onChange={handleChange}
          placeholder="Hour Duration"
          required
        />
        <input
          type="number"
          name="maxCapacity"
          value={specificClass.maxCapacity}
          onChange={handleChange}
          placeholder="Max Capacity"
          required
        />
        <input
          type="number"
          name="currentCapacity"
          value={specificClass.currentCapacity}
          onChange={handleChange}
          placeholder="Current Capacity"
          required
        />
        <input
          type="number"
          name="registrationFee"
          value={specificClass.registrationFee}
          onChange={handleChange}
          placeholder="Registration Fee"
          required
        />
        <button type="submit">Update Specific Class</button>
      </form>

      <div className="border-4 border-solid border-red-300">
        <h1>Response</h1>
        <div>{loading ? "Loading" : JSON.stringify(data)}</div>
        <div>{loading ? "Loading" : JSON.stringify(error)}</div>
        <div>
          {data != null && data.constructor !== Array && (
            <img src={`data:image/jpeg;base64,${data.image}`} alt="Specific Class" />
          )}
        </div>
      </div>
    </div>
  );
}