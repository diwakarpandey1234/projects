import { useEffect, useState } from "react";
import DashboardLayout from "../../components/DashboardLayout";
import { getAllSalaries } from "../../service/salaryService";

const SalariesADMIN = () => {

    const [salaries, setSalaries] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState("");

    useEffect(() => {

        const loadSalaries = async () => {
            try {
                const data = await getAllSalaries();
                setSalaries(data);
            } catch (error) {
                console.error(error);
                setError("Unable to load salary records.");
            } finally {
                setLoading(false);
            }
        };

        loadSalaries();

    }, []);

    return (
        <DashboardLayout role="ADMIN">

            <div className="page-header">
                <div>
                    <h1>Salaries</h1>
                    <p>View employee salary records</p>
                </div>
            </div>

            {loading && (
                <div className="card">
                    <p>Loading salary records...</p>
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
                                    <th>Basic</th>
                                    <th>Bonus</th>
                                    <th>Deduction</th>
                                    <th>Net Salary</th>
                                    <th>Effective Date</th>
                                </tr>
                            </thead>

                            <tbody>

                                {salaries.length === 0 ? (
                                    <tr>
                                        <td colSpan="7">
                                            No salary records found.
                                        </td>
                                    </tr>
                                ) : (

                                    salaries.map((salary) => (
                                        <tr key={salary.id}>

                                            <td>{salary.id}</td>

                                            <td>
                                                {salary.employeeId ?? "--"}
                                            </td>

                                            <td>
                                                ₹{salary.basic ?? 0}
                                            </td>

                                            <td>
                                                ₹{salary.bonus ?? 0}
                                            </td>

                                            <td>
                                                ₹{salary.deduction ?? 0}
                                            </td>

                                            <td>
                                                ₹{salary.netSalary ?? 0}
                                            </td>

                                            <td>
                                                {salary.effectiveDate ?? "--"}
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

export default SalariesADMIN;