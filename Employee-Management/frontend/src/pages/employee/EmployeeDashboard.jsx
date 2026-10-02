import { useEffect, useState } from "react";
import DashboardLayout from "../../components/DashboardLayout";
import StatCard from "../../components/Statcard";
import { getCurrentUser } from "../../service/userService";

const EmployeeDashboard = () => {

    const [user, setUser] = useState(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState("");

    useEffect(() => {
        const loadUser = async () => {
            try {
                const data = await getCurrentUser();
                setUser(data);

                // We can use this later for attendance/salary/leaves
                localStorage.setItem("employeeId", data.employeeId);
            } catch (err) {
                console.error(err);
                setError("Unable to load employee information.");
            } finally {
                setLoading(false);
            }
        };

        loadUser();
    }, []);

    if (loading) {
        return (
            <DashboardLayout role="USER">
                <p>Loading dashboard...</p>
            </DashboardLayout>
        );
    }

    if (error) {
        return (
            <DashboardLayout role="USER">
                <p>{error}</p>
            </DashboardLayout>
        );
    }

    return (
        <DashboardLayout role="USER">

            <div className="dashboard-header">
                <h1>Employee Dashboard</h1>
                <p>
                    Welcome back, {user?.firstName} {user?.lastName}
                </p>
            </div>

            <div className="stats-grid">

                <StatCard
                    title="Employee ID"
                    value={user?.employeeId ?? "-"}
                />

                <StatCard
                    title="Designation"
                    value={user?.designation ?? "-"}
                />

                <StatCard
                    title="Department"
                    value={user?.departmentName ?? "-"}
                />

                <StatCard
                    title="Status"
                    value={user?.status ?? "-"}
                />

            </div>

            <div className="employee-info-card">

                <h2>My Information</h2>

                <div className="employee-info-grid">

                    <div>
                        <span>Name</span>
                        <strong>
                            {user?.firstName} {user?.lastName}
                        </strong>
                    </div>

                    <div>
                        <span>Email</span>
                        <strong>{user?.email || "-"}</strong>
                    </div>

                    <div>
                        <span>Phone</span>
                        <strong>{user?.phone || "-"}</strong>
                    </div>

                    <div>
                        <span>Designation</span>
                        <strong>{user?.designation || "-"}</strong>
                    </div>

                    <div>
                        <span>Status</span>
                        <strong>{user?.status || "-"}</strong>
                    </div>

                </div>

            </div>

        </DashboardLayout>
    );
};

export default EmployeeDashboard;