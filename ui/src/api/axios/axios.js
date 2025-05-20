// src/axios.js
import axios from 'axios';

// Create an instance of axios with a default base URL
const axiosInstance = axios.create({
    baseURL: 'http://localhost:8080/api/', // Your base API URL
    timeout: 10000, // Optional timeout setting
});

export default axiosInstance;
