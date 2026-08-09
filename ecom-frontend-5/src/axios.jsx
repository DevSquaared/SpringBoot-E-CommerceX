import axios from "axios";
import { API_BASE_URL } from "./api/config";

const API = axios.create({
  baseURL: API_BASE_URL,
});
delete API.defaults.headers.common["Authorization"];
export default API;