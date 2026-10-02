import { useEffect, useState } from "react";
import DashboardLayout from "../../components/DashboardLayout";
import { getCurrentUser } from "../../service/userService";

const ProfileADMIN = () => {

    const [user, setUser] = useState(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState("");

    useEffect(() => {

        const loadProfile = async () => {

            try {

                const data = await getCurrentUser();

                setUser(data);

            } catch (error) {

                console.error(error);
                setError("Unable to load profile.");

            } finally {

                setLoading(false);

            }
        };

        loadProfile();

    }, []);

    return (
        <DashboardLayout role="ADMIN">

            <div className="page-header">
                <div>
                    <h1>My Profile</h1>
                    {/* <p>View your account information</p> */}
                </div>
            </div>

            {loading && (
                <div className="card">
                    <p>Loading profile...</p>
                </div>
            )}

            {error && (
                <div className="card">
                    <p>{error}</p>
                </div>
            )}

            {!loading && !error && user && (

                <div className="profile-grid">

                    <div className="card">

                        <h2>Account Information</h2>

                        <div className="profile-info">

                            <div className="profile-field">
                                <span>Username</span>
                                <strong>{user.username}</strong>
                            </div>

                            <div className="profile-field">
                                <span>Role</span>
                                <strong>{user.role}</strong>
                            </div>

                            <div className="profile-field">
                                <span>User ID</span>
                                <strong>{user.userId}</strong>
                            </div>

                            <div className="profile-field">
                                <span>Status</span>
                                <strong>{user.status || "--"}</strong>
                            </div>

                        </div>

                    </div>


                    <div className="card">

                        <h2>Personal Information</h2>

                        <div className="profile-info">

                            <div className="profile-field">
                                <span>First Name</span>
                                <strong>
                                    {user.firstName || "--"}
                                </strong>
                            </div>

                            <div className="profile-field">
                                <span>Last Name</span>
                                <strong>
                                    {user.lastName || "--"}
                                </strong>
                            </div>

                            <div className="profile-field">
                                <span>Email</span>
                                <strong>
                                    {user.email || "--"}
                                </strong>
                            </div>

                            <div className="profile-field">
                                <span>Phone</span>
                                <strong>
                                    {user.phone || "--"}
                                </strong>
                            </div>

                        </div>

                    </div>


                    <div className="card">

                        <h2>Employee Information</h2>

                        <div className="profile-info">

                            <div className="profile-field">
                                <span>Employee ID</span>
                                <strong>
                                    {user.employeeId || "--"}
                                </strong>
                            </div>

                            <div className="profile-field">
                                <span>Designation</span>
                                <strong>
                                    {user.designation || "--"}
                                </strong>
                            </div>

                            <div className="profile-field">
                                <span>Employee Status</span>
                                <strong>
                                    {user.status || "--"}
                                </strong>
                            </div>

                        </div>

                    </div>

                </div>

            )}

        </DashboardLayout>
    );
};

export default ProfileADMIN;