import { useNavigate } from "react-router-dom";
import { useAuth } from "../context/AuthContext";

const Navbar = ({ role }) => {
    const { logout } = useAuth();
    const navigate = useNavigate();

    const handleLogout = () => {
        logout();
        navigate("/");
    };

    return (
        <header className="navbar">
            <div>
                <h2>Employee Management System</h2>
            </div>

            <div className="navbar-right">
                <span className="role-badge">
                    {role}
                </span>

                <button onClick={handleLogout}>
                    Logout
                </button>
            </div>
        </header>
    );
};

export default Navbar;