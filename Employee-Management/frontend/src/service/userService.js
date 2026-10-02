import api from "./api";

export const getCurrentUser = async () => {
    const response = await api.get("/api/user/me");
    return response.data;
};

export const getHRUsers = async () => {
    const response = await api.get("/api/user/hr");
    return response.data;
};