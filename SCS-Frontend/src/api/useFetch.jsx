import { useState } from 'react';

function useFetch() {
    const initialData = null;
    const initialLoading = true;
    const initialError = null;

    const [data, setData] = useState(initialData);
    const [loading, setLoading] = useState(initialLoading);
    const [error, setError] = useState(initialError);

    const fetchData = async (url, options, callback = () => {}) => {
        setLoading(true);
        try {
            const response = await fetch(url, options);
            const data = await response.json(); 
            
            if (!response.ok) {
                setData(data); // Assuming error details are in the data
                callback(null);
                throw new Error(response.status);
            }

            setData(data);
            setError(null);
            callback(data); // Execute callback with fetched data
        } catch (error) {
            setError(error.toString());
        } finally {
            setLoading(false);
        }
    };

    const reset = () => {
        setData(initialData);
        setLoading(initialLoading);
        setError(initialError);
    };

    return { data, loading, error, fetchData, reset };
}

export default useFetch;