import { useEffect, useState } from "react";
import DashboardLayout from "../../components/DashboardLayout";
import StatCard from "../../components/Statcard"
import { getDashboard } from "../../service/dashboardService";

const AdminDashboard = () => {

    const [dashboard, setDashboard] = useState(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState("");

    useEffect(() => {

        const loadDashboard = async () => {

            try {
                const data = await getDashboard();

                setDashboard(data);

            } catch (error) {

                console.error(error);

                setError("Unable to load dashboard data.");

            } finally {

                setLoading(false);
            }
        };

        loadDashboard();

    }, []);

    if (loading) {
        return (
            <DashboardLayout role="ADMIN">
                <h1>Admin Dashboard</h1>
                <p>Loading dashboard...</p>
            </DashboardLayout>
        );
    }

    if (error) {
        return (
            <DashboardLayout role="ADMIN">
                <h1>Admin Dashboard</h1>
                <p>{error}</p>
            </DashboardLayout>
        );
    }

    return (
        <DashboardLayout role="ADMIN">

            <div className="dashboard-header">
                <h1>Admin Dashboard</h1>
                <p>System overview</p>
            </div>

            <div className="stats-grid">

                <StatCard
                    title="Total Employees"
                    value={dashboard.totalEmployees}
                />

                <StatCard
                    title="Active Employees"
                    value={dashboard.activeEmployees}
                />

                <StatCard
                    title="Inactive Employees"
                    value={dashboard.inactiveEmployees}
                />

                <StatCard
                    title="Departments"
                    value={dashboard.totalDepartments}
                />

                <StatCard
                    title="Pending Leaves"
                    value={dashboard.pendingLeaves}
                />

                <StatCard
                    title="Approved Leaves"
                    value={dashboard.approvedLeaves}
                />

                <StatCard
                    title="Rejected Leaves"
                    value={dashboard.rejectedLeaves}
                />

                <StatCard
                    title="Salary Records"
                    value={dashboard.totalSalaryRecords}
                />

                <StatCard
                    title="Present Today"
                    value={dashboard.todayPresent}
                />

                <StatCard
                    title="Absent Today"
                    value={dashboard.todayAbsent}
                />

                <StatCard
                    title="Late Today"
                    value={dashboard.todayLate}
                />

                <StatCard
                    title="Half Day Today"
                    value={dashboard.todayHalfDay}
                />

            </div>

        </DashboardLayout>
    );
};

export default AdminDashboard;