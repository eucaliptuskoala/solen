import api from "./AxiosConfig";

const BASE_API_URL = import.meta.env.VITE_BASE_API_URL;
const CATEGORY_BASE_URL = BASE_API_URL + "/categories";

const CategoryAPI = {
  getTree: () => api.get(CATEGORY_BASE_URL).then(res => res.data),
};

export default CategoryAPI;
