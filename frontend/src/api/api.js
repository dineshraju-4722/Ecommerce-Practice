import axios from "axios";
 const api = axios.create({
    // baseURL : `${import.meta.env.BACKEND_URL}/api`,
    baseURL : `http://localhost:4722/api`,
    // withCredentials : true ,
})
export default api;