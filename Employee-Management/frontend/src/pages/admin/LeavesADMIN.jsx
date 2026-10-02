import { useEffect, useState } from "react";
import DashboardLayout from "../../components/DashboardLayout";
import {
    getPendingLeaves,
    approveLeave,
    rejectLeave
} from "../../service/leaveService";

const LeavesADMIN = () => {

    const [leaves, setLeaves] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState("");

    const loadLeaves = async () => {

        try {
            setLoading(true);

            const data = await getPendingLeaves();

            setLeaves(data);

        } catch (error) {
            console.error(error);
            setError("Unable to load pending leaves.");
        } finally {
            setLoading(false);
        }
    };

    useEffect(() => {
        loadLeaves();
    }, []);

    const handleApprove = async (id) => {

        try {

            await approveLeave(id);

            await loadLeaves();

        } catch (error) {
            console.error(error);
            alert("Unable to approve leave.");
        }
    };

    const handleReject = async (id) => {

        try {

            await rejectLeave(id);

            await loadLeaves();

        } catch (error) {
            console.error(error);
            alert("Unable to reject leave.");
        }
    };

    return (
        <DashboardLayout role="ADMIN">

            <div className="page-header">
                <div>
                    <h1>Leaves</h1>
                    <p>Manage employee leave requests</p>
                </div>
            </div>

            {loading && (
                <div className="card">
                    <p>Loading leave requests...</p>
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
                                    <th>ID</th>
                                    <th>Employee ID</th>
                                    <th>Leave Type</th>
                                    <th>Start Date</th>
                                    <th>End Date</th>
                                    <th>Reason</th>
                                    <th>Status</th>
                                    <th>Actions</th>
                                </tr>
                            </thead>

                            <tbody>

                                {leaves.length === 0 ? (
                                    <tr>
                                        <td colSpan="8">
                                            No pending leave requests.
                                        </td>
                                    </tr>
                                ) : (

                                    leaves.map((leave) => (
                                        <tr key={leave.id}>

                                            <td>
                                                {leave.id}
                                            </td>

                                            <td>
                                                {leave.employeeId ?? "--"}
                                            </td>

                                            <td>
                                                {leave.leaveType ?? "--"}
                                            </td>

                                            <td>
                                                {leave.startDate ?? "--"}
                                            </td>

                                            <td>
                                                {leave.endDate ?? "--"}
                                            </td>

                                            <td>
                                                {leave.reason || "--"}
                                            </td>

                                            <td>
                                                <span
                                                    className={`status-badge ${leave.status?.toLowerCase()}`}
                                                >
                                                    {leave.status}
                                                </span>
                                            </td>

                                            <td>

                                                <button
                                                    className="btn btn-success"
                                                    onClick={() =>
                                                        handleApprove(leave.id)
                                                    }
                                                >
                                                    Approve
                                                </button>

                                                <button
                                                    className="btn btn-danger"
                                                    onClick={() =>
                                                        handleReject(leave.id)
                                                    }
                                                >
                                                    Reject
                                                </button>

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

export default LeavesADMIN;