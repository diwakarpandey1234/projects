import { useEffect, useState } from "react";
import DashboardLayout from "../../components/DashboardLayout";
import { getHRUsers } from "../../service/userService";

const HRADMIN = () => {

    const [hrUsers, setHrUsers] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState("");

    useEffect(() => {

        const loadHRUsers = async () => {
            try {
                const data = await getHRUsers();
                setHrUsers(data);
            } catch (error) {
                console.error(error);
                setError("Unable to load HR users.");
            } finally {
                setLoading(false);
            }
        };

        loadHRUsers();

    }, []);

    return (
        <DashboardLayout role="ADMIN">

            <div className="page-header">
                <div>
                    <h1>HR</h1>
                    {/* <p>View HR users</p> */}
                </div>
            </div>

            {loading && (
                <div className="card">
                    <p>Loading HR users...</p>
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
                                    <th>Username</th>
                                    <th>Department</th>
                                </tr>
                            </thead>

                            <tbody>

                                {hrUsers.length === 0 ? (

                                    <tr>
                                        <td colSpan="3">
                                            No HR users found.
                                        </td>
                                    </tr>

                                ) : (

                                    hrUsers.map((user) => (
                                        <tr key={user.employeeID}>

                                            <td>{user.employeeID}</td>

                                            <td>{user.firstName} {user.lastName}</td>

                                            <td>
                                                <span className="status-badge">
                                                    {user.departmentName}
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

export default HRADMIN;