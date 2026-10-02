import { createContext, useContext, useState } from "react";
import { login as loginRequest } from "../service/authService";

const AuthContext = createContext(null);

export const AuthProvider = ({ children }) => {

    const [accessToken, setAccessToken] = useState(
        localStorage.getItem("accessToken")
    );

    const [refreshToken, setRefreshToken] = useState(
        localStorage.getItem("refreshToken")
    );

    const [role, setRole] = useState(
        localStorage.getItem("role")
    );

    const login = async (username, password, role) => {

        const data = await loginRequest(
            username,
            password,
            role
        );

        localStorage.setItem(
            "accessToken",
            data.accessToken
        );

        localStorage.setItem(
            "refreshToken",
            data.refreshToken
        );

        localStorage.setItem(
            "role",
            role
        );

        setAccessToken(data.accessToken);
        setRefreshToken(data.refreshToken);
        setRole(role);

        return data;
    };

    const logout = () => {

        localStorage.removeItem("accessToken");
        localStorage.removeItem("refreshToken");
        localStorage.removeItem("role");

        setAccessToken(null);
        setRefreshToken(null);
        setRole(null);
    };

    const isAuthenticated = !!accessToken;

    return (
        <AuthContext.Provider
            value={{
                accessToken,
                refreshToken,
                role,
                isAuthenticated,
                login,
                logout
            }}
        >
            {children}
        </AuthContext.Provider>
    );
};

export const useAuth = () => {
    return useContext(AuthContext);
};