import { useState } from "react";
import { useNavigate } from "react-router-dom";
import api from "../../service/api";
import DashboardLayout from "../../components/DashboardLayout";

const AddDepartmentsADMIN = () => {

    const navigate = useNavigate();

    const [form, setForm] = useState({
        name: "",
        location: ""
    });

    const [loading, setLoading] = useState(false);

    const handleChange = (e) => {

        setForm({
            ...form,
            [e.target.name]: e.target.value
        });

    };

    const handleSubmit = async (e) => {

        e.preventDefault();

        try {

            setLoading(true);

            await api.post("/departments/add", form);

            navigate("/admin/departments");

        } catch (error) {

            console.error("Failed to add department", error);

            alert(
                error.response?.data?.message ||
                "Failed to add department"
            );

        } finally {
            setLoading(false);
        }
    };

    return (
        <DashboardLayout role="ADMIN">
        <div className="page">

            <div className="page-header">

                <div>
                    <h1>Add Department</h1>
                    <p>Create a new department</p>
                </div>

            </div>

            <div className="card">

                <form onSubmit={handleSubmit}>

                    <div className="form-group">

                        <label>Department Name</label>

                        <input
                            type="text"
                            name="name"
                            value={form.name}
                            onChange={handleChange}
                            placeholder="Enter department name"
                            required
                        />

                    </div>

                    <div className="form-group">

                        <label>Location</label>

                        <input
                            type="text"
                            name="location"
                            value={form.location}
                            onChange={handleChange}
                            placeholder="Enter location"
                            required
                        />

                    </div>

                    <button
                        type="submit"
                        className="primary-btn"
                        disabled={loading}
                    >
                        {loading ? "Adding..." : "Add Department"}
                    </button>

                </form>

            </div>

        </div>
      </DashboardLayout>
    );
};

export default AddDepartmentsADMIN;