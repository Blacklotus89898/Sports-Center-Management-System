import { useState } from 'react';

function useFetch() {
    const [data, setData] = useState(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);


        const fetchData = async (url, options) => {
            setLoading(true);
            try {
                const response = await fetch(url, options);
                const data = await response.json(); 
                if (!response.ok) {
                    // setError(error)
                    setData(data); //error test is returned inside the response data
                    throw new Error(response.status); 
                } 

                    
                    setData(data);
                    setError(null);
            } catch (error) {
                setError(error.toString());
            } finally {
                setLoading(false);
            }
        };

    return { data, loading, error, fetchData };
}

export default useFetch;