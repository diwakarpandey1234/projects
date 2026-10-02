import { useEffect, useState } from "react";

import DashboardLayout from "../../components/DashboardLayout";

import {
    checkIn,
    checkOut,
    getEmployeeAttendance,
    getMonthlyAttendance
} from "../../service/attendanceService";

import { getAllEmployees } from "../../service/EmployeeService";

const AttendanceHR = () => {

    const [employees, setEmployees] = useState([]);
    const [attendance, setAttendance] = useState([]);

    const [selectedEmployee, setSelectedEmployee] = useState("");

    const [month, setMonth] = useState(
        new Date().getMonth() + 1
    );

    const [year, setYear] = useState(
        new Date().getFullYear()
    );

    const [loading, setLoading] = useState(false);
    const [error, setError] = useState("");
    const [success, setSuccess] = useState("");

    useEffect(() => {
        loadEmployees();
    }, []);

    const loadEmployees = async () => {

        try {

            const data = await getAllEmployees();

            setEmployees(data);

        } catch (err) {

            console.error(err);

            setError("Failed to load employees.");

        }
    };

    const loadAttendance = async (employeeId) => {

        try {

            setLoading(true);
            setError("");

            const data = await getEmployeeAttendance(employeeId);

            setAttendance(data);

        } catch (err) {

            console.error(err);

            setError("Failed to load attendance.");

        } finally {

            setLoading(false);

        }
    };

    const handleEmployeeChange = async (e) => {

        const employeeId = e.target.value;

        setSelectedEmployee(employeeId);
        setAttendance([]);

        if (!employeeId) {
            return;
        }

        await loadAttendance(employeeId);
    };

    const handleCheckIn = async () => {

        if (!selectedEmployee) {
            setError("Please select an employee.");
            return;
        }

        try {

            setLoading(true);
            setError("");
            setSuccess("");

            await checkIn(Number(selectedEmployee));

            setSuccess("Employee checked in successfully.");

            await loadAttendance(selectedEmployee);

        } catch (err) {

            console.error(err);

            setError(
                err.response?.data ||
                "Failed to check in employee."
            );

        } finally {

            setLoading(false);

        }
    };

    const handleCheckOut = async () => {

        if (!selectedEmployee) {
            setError("Please select an employee.");
            return;
        }

        try {

            setLoading(true);
            setError("");
            setSuccess("");

            await checkOut(Number(selectedEmployee));

            setSuccess("Employee checked out successfully.");

            await loadAttendance(selectedEmployee);

        } catch (err) {

            console.error(err);

            setError(
                err.response?.data ||
                "Failed to check out employee."
            );

        } finally {

            setLoading(false);

        }
    };

    const handleMonthlyAttendance = async () => {

        if (!selectedEmployee) {
            setError("Please select an employee.");
            return;
        }

        try {

            setLoading(true);
            setError("");

            const data = await getMonthlyAttendance(
                Number(selectedEmployee),
                Number(month),
                Number(year)
            );

            setAttendance(data);

        } catch (err) {

            console.error(err);

            setError("Failed to load monthly attendance.");

        } finally {

            setLoading(false);

        }
    };

    return (
        <DashboardLayout role="HR">

            <div className="page-header">

                <div>
                    <h1>Attendance Management</h1>
                    <p>
                        Manage employee attendance
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

            {/* Employee Selection */}

            <div className="attendance-control-card">

                <div className="form-group">

                    <label>
                        Select Employee
                    </label>

                    <select
                        value={selectedEmployee}
                        onChange={handleEmployeeChange}
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
                                {employee.lastName}
                            </option>

                        ))}

                    </select>

                </div>

                {/* Attendance Actions */}

                <div className="attendance-actions">

                    <button
                        onClick={handleCheckIn}
                        disabled={
                            !selectedEmployee || loading
                        }
                    >
                        Check In
                    </button>

                    <button
                        onClick={handleCheckOut}
                        disabled={
                            !selectedEmployee || loading
                        }
                    >
                        Check Out
                    </button>

                </div>

            </div>

            {/* Monthly Filter */}

            <div className="attendance-control-card">

                <h2>Monthly Attendance</h2>

                <div className="monthly-filter">

                    <div className="form-group">

                        <label>Month</label>

                        <select
                            value={month}
                            onChange={(e) =>
                                setMonth(e.target.value)
                            }
                        >
                            <option value="1">January</option>
                            <option value="2">February</option>
                            <option value="3">March</option>
                            <option value="4">April</option>
                            <option value="5">May</option>
                            <option value="6">June</option>
                            <option value="7">July</option>
                            <option value="8">August</option>
                            <option value="9">September</option>
                            <option value="10">October</option>
                            <option value="11">November</option>
                            <option value="12">December</option>
                        </select>

                    </div>

                    <div className="form-group">

                        <label>Year</label>

                        <input
                            type="number"
                            value={year}
                            onChange={(e) =>
                                setYear(e.target.value)
                            }
                        />

                    </div>

                    <button
                        onClick={handleMonthlyAttendance}
                        disabled={
                            !selectedEmployee || loading
                        }
                    >
                        View Monthly Attendance
                    </button>

                </div>

            </div>

            {/* Attendance Table */}

            <div className="attendance-table-card">

                <h2>Attendance Records</h2>

                {!selectedEmployee ? (

                    <p>
                        Select an employee to view attendance.
                    </p>

                ) : loading ? (

                    <p>Loading...</p>

                ) : attendance.length === 0 ? (

                    <p>
                        No attendance records found.
                    </p>

                ) : (

                    <div className="employee-table-container">

                        <table className="employee-table">

                            <thead>

                                <tr>
                                    <th>Date</th>
                                    <th>Check In</th>
                                    <th>Check Out</th>
                                    <th>Status</th>
                                </tr>

                            </thead>

                            <tbody>

                                {attendance.map((record) => (

                                    <tr key={record.id}>

                                        <td>
                                            {record.date}
                                        </td>

                                        <td>
                                            {record.checkIn || "-"}
                                        </td>

                                        <td>
                                            {record.checkOut || "-"}
                                        </td>

                                        <td>
                                            {record.status}
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

export default AttendanceHR;