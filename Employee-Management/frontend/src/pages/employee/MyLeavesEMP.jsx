import { useEffect, useState } from "react";
import DashboardLayout from "../../components/DashboardLayout";
import {
    getMyLeaves,
    applyLeave,
    cancelLeave
} from "../../service/leaveService";
import { getCurrentUser } from "../../service/userService";

const MyLeavesEMP = () => {

    const [leaves, setLeaves] = useState([]);

    const [loading, setLoading] = useState(true);
    const [error, setError] = useState("");

    const [showForm, setShowForm] = useState(false);
    const [submitting, setSubmitting] = useState(false);

    const [form, setForm] = useState({
        leaveType: "CASUAL",
        startDate: "",
        endDate: "",
        reason: ""
    });

    const loadLeaves = async () => {

        try {

            setLoading(true);
            setError("");

            const user = await getCurrentUser();
            
            if (user && user.employeeId) {
                const data = await getMyLeaves(user.employeeId);
                setLeaves(data);
            } else {
                console.error("Employee ID is missing, cannot fetch leaves");
            }

            setLeaves(data);

        } catch (error) {

            console.error(error);

            setError("Unable to load your leaves.");

        } finally {

            setLoading(false);
        }
    };

    useEffect(() => {
        loadLeaves();
    }, []);

    const handleChange = (e) => {

        const { name, value } = e.target;

        setForm((prev) => ({
            ...prev,
            [name]: value
        }));
    };

    const handleApply = async (e) => {

        e.preventDefault();

        try {

            setSubmitting(true);
            setError("");

            const user = await getCurrentUser();

            await applyLeave({
                employeeId: user.employeeId,
                leaveType: form.leaveType,
                startDate: form.startDate,
                endDate: form.endDate,
                reason: form.reason
            });

            setForm({
                leaveType: "CASUAL",
                startDate: "",
                endDate: "",
                reason: ""
            });

            setShowForm(false);

            await loadLeaves();

        } catch (error) {

            console.error(error);

            setError(
                error.response?.data?.message ||
                "Unable to apply for leave."
            );

        } finally {

            setSubmitting(false);
        }
    };

    const handleCancel = async (leaveId) => {

        const confirmed = window.confirm(
            "Are you sure you want to cancel this leave?"
        );

        if (!confirmed) {
            return;
        }

        try {

            await cancelLeave(leaveId);

            await loadLeaves();

        } catch (error) {

            console.error(error);

            setError(
                error.response?.data?.message ||
                "Unable to cancel leave."
            );
        }
    };

    return (
        <DashboardLayout role="USER">

            <div className="page-header">

                <div>
                    <h1>My Leaves</h1>
                    <p>View and manage your leave requests</p>
                </div>

                <button
                    onClick={() => setShowForm(!showForm)}
                >
                    {showForm ? "Close" : "Apply for Leave"}
                </button>

            </div>

            {error && (
                <div className="card">
                    <p className="error-message">
                        {error}
                    </p>
                </div>
            )}

            {showForm && (

                <div className="card">

                    <h2>Apply for Leave</h2>

                    <form onSubmit={handleApply}>

                        <div className="form-group">
                            <label>Leave Type</label>

                            <select
                                name="leaveType"
                                value={form.leaveType}
                                onChange={handleChange}
                                required
                            >
                                <option value="CASUAL">Casual Leave</option>
                                <option value="SICK">Sick Leave</option>
                                <option value="EARNED">Earned Leave</option>
                                <option value="UNPAID">Unpaid Leave</option>
                                <option value="MATERNITY">Maternity Leave</option>
                                <option value="PATERNITY">Paternity Leave</option>
                            </select>
                        </div>
                        <div className="form-group">

                            <label>Start Date</label>

                            <input
                                type="date"
                                name="startDate"
                                value={form.startDate}
                                onChange={handleChange}
                                required
                            />

                        </div>

                        <div className="form-group">

                            <label>End Date</label>

                            <input
                                type="date"
                                name="endDate"
                                value={form.endDate}
                                onChange={handleChange}
                                required
                            />

                        </div>

                        <div className="form-group">

                            <label>Reason</label>

                            <textarea
                                name="reason"
                                value={form.reason}
                                onChange={handleChange}
                                placeholder="Enter reason for leave"
                                rows="4"
                                required
                            />

                        </div>

                        <button
                            type="submit"
                            disabled={submitting}
                        >
                            {submitting
                                ? "Submitting..."
                                : "Apply Leave"}
                        </button>

                    </form>

                </div>
            )}

            {loading ? (

                <div className="card">
                    <p>Loading leaves...</p>
                </div>

            ) : (

                <div className="card">

                    <div className="table-container">

                        <table className="data-table">

                            <thead>

                                <tr>
                                    <th>ID</th>
                                    <th>Type</th>
                                    <th>Start Date</th>
                                    <th>End Date</th>
                                    <th>Reason</th>
                                    <th>Status</th>
                                    <th>Action</th>
                                </tr>

                            </thead>

                            <tbody>

                                {leaves.length === 0 ? (

                                    <tr>
                                        <td colSpan="7">
                                            No leave requests found.
                                        </td>
                                    </tr>

                                ) : (

                                    leaves.map((leave) => (

                                        <tr key={leave.id}>

                                            <td>
                                                {leave.id}
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
                                                {leave.reason || "--"}
                                            </td>

                                            <td>
                                                {leave.status}
                                            </td>

                                            <td>

                                                {leave.status === "PENDING" && (

                                                    <button
                                                        onClick={() =>
                                                            handleCancel(
                                                                leave.id
                                                            )
                                                        }
                                                    >
                                                        Cancel
                                                    </button>

                                                )}

                                                {leave.status !== "PENDING" &&
                                                    "--"}

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

export default MyLeavesEMP;