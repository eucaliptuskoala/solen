import api from "./AxiosConfig";

const BASE_API_URL = import.meta.env.VITE_BASE_API_URL;
const PRACTICE_BASE_URL = BASE_API_URL + "/practices";

const PracticeAPI = {
  createPractice: (practice) => api.post(PRACTICE_BASE_URL, practice).then(res => res.data),
  getPracticesByUser: () => api.get(PRACTICE_BASE_URL + "/my").then(res => res.data),
  deletePractice: (id) => api.delete(`${PRACTICE_BASE_URL}/${id}`).then(res => res.data),
  updateStreak: (id) => api.put(`${PRACTICE_BASE_URL}/${id}`).then(res => res.data),
};

export default PracticeAPI;
