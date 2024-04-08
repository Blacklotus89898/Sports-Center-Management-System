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
            let data = null;

            try {
                data = await response.json();
            } catch (e) { console.error("error in useFetch:", e); } 

            if (response.status === 500) {
                setData(response);
                callback(null);
                throw new Error(response.status);
            }

            if (response.status === 400 && data && !data.errors) {
                setData(response);
                callback(null);
                throw new Error(response.status);
            }
            
            if (data && data.errors) {
                setData(data);
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