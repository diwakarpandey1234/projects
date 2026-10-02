import api from "./api";

export const getAllEmployees = async () => {
    const response = await api.get("/Employee/get/Allemployees");
    return response.data;
};

export const createEmployee = async (employeeData) => {
    const response = await api.post(
        "/Employee/create",
        employeeData
    );

    return response.data;
};

export const deleteEmployee = async (id) => {
    const response = await api.delete(
        `/Employee/delete/${id}`
    );

    return response.data;
};