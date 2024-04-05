import { useState } from 'react';

function useFetch() {
    const [data, setData] = useState(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);


        const fetchData = async (url, options) => {
            setLoading(true);
            try {
                const response = await fetch(url, options);
                if (!response.ok) {
                    // setError(error);
                    throw new Error(response.status); //slow
                } 

                    const data = await response.json(); //error test is returned inside the resonse
                    setData(data);
                    setError(null);
                
            } catch (error) {
                // console.error(error); // Print the error to the console
                setError(error.toString());
                setData(null);
            } finally {
                setLoading(false);
            }
        };


    return { data, loading, error, fetchData };
}

export default useFetch;