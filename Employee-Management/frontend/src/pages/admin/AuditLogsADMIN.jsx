import { useEffect, useState } from "react";
import DashboardLayout from "../../components/DashboardLayout";
import { getAllAuditLogs } from "../../service/auditService";

const AuditLogsADMIN = () => {

    const [logs, setLogs] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState("");

    useEffect(() => {

        const loadAuditLogs = async () => {

            try {
                const data = await getAllAuditLogs();
                setLogs(data);
            } catch (error) {
                console.error(error);
                setError("Unable to load audit logs.");
            } finally {
                setLoading(false);
            }
        };

        loadAuditLogs();

    }, []);

    return (
        <DashboardLayout role="ADMIN">

            <div className="page-header">
                <div>
                    <h1>Audit Logs</h1>
                    <p>Track system activities and changes</p>
                </div>
            </div>

            {loading && (
                <div className="card">
                    <p>Loading audit logs...</p>
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
                                    <th>Action</th>
                                    <th>Entity</th>
                                    <th>Entity ID</th>
                                    <th>Date & Time</th>
                                    <th>Details</th>
                                </tr>
                            </thead>

                            <tbody>

                                {logs.length === 0 ? (
                                    <tr>
                                        <td colSpan="7">
                                            No audit logs found.
                                        </td>
                                    </tr>
                                ) : (

                                    logs.map((log) => (
                                        <tr key={log.id}>

                                            <td>
                                                {log.id}
                                            </td>

                                            <td>
                                                {log.username}
                                            </td>

                                            <td>
                                                <span
                                                    className={`status-badge ${log.action
                                                        ?.toLowerCase()
                                                        .replaceAll("_", "-")}`}
                                                >
                                                    {log.action}
                                                </span>
                                            </td>

                                            <td>
                                                {log.entity}
                                            </td>

                                            <td>
                                                {log.entityId ?? "--"}
                                            </td>

                                            <td>
                                                {log.timestamp
                                                    ? new Date(log.timestamp).toLocaleString()
                                                    : "--"}
                                            </td>

                                            <td>
                                                {log.details || "--"}
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

export default AuditLogsADMIN;