import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";

import DashboardLayout from "../../components/DashboardLayout";
import {
    getAllEmployees,
    deleteEmployee
} from "../../service/EmployeeService";

const EmployeesHR = () => {

    const navigate = useNavigate();

    const [employees, setEmployees] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState("");

    const loadEmployees = async () => {
        try {

            setLoading(true);
            setError("");

            const data = await getAllEmployees();

            setEmployees(data);

        } catch (error) {

            console.error(error);

            setError("Unable to load employees.");

        } finally {

            setLoading(false);
        }
    };

    useEffect(() => {
        loadEmployees();
    }, []);

    const handleDelete = async (id) => {

        const confirmed = window.confirm(
            "Are you sure you want to delete this employee?"
        );

        if (!confirmed) {
            return;
        }

        try {

            await deleteEmployee(id);

            setEmployees((previousEmployees) =>
                previousEmployees.filter(
                    (employee) => employee.id !== id
                )
            );

        } catch (error) {

            console.error(error);

            alert("Unable to delete employee.");
        }
    };

    return (
        <DashboardLayout role="HR">

            <div className="page-header">

                <div>
                    <h1>Employees</h1>
                    <p>Manage organization employees</p>
                </div>

                <button
                    onClick={() => navigate("/hr/employees/add")}
                >
                    Add Employee
                </button>

            </div>

            {loading && (
                <p>Loading employees...</p>
            )}

            {error && (
                <p>{error}</p>
            )}

            {!loading && !error && (
                <div className="employee-table-container">

                    <table className="employee-table">

                        <thead>
                            <tr>
                                <th>ID</th>
                                <th>Name</th>
                                <th>Email</th>
                                <th>Phone</th>
                                <th>Designation</th>
                                <th>Status</th>
                                <th>Department</th>
                                <th>Actions</th>
                            </tr>
                        </thead>

                        <tbody>

                            {employees.length === 0 ? (

                                <tr>
                                    <td
                                        colSpan="8"
                                        style={{
                                            textAlign: "center"
                                        }}
                                    >
                                        No employees found.
                                    </td>
                                </tr>

                            ) : (

                                employees.map((employee) => (

                                    <tr key={employee.id}>

                                        <td>
                                            {employee.id}
                                        </td>

                                        <td>
                                            {employee.firstName}{" "}
                                            {employee.lastName || ""}
                                        </td>

                                        <td>
                                            {employee.email}
                                        </td>

                                        <td>
                                            {employee.phone}
                                        </td>

                                        <td>
                                            {employee.designation}
                                        </td>

                                        <td>
                                            {employee.status}
                                        </td>

                                        <td>
                                            {employee.department?.name ||
                                                "N/A"}
                                        </td>

                                        <td>

                                            <button
                                                onClick={() =>
                                                    handleDelete(
                                                        employee.id
                                                    )
                                                }
                                            >
                                                Delete
                                            </button>

                                        </td>

                                    </tr>

                                ))
                            )}

                        </tbody>

                    </table>

                </div>
            )}

        </DashboardLayout>
    );
};

export default EmployeesHR;