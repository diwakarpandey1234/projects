import api from "./api";

export const getPendingLeaves = async () => {
    const response = await api.get("/leave/pending");
    return response.data;
};

export const approveLeave = async (leaveId) => {
    const response = await api.put(`/leave/${leaveId}/approve`);
    return response.data;
};

export const rejectLeave = async (leaveId) => {
    const response = await api.put(`/leave/${leaveId}/reject`);
    return response.data;
};

export const getEmployeeLeaves = async (employeeId) => {
    const response = await api.get(
        `/leave/employee/${employeeId}`
    );
    return response.data;
};

export const getLeavesByStatus = async (status) => {
    const response = await api.get(
        `/leave/status/${status}`
    );
    return response.data;
};

export const getLeavesByType = async (type) => {
    const response = await api.get(
        `/leave/type/${type}`
    );
    return response.data;
};


export const getMyLeaves = async (employeeId) => {
    const response = await api.get(`/leave/employee/${employeeId}`);
    return response.data;
};

export const applyLeave = async (leaveData) => {
    const response = await api.post("/leave/apply", leaveData);
    return response.data;
};

export const cancelLeave = async (leaveId) => {
    const response = await api.put(`/leave/${leaveId}/cancel`);
    return response.data;
};
