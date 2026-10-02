import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import { getAllDepartments } from "../../service/departmentService";
import DashboardLayout from "../../components/DashboardLayout";

const DepartmentsADMIN = () => {

    const [departments, setDepartments] = useState([]);
    const [loading, setLoading] = useState(true);

    const navigate = useNavigate();

    useEffect(() => {

        const loadDepartments = async () => {

            try {
                const data = await getAllDepartments();
                setDepartments(data);
            } catch (error) {
                console.error("Failed to load departments", error);
            } finally {
                setLoading(false);
            }

        };

        loadDepartments();

    }, []);

    return (
        <DashboardLayout role="ADMIN">
        <div className="page">

            <div className="page-header">

                <div>
                    <h1>Departments</h1>
                    <p>Manage company departments</p>
                </div>

                <button
                    className="primary-btn"
                    onClick={() =>
                        navigate("/admin/departments/add")
                    }
                >
                    + Add Department
                </button>

            </div>

            <div className="card">

                {loading ? (
                    <p>Loading departments...</p>
                ) : departments.length === 0 ? (
                    <p>No departments found.</p>
                ) : (

                    <table className="data-table">

                        <thead>
                            <tr>
                                <th>ID</th>
                                <th>Name</th>
                                <th>Location</th>
                            </tr>
                        </thead>

                        <tbody>
                            
                            {departments.map((department) => (

                                <tr key={department.id}>

                                    <td>{department.id}</td>
                                    <td>{department.name}</td>
                                    <td>{department.location}</td>

                                </tr>

                            ))}

                        </tbody>

                    </table>

                )}

            </div>

        </div>
      </DashboardLayout>
    );
};

export default DepartmentsADMIN;