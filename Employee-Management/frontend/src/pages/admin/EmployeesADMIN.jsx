import { useEffect, useMemo, useState } from "react";
import DashboardLayout from "../../components/DashboardLayout";
import {
    getAllEmployees,
    deleteEmployee
} from "../../service/employeeService";

const EmployeesADMIN = () => {

    const [employees, setEmployees] = useState([]);
    const [search, setSearch] = useState("");
    const [departmentFilter, setDepartmentFilter] = useState("ALL");

    const [loading, setLoading] = useState(true);
    const [error, setError] = useState("");

    useEffect(() => {
        loadEmployees();
    }, []);

    const loadEmployees = async () => {
        try {
            setLoading(true);
            setError("");

            const data = await getAllEmployees();

            setEmployees(Array.isArray(data) ? data : []);

        } catch (error) {
            console.error(error);
            setError("Unable to load employees.");
        } finally {
            setLoading(false);
        }
    };

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

    const departments = useMemo(() => {

        const uniqueDepartments = employees
            .map((employee) => employee.departmentName)
            .filter(Boolean);

        return [...new Set(uniqueDepartments)];

    }, [employees]);

    const filteredEmployees = useMemo(() => {

        const searchValue = search.toLowerCase().trim();

        return employees.filter((employee) => {

            const fullName =
                `${employee.firstName || ""} ${employee.lastName || ""}`
                    .toLowerCase();

            const email =
                (employee.email || "").toLowerCase();

            const designation =
                (employee.designation || "").toLowerCase();

            const department =
                (employee.departmentName || "").toLowerCase();

            const matchesSearch =
                fullName.includes(searchValue) ||
                email.includes(searchValue) ||
                designation.includes(searchValue) ||
                department.includes(searchValue);

                
            const matchesDepartment =
                departmentFilter === "ALL" ||
                employee.departmentName === departmentFilter;

            return matchesSearch && matchesDepartment;
        });

    }, [employees, search, departmentFilter]);

    return (
        <DashboardLayout role="ADMIN">

            <div className="page-header">
                <div>
                    <h1>Employees</h1>
                    <p>View and manage organization employees</p>
                </div>
            </div>

            <div className="card">

                <div className="employee-toolbar">

                    <div className="search-box">
                        <input
                            type="text"
                            placeholder="Search by name, email, designation..."
                            value={search}
                            onChange={(e) =>
                                setSearch(e.target.value)
                            }
                        />
                    </div>

                    <div className="filter-box">

                        <select
                            value={departmentFilter}
                            onChange={(e) =>
                                setDepartmentFilter(e.target.value)
                            }
                        >
                            <option value="ALL">
                                All Departments
                            </option>

                            {departments.map((department) => (
                                <option
                                    key={department}
                                    value={department}
                                >
                                    {department}
                                </option>
                            ))}
                        </select>

                    </div>

                </div>

                {loading && (
                    <p>Loading employees...</p>
                )}

                {error && (
                    <p className="error-message">
                        {error}
                    </p>
                )}

                {!loading && !error && (
                    <div className="table-container">

                        <table className="data-table">

                            <thead>
                                <tr>
                                    <th>ID</th>
                                    <th>Name</th>
                                    <th>Email</th>
                                    <th>Department</th>
                                    <th>Designation</th>
                                    <th>Status</th>
                                    <th>Action</th>
                                </tr>
                            </thead>

                            <tbody>

                                {filteredEmployees.length === 0 ? (

                                    <tr>
                                        <td colSpan="7">
                                            No employees found.
                                        </td>
                                    </tr>

                                ) : (

                                    filteredEmployees.map((employee) => (

                                        <tr key={employee.id}>

                                            <td>
                                                {employee.id}
                                            </td>

                                            <td>
                                                {employee.firstName}{" "}
                                                {employee.lastName || ""}
                                            </td>

                                            <td>
                                                {employee.email || "--"}
                                            </td>

                                            <td>
                                                {employee.departmentName || "--"}
                                            </td>

                                            <td>
                                                {employee.designation || "--"}
                                            </td>

                                            <td>
                                                {employee.status || "--"}
                                            </td>

                                            <td>

                                                <button
                                                    className="delete-button"
                                                    onClick={() =>
                                                        handleDelete(employee.id)
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

            </div>

        </DashboardLayout>
    );
};

export default EmployeesADMIN;