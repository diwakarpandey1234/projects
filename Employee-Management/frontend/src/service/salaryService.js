import api from "./api";

export const getAllSalaries = async () => {
    const response = await api.get("/salary/get/All");
    return response.data;
};

export const addSalary = async (salaryData) => {
    const response = await api.post("/salary/add", salaryData);
    return response.data;
};

export const getSalary = async (id) => {
    const response = await api.get(`/salary/get/${id}`);
    return response.data;
};

export const deleteSalary = async (id) => {
    const response = await api.delete(`/salary/delete/${id}`);
    return response.data;
};


export const getMySalary = async () => {
    const response = await api.get("/salary/my");
    return response.data;
};