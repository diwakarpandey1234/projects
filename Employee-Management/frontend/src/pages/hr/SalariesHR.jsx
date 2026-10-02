import { useEffect, useState } from "react";

import DashboardLayout from "../../components/DashboardLayout";

import {
    getAllSalaries,
    addSalary,
    deleteSalary
} from "../../service/salaryService";

import { getAllEmployees } from "../../service/EmployeeService";

const SalariesHR = () => {

    const [employees, setEmployees] = useState([]);
    const [salaries, setSalaries] = useState([]);

    const [formData, setFormData] = useState({
        employeeId: "",
        basic: "",
        bonus: "",
        deduction: "",
        effectiveDate: ""
    });

    const [loading, setLoading] = useState(false);
    const [error, setError] = useState("");
    const [success, setSuccess] = useState("");

    useEffect(() => {
        loadData();
    }, []);

    const loadData = async () => {

        try {

            setLoading(true);
            setError("");

            const [employeeData, salaryData] =
                await Promise.all([
                    getAllEmployees(),
                    getAllSalaries()
                ]);

            setEmployees(employeeData);
            setSalaries(salaryData);

        } catch (err) {

            console.error(err);

            setError("Failed to load salary data.");

        } finally {

            setLoading(false);

        }
    };

    const handleChange = (e) => {

        const { name, value } = e.target;

        setFormData({
            ...formData,
            [name]: value
        });
    };

    const calculateNetSalary = () => {

        const basic = Number(formData.basic) || 0;
        const bonus = Number(formData.bonus) || 0;
        const deduction = Number(formData.deduction) || 0;

        return basic + bonus - deduction;
    };

    const handleSubmit = async (e) => {

        e.preventDefault();

        setError("");
        setSuccess("");

        if (!formData.employeeId) {
            setError("Please select an employee.");
            return;
        }

        try {

            setLoading(true);

            const salaryData = {
                employeeId: Number(formData.employeeId),
                basic: Number(formData.basic),
                bonus: Number(formData.bonus),
                deduction: Number(formData.deduction),
                netSalary: calculateNetSalary(),
                effectiveDate: formData.effectiveDate
            };

            await addSalary(salaryData);

            setSuccess("Salary added successfully.");

            setFormData({
                employeeId: "",
                basic: "",
                bonus: "",
                deduction: "",
                effectiveDate: ""
            });

            const data = await getAllSalaries();

            setSalaries(data);

        } catch (err) {

            console.error(err);

            setError(
                err.response?.data ||
                "Failed to add salary."
            );

        } finally {

            setLoading(false);

        }
    };

    const handleDelete = async (id) => {

        const confirmed = window.confirm(
            "Are you sure you want to delete this salary record?"
        );

        if (!confirmed) {
            return;
        }

        try {

            setLoading(true);
            setError("");
            setSuccess("");

            await deleteSalary(id);

            setSuccess("Salary deleted successfully.");

            setSalaries(
                salaries.filter(
                    (salary) => salary.id !== id
                )
            );

        } catch (err) {

            console.error(err);

            setError(
                err.response?.data ||
                "Failed to delete salary."
            );

        } finally {

            setLoading(false);

        }
    };

    const getEmployeeName = (employeeId) => {

        const employee = employees.find(
            (item) => item.id === employeeId
        );

        if (!employee) {
            return `Employee #${employeeId}`;
        }

        return `${employee.firstName} ${employee.lastName || ""}`;
    };

    return (
        <DashboardLayout role="HR">

            <div className="page-header">

                <div>
                    <h1>Salary Management</h1>
                    <p>
                        Manage employee salary records
                    </p>
                </div>

            </div>

            {error && (
                <div className="error-message">
                    {error}
                </div>
            )}

            {success && (
                <div className="success-message">
                    {success}
                </div>
            )}

            {/* Add Salary */}

            <div className="salary-form-card">

                <h2>Add Salary</h2>

                <form
                    className="salary-form"
                    onSubmit={handleSubmit}
                >

                    <div className="form-group">

                        <label>Employee</label>

                        <select
                            name="employeeId"
                            value={formData.employeeId}
                            onChange={handleChange}
                            required
                        >

                            <option value="">
                                Select Employee
                            </option>

                            {employees.map((employee) => (

                                <option
                                    key={employee.id}
                                    value={employee.id}
                                >
                                    {employee.firstName}{" "}
                                    {employee.lastName || ""}
                                </option>

                            ))}

                        </select>

                    </div>

                    <div className="form-group">

                        <label>Basic Salary</label>

                        <input
                            type="number"
                            name="basic"
                            value={formData.basic}
                            onChange={handleChange}
                            min="0"
                            required
                        />

                    </div>

                    <div className="form-group">

                        <label>Bonus</label>

                        <input
                            type="number"
                            name="bonus"
                            value={formData.bonus}
                            onChange={handleChange}
                            min="0"
                        />

                    </div>

                    <div className="form-group">

                        <label>Deduction</label>

                        <input
                            type="number"
                            name="deduction"
                            value={formData.deduction}
                            onChange={handleChange}
                            min="0"
                        />

                    </div>

                    <div className="form-group">

                        <label>Effective Date</label>

                        <input
                            type="date"
                            name="effectiveDate"
                            value={formData.effectiveDate}
                            onChange={handleChange}
                            required
                        />

                    </div>

                    <div className="salary-preview">

                        <span>Net Salary</span>

                        <strong>
                            ₹{calculateNetSalary()}
                        </strong>

                    </div>

                    <div className="form-actions">

                        <button
                            type="submit"
                            disabled={loading}
                        >
                            {loading
                                ? "Saving..."
                                : "Add Salary"}
                        </button>

                    </div>

                </form>

            </div>

            {/* Salary Records */}

            <div className="salary-table-card">

                <h2>Salary Records</h2>

                {loading ? (

                    <p>Loading...</p>

                ) : salaries.length === 0 ? (

                    <p>No salary records found.</p>

                ) : (

                    <div className="employee-table-container">

                        <table className="employee-table">

                            <thead>

                                <tr>
                                    <th>ID</th>
                                    <th>Employee</th>
                                    <th>Basic</th>
                                    <th>Bonus</th>
                                    <th>Deduction</th>
                                    <th>Net Salary</th>
                                    <th>Effective Date</th>
                                    <th>Action</th>
                                </tr>

                            </thead>

                            <tbody>

                                {salaries.map((salary) => (

                                    <tr key={salary.id}>

                                        <td>
                                            {salary.id}
                                        </td>

                                        <td>
                                            {getEmployeeName(
                                                salary.employeeId
                                            )}
                                        </td>

                                        <td>
                                            ₹{salary.basic}
                                        </td>

                                        <td>
                                            ₹{salary.bonus}
                                        </td>

                                        <td>
                                            ₹{salary.deduction}
                                        </td>

                                        <td>
                                            ₹{salary.netSalary}
                                        </td>

                                        <td>
                                            {salary.effectiveDate}
                                        </td>

                                        <td>

                                            <button
                                                onClick={() =>
                                                    handleDelete(
                                                        salary.id
                                                    )
                                                }
                                                disabled={loading}
                                            >
                                                Delete
                                            </button>

                                        </td>

                                    </tr>

                                ))}

                            </tbody>

                        </table>

                    </div>

                )}

            </div>

        </DashboardLayout>
    );
};

export default SalariesHR;