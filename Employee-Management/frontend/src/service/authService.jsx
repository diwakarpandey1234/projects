import api from "./api";

export const login = async (username, password, role) => {
    const response = await api.post("/authenticate", {
        username,
        password,
        role
    });

    return response.data;
};

export const refreshToken = async (refreshToken) => {
    const response = await api.post("/refresh", {
        refreshToken
    });

    return response.data;
};