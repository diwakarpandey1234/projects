import { useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import { useAuth } from "../context/AuthContext";

const RoleLogin = () => {
    const { role } = useParams();

    const navigate = useNavigate();
    const { login } = useAuth();

    const [username, setUsername] = useState("");
    const [password, setPassword] = useState("");

    const [error, setError] = useState("");
    const [loading, setLoading] = useState(false);

    const selectedRole = role.toUpperCase();

    const getRoleTitle = () => {
        if (selectedRole === "ADMIN") {
            return "Admin Panel";
        }

        if (selectedRole === "HR") {
            return "HR Panel";
        }

        return "Employee Panel";
    };

    const handleSubmit = async (e) => {
        e.preventDefault();

        setError("");
        setLoading(true);

        try {
            const data = await login(
                username,
                password,
                selectedRole
            );

            console.log("Login successful:", data);

            if (selectedRole === "ADMIN") {
                navigate("/admin");
            } else if (selectedRole === "HR") {
                navigate("/hr");
            } else {
                navigate("/employee");
            }

        } catch (err) {
            console.error(err);

            if (err.response?.status === 401) {
                setError("Invalid username, password or role.");
            } else {
                setError("Login failed. Please try again.");
            }
        } finally {
            setLoading(false);
        }
    };

    return (
        <div className="login-page">

            <div className="login-card">

                <button
                    className="back-button"
                    onClick={() => navigate("/")}
                >
                    ← Back
                </button>

                <h1>{getRoleTitle()}</h1>

                {/* <p>Login to continue</p> */}

                <form onSubmit={handleSubmit}>

                    <div className="form-group">
                        <label>Username</label>

                        <input
                            type="text"
                            value={username}
                            onChange={(e) =>
                                setUsername(e.target.value)
                            }
                            placeholder="Enter username"
                            required
                        />
                    </div>

                    <div className="form-group">
                        <label>Password</label>

                        <input
                            type="password"
                            value={password}
                            onChange={(e) =>
                                setPassword(e.target.value)
                            }
                            placeholder="Enter password"
                            required
                        />
                    </div>

                    {error && (
                        <p className="error-message">
                            {error}
                        </p>
                    )}

                    <button
                        type="submit"
                        disabled={loading}
                    >
                        {loading ? "Logging in..." : "Login"}
                    </button>

                </form>

            </div>

        </div>
    );
};

export default RoleLogin;