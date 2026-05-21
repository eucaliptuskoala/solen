import api from "./AxiosConfig";

const BASE_API_URL = import.meta.env.VITE_BASE_API_URL;
const CHECKIN_BASE_URL = BASE_API_URL + "/checkins";

const CheckInAPI = {
  getAll: (from, to) => {
    const params = {};
    if (from) params.from = from;
    if (to) params.to = to;
    return api.get(CHECKIN_BASE_URL, { params }).then(res => res.data);
  },
  create: (request) => api.post(CHECKIN_BASE_URL + "/checkin", request).then(res => res.data),
  update: (id, request) => api.put(`${CHECKIN_BASE_URL}/${id}`, request).then(res => res.data),
  delete: (id) => api.delete(`${CHECKIN_BASE_URL}/${id}`).then(res => res.data),
  getFyp: () => api.get(CHECKIN_BASE_URL + "/fyp").then(res => res.data),
};

export default CheckInAPI;
