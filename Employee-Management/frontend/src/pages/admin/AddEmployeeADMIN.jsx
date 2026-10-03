import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";

import DashboardLayout from "../../components/DashboardLayout";
import { registerUser } from "../../service/registerUser";
import { getAllDepartments } from "../../service/departmentService";

const AddEmployeeADMIN = () => {

    const navigate = useNavigate();

    const [departments, setDepartments] = useState([]);

    const [formData, setFormData] = useState({
        username:"",
        password:"",
        firstName: "",
        lastName: "",
        email: "",
        phone: "",
        designation: "",
        joiningDate: "",
        role:"USER",
        status: "ACTIVE",
        departmentName: ""
    });

    const [loading, setLoading] = useState(false);
    const [error, setError] = useState("");

    useEffect(() => {

        const loadDepartments = async () => {
            try {
                const data = await getAllDepartments();
                setDepartments(data);
            } catch (error) {
                console.error(error);
                setError("Unable to load departments.");
            }
        };

        loadDepartments();

    }, []);

    const handleChange = (event) => {

        const { name, value } = event.target;

        setFormData((previous) => ({
            ...previous,
            [name]: value
        }));
    };

    const handleSubmit = async (event) => {

        event.preventDefault();

        setError("");
        setLoading(true);

        try {

            console.log(formData);
            await registerUser(formData);
                
                // departmentId: Number(formData.departmentId)
            navigate("/admin/employees/add");

        } catch (error) {

            console.error(error);

            setError(
                error.response?.data?.message ||
                "Unable to create employee."
            );

        } finally {

            setLoading(false);
        }
    };

    return (
        <DashboardLayout role="ADMIN">

            <div className="page-header">

                <div>
                    <h1>Add Employee</h1>
                    {/* <p>Create a new employee record</p> */}
                </div>

            </div>

            <form
                className="employee-form"
                onSubmit={handleSubmit}
            >
                <div className="form-group">
                    <label>Username</label>

                    <input
                        type="text"
                        name="username"
                        value={formData.username}
                        onChange={handleChange}
                        required
                    />
                </div>

                <div className="form-group">
                    <label>Password</label>

                    <input
                        type="text"
                        name="password"
                        value={formData.password}
                        onChange={handleChange}
                        required
                    />
                </div>


                <div className="form-group">
                    <label>First Name</label>

                    <input
                        type="text"
                        name="firstName"
                        value={formData.firstName}
                        onChange={handleChange}
                        required
                    />
                </div>

                <div className="form-group">
                    <label>Last Name</label>

                    <input
                        type="text"
                        name="lastName"
                        value={formData.lastName}
                        onChange={handleChange}
                    />
                </div>

                <div className="form-group">
                    <label>Email</label>

                    <input
                        type="email"
                        name="email"
                        value={formData.email}
                        onChange={handleChange}
                        required
                    />
                </div>

                <div className="form-group">
                    <label>Phone</label>

                    <input
                        type="text"
                        name="phone"
                        value={formData.phone}
                        onChange={handleChange}
                        required
                    />
                </div>

                <div className="form-group">
                    <label>Designation</label>

                    <input
                        type="text"
                        name="designation"
                        value={formData.designation}
                        onChange={handleChange}
                        required
                    />
                </div>

                <div className="form-group">
                    <label>Joining Date</label>

                    <input
                        type="date"
                        name="joiningDate"
                        value={formData.joiningDate}
                        onChange={handleChange}
                        required
                    />
                </div>

                {/* <div className="form-group">
                    <label>Role</label>

                    <select
                        name="role"
                        value={formData.role}
                        onChange={handleChange}
                    >
                        <option value="HR">HR</option>
                        <option value="ADMIN">ADMIN</option>
                    </select>
                </div> */}

                <div className="form-group">
                    <label>Status</label>

                    <select
                        name="status"
                        value={formData.status}
                        onChange={handleChange}
                    >
                        <option value="ACTIVE">Active</option>
                        <option value="INACTIVE">Inactive</option>
                    </select>
                </div>

                <div className="form-group">
                    <label>Department</label>

                    <select
                        name="departmentName"
                        value={formData.departmentName}
                        onChange={handleChange}
                        required
                    >
                        <option value="">
                            Select Department
                        </option>

                        {departments.map((department) => (
                            <option
                                key={department.id}
                                value={department.name}
                            >
                                {department.name}
                            </option>
                        ))}
                    </select>
                </div>

                {error && (
                    <p className="error-message">
                        {error}
                    </p>
                )}

                <div className="form-actions">

                    <button
                        type="button"
                        onClick={() =>
                            navigate("/admin/employees/add")
                        }
                    >
                        Cancel
                    </button>

                    <button
                        type="submit"
                        disabled={loading}
                    >
                        {loading
                            ? "Creating..."
                            : "Create Employee"}
                    </button>

                </div>

            </form>

        </DashboardLayout>
    );
};

export default AddEmployeeADMIN;