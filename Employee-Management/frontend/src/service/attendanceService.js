import api from "./api";

export const checkIn = async (employeeId) => {
    const response = await api.post(
        `/attendance/check-in?employeeId=${employeeId}`
    );
    return response.data;
};

export const checkOut = async (employeeId) => {
    const response = await api.post(
        `/attendance/check-out?employeeId=${employeeId}`
    );
    return response.data;
};

export const getAllAttendance = async () => {
    const response = await api.get("/attendance/all");
    return response.data;
};

export const getEmployeeAttendance = async (employeeId) => {
    const response = await api.get(
        `/attendance/employee/${employeeId}`
    );
    return response.data;
};

export const getMonthlyAttendance = async (
    employeeId,
    month,
    year
) => {
    const response = await api.get(
        `/attendance/monthly?employeeId=${employeeId}&month=${month}&year=${year}`
    );
    return response.data;
};