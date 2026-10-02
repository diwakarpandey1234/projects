import api from "./api";

export const getAllDepartments = async () => {
    const response = await api.get("/departments/get/all");
    return response.data;
};