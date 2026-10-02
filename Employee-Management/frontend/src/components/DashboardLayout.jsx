import Sidebar from "./Sidebar";
import Navbar from "./Navbar";

const DashboardLayout = ({ role, children }) => {
    return (
        <div className="dashboard-layout">

            <Sidebar role={role} />

            <div className="dashboard-main">

                <Navbar role={role} />

                <main className="dashboard-content">
                    {children}
                </main>

            </div>

        </div>
    );
};

export default DashboardLayout;