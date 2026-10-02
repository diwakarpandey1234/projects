import { useNavigate } from "react-router-dom";

const roles = [
    {
        role: "ADMIN",
        title: "Admin Panel",
       // description: "Manage employees, departments, users, salaries and system activities.",
    },
    {
        role: "HR",
        title: "HR Panel",
       // description: "Manage employees, attendance, salaries and employee leaves.",
    },
    {
        role: "USER",
        title: "Employee Panel",
        //description: "View your profile, attendance, salary and manage your leaves.",
    },
];

const Landing = () => {
    const navigate = useNavigate();

    const handleRoleSelect = (role) => {
        navigate(`/login/${role.toLowerCase()}`);
    };

    return (
        <div className="landing-page">
            <div className="landing-header">
                <h1>Employee Management System</h1>
                
            </div>

            <div className="role-container">
                {roles.map((item) => (
                    <div className="role-card" key={item.role}>
                        <h2>{item.title}</h2>

                        <p>{item.description}</p>

                        <button id="button"
                            onClick={() => handleRoleSelect(item.role)}
                        >
                            Login
                        </button>
                    </div>
                ))}
            </div>
        </div>
    );
};

export default Landing;