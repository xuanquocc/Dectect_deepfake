import { useState, useEffect } from "react";
import http from "../utils/http";


const useFetch = (endpoint, query) => {
    const [data, setData] = useState([]);
    const [isLoading, setIsLoading] = useState(false);
    const [error, setError] = useState(null);

    // const axios = require('axios');

   
    const fetchData = async () => {
        setIsLoading(true)
        const res = await http.get("image")
        setData(res)
        setIsLoading(false)
    }
    useEffect(() => {
        const delay = 1000;
        const timer = setTimeout(() => {
            fetchData();
        }, delay);

        return () => clearTimeout(timer);

    }, []);

    const refetch = () => {
        setIsLoading(true);
        fetchData();
    }

    return { data, isLoading, error, refetch };
}

export default useFetch;