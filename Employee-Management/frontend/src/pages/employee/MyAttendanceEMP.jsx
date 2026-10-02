import { useEffect, useState } from "react";
import DashboardLayout from "../../components/DashboardLayout";

import {
    checkIn,
    checkOut,
    getEmployeeAttendance,
    getMonthlyAttendance
} from "../../service/attendanceService";



const MyAttendanceEMP = () => {

    const employeeId = localStorage.getItem("employeeId");

    const [attendance, setAttendance] = useState([]);
    const [loading, setLoading] = useState(true);
    const [message, setMessage] = useState("");
    const [error, setError] = useState("");

    const currentDate = new Date();

    const [month, setMonth] = useState(
        currentDate.getMonth() + 1
    );

    const [year, setYear] = useState(
        currentDate.getFullYear()
    );

    const loadAttendance = async () => {

        if (!employeeId) {
            setError("Employee information not found.");
            setLoading(false);
            return;
        }

        try {
            setLoading(true);
            setError("");

            const data = await getMonthlyAttendance(
                employeeId,
                month,
                year
            );

            setAttendance(data);

        } catch (err) {
            console.error(err);
            setError("Unable to load attendance.");
        } finally {
            setLoading(false);
        }
    };

    useEffect(() => {
        loadAttendance();
    }, [month, year]);

    const handleCheckIn = async () => {

        try {
            setMessage("");
            setError("");

            await checkIn(employeeId);

            setMessage("Check-in successful.");

            await loadAttendance();

        } catch (err) {
            console.error(err);

            setError(
                err.response?.data?.message ||
                "Unable to check in."
            );
        }
    };

    const handleCheckOut = async () => {

        try {
            setMessage("");
            setError("");

            await checkOut(employeeId);

            setMessage("Check-out successful.");

            await loadAttendance();

        } catch (err) {
            console.error(err);

            setError(
                err.response?.data?.message ||
                "Unable to check out."
            );
        }
    };

    return (
        <DashboardLayout role="USER">

            <div className="page-header">

                <div>
                    <h1>My Attendance</h1>
                    <p>View your attendance records</p>
                </div>

            </div>

            {/* Check In / Check Out */}

            <div className="attendance-actions">

                <button
                    className="attendance-btn"
                    onClick={handleCheckIn}
                >
                    Check In
                </button>

                <button
                    className="attendance-btn"
                    onClick={handleCheckOut}
                >
                    Check Out
                </button>

            </div>

            {message && (
                <div className="success-message">
                    {message}
                </div>
            )}

            {error && (
                <div className="error-message">
                    {error}
                </div>
            )}

            {/* Month Filter */}

            <div className="attendance-filter">

                <div>
                    <label>Month</label>

                    <select
                        value={month}
                        onChange={(e) =>
                            setMonth(Number(e.target.value))
                        }
                    >
                        <option value={1}>January</option>
                        <option value={2}>February</option>
                        <option value={3}>March</option>
                        <option value={4}>April</option>
                        <option value={5}>May</option>
                        <option value={6}>June</option>
                        <option value={7}>July</option>
                        <option value={8}>August</option>
                        <option value={9}>September</option>
                        <option value={10}>October</option>
                        <option value={11}>November</option>
                        <option value={12}>December</option>
                    </select>

                </div>

                <div>
                    <label>Year</label>

                    <select
                        value={year}
                        onChange={(e) =>
                            setYear(Number(e.target.value))
                        }
                    >
                        <option value={2026}>2026</option>
                        <option value={2027}>2027</option>
                        <option value={2028}>2028</option>
                    </select>

                </div>

            </div>

            {/* Attendance Table */}

            {loading ? (
                <p>Loading attendance...</p>
            ) : attendance.length === 0 ? (
                <p>No attendance records found.</p>
            ) : (

                <div className="attendance-table-container">

                    <table className="attendance-table">

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
                                        {record.status || "-"}
                                    </td>

                                </tr>

                            ))}

                        </tbody>

                    </table>

                </div>

            )}

        </DashboardLayout>
    );
};

export default MyAttendanceEMP;