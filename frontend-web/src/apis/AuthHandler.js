import {jwtDecode} from "jwt-decode";

const AuthHandler = {

    saveToken: (token) => {
        localStorage.setItem("jwt", token);
    },

    getToken: () => {
        return localStorage.getItem("jwt");
    },

    clearToken: () => {
        localStorage.removeItem("jwt");
    },

    getUserId: () => {
        const token = localStorage.getItem("jwt");
        if (!token) return null;
        return jwtDecode(token).userId;
    },

    getName: () => {
        const token = localStorage.getItem("jwt");
        if (!token) return null;
        return jwtDecode(token).name;
    },

    tokenExists: () => !!localStorage.getItem("jwt"),
}

export default AuthHandler;