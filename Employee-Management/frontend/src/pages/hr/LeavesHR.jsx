import { useEffect, useState } from "react";

import DashboardLayout from "../../components/DashboardLayout";

import {
    getPendingLeaves,
    approveLeave,
    rejectLeave
} from "../../service/leaveService";

const LeavesHR = () => {

    const [leaves, setLeaves] = useState([]);

    const [loading, setLoading] = useState(false);
    const [error, setError] = useState("");
    const [success, setSuccess] = useState("");

    useEffect(() => {
        loadLeaves();
    }, []);

    const loadLeaves = async () => {

        try {

            setLoading(true);
            setError("");

            const data = await getPendingLeaves();

            setLeaves(data);

        } catch (err) {

            console.error(err);

            setError("Failed to load leave requests.");

        } finally {

            setLoading(false);

        }
    };

    const handleApprove = async (id) => {

        try {

            setLoading(true);
            setError("");
            setSuccess("");

            await approveLeave(id);

            setSuccess("Leave approved successfully.");

            await loadLeaves();

        } catch (err) {

            console.error(err);

            setError(
                err.response?.data ||
                "Failed to approve leave."
            );

        } finally {

            setLoading(false);

        }
    };

    const handleReject = async (id) => {

        try {

            setLoading(true);
            setError("");
            setSuccess("");

            await rejectLeave(id);

            setSuccess("Leave rejected successfully.");

            await loadLeaves();

        } catch (err) {

            console.error(err);

            setError(
                err.response?.data ||
                "Failed to reject leave."
            );

        } finally {

            setLoading(false);

        }
    };

    return (
        <DashboardLayout role="HR">

            <div className="page-header">

                <div>
                    <h1>Leave Management</h1>
                    <p>
                        Review and manage employee leave requests
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

            <div className="leave-table-card">

                <h2>Pending Leave Requests</h2>

                {loading ? (

                    <p>Loading...</p>

                ) : leaves.length === 0 ? (

                    <p>
                        No pending leave requests.
                    </p>

                ) : (

                    <div className="employee-table-container">

                        <table className="employee-table">

                            <thead>

                                <tr>
                                    <th>Employee</th>
                                    <th>Leave Type</th>
                                    <th>Start Date</th>
                                    <th>End Date</th>
                                    <th>Reason</th>
                                    <th>Status</th>
                                    <th>Actions</th>
                                </tr>

                            </thead>

                            <tbody>

                                {leaves.map((leave) => (

                                    <tr key={leave.id}>

                                        <td>
                                            {leave.employee?.firstName}{" "}
                                            {leave.employee?.lastName}
                                        </td>

                                        <td>
                                            {leave.leaveType}
                                        </td>

                                        <td>
                                            {leave.startDate}
                                        </td>

                                        <td>
                                            {leave.endDate}
                                        </td>

                                        <td>
                                            {leave.reason || "-"}
                                        </td>

                                        <td>
                                            {leave.status}
                                        </td>

                                        <td>

                                            <div className="leave-actions">

                                                <button
                                                    onClick={() =>
                                                        handleApprove(
                                                            leave.id
                                                        )
                                                    }
                                                    disabled={loading}
                                                >
                                                    Approve
                                                </button>

                                                <button
                                                    onClick={() =>
                                                        handleReject(
                                                            leave.id
                                                        )
                                                    }
                                                    disabled={loading}
                                                >
                                                    Reject
                                                </button>

                                            </div>

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

export default LeavesHR;