import api from "./api";

export const getAllAuditLogs = async () => {
    const response = await api.get("/audit");
    return response.data;
};

export const getAuditLogsByUsername = async (username) => {
    const response = await api.get(`/audit/user/${username}`);
    return response.data;
};

export const getAuditLogsByAction = async (action) => {
    const response = await api.get(`/audit/action/${action}`);
    return response.data;
};

export const getAuditLogsByEntity = async (entity) => {
    const response = await api.get(`/audit/entity/${entity}`);
    return response.data;
};

export const getAuditLogsByEntityAndId = async (entity, entityId) => {
    const response = await api.get(`/audit/entity/${entity}/${entityId}`);
    return response.data;
};