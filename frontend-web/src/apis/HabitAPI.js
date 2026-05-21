import api from "./AxiosConfig";

const BASE_API_URL = import.meta.env.VITE_BASE_API_URL;
const HABIT_BASE_URL = BASE_API_URL + "/habits";

const HabitAPI = {
  createHabit: (habit) => api.post(HABIT_BASE_URL, habit).then(res => res.data),
  getHabitsByUser: () => api.get(HABIT_BASE_URL + "/my").then(res => res.data),
  deleteHabit: (id) => api.delete(`${HABIT_BASE_URL}/${id}`).then(res => res.data),
  updateStreak: (id) => api.put(`${HABIT_BASE_URL}/${id}`).then(res => res.data),
};

export default HabitAPI;
