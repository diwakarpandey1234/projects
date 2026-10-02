import { Navigate } from "react-router-dom";
import { useAuth } from "../context/AuthContext";

const RoleRoute = ({ role, children }) => {

    const { isAuthenticated, role: userRole } = useAuth();

    if (!isAuthenticated) {
        return <Navigate to="/" replace />;
    }

    if (userRole !== role) {
        return <Navigate to="/" replace />;
    }

    return children;
};

export default RoleRoute;