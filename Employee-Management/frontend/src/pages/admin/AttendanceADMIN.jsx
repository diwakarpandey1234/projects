import { useEffect, useState } from "react";
import DashboardLayout from "../../components/DashboardLayout";
import { getAllAttendance } from "../../service/attendanceService";

const AttendanceADMIN = () => {

    const [attendance, setAttendance] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState("");

    useEffect(() => {

        const loadAttendance = async () => {

            try {

                const data = await getAllAttendance();

                setAttendance(data);

            } catch (error) {

                console.error(error);
                setError("Unable to load attendance.");

            } finally {

                setLoading(false);
            }
        };

        loadAttendance();

    }, []);

    return (
        <DashboardLayout role="ADMIN">

            <div className="page-header">

                <div>
                    <h1>Attendance</h1>
                    <p>View employee attendance records</p>
                </div>

            </div>

            {loading && (
                <div className="card">
                    <p>Loading attendance...</p>
                </div>
            )}

            {error && (
                <div className="card">
                    <p>{error}</p>
                </div>
            )}

            {!loading && !error && (
                <div className="card">

                    <div className="table-container">

                        <table className="data-table">

                            <thead>
                                <tr>
                                    <th>Employee ID</th>
                                    <th>Date</th>
                                    <th>Check In</th>
                                    <th>Check Out</th>
                                    <th>Role</th>
                                    <th>Status</th>
                                    
                                </tr>
                            </thead>

                            <tbody>

                                {attendance.length === 0 ? (

                                    <tr>
                                        <td colSpan="5">
                                            No attendance records found.
                                        </td>
                                    </tr>

                                ) : (

                                    attendance.map((record, index) => (

                                        <tr key={`${record.employeeId}-${record.date}-${index}`}>

                                            <td>
                                                {record.employeeId}
                                            </td>

                                            <td>
                                                {record.date}
                                            </td>

                                            <td>
                                                {record.checkIn || "--"}
                                            </td>

                                            <td>
                                                {record.checkOut || "--"}
                                            </td>
                                            <td>
                                                {record.role || "--"}
                                            </td>

                                            <td>
                                                <span
                                                    className={`status-badge ${record.status?.toLowerCase()}`}
                                                >
                                                    {record.status}
                                                </span>
                                            </td>

                                        </tr>

                                    ))
                                )}

                            </tbody>

                        </table>

                    </div>

                </div>
            )}

        </DashboardLayout>
    );
};

export default AttendanceADMIN;