import { useEffect, useState } from "react";
import DashboardLayout from "../../components/DashboardLayout";
import { getMySalary } from "../../service/salaryService";

const MySalaryEMP = () => {

    const [salaries, setSalaries] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState("");

    useEffect(() => {

        const loadSalary = async () => {

            try {

                const data = await getMySalary();

                setSalaries(data);

            } catch (error) {

                console.error(error);

                setError("Unable to load salary information.");

            } finally {

                setLoading(false);
            }
        };

        loadSalary();

    }, []);

    return (
        <DashboardLayout role="USER">

            <div className="page-header">
                <div>
                    <h1>My Salary</h1>
                    <p>View your salary records</p>
                </div>
            </div>

            {loading && (
                <div className="card">
                    <p>Loading salary...</p>
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
                                    <th>Net Salary</th>
                                </tr>
                            </thead>

                            <tbody>

                                {salaries.length === 0 ? (

                                    <tr>
                                        <td colSpan="2">
                                            No salary records found.
                                        </td>
                                    </tr>

                                ) : (

                                    salaries.map((salary) => (

                                        <tr key={salary.id}>

                                            <td>
                                                {salary.id}
                                            </td>

                                            <td>
                                                ₹{salary.netSalary}
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

export default MySalaryEMP;

// import { useEffect, useState } from "react";
// import DashboardLayout from "../../components/DashboardLayout";
// import { getAllSalaries } from "../../service/salaryService";

// const MySalaryEMP = () => {

//     const employeeId = Number(localStorage.getItem("employeeId"));

//     const [salaries, setSalaries] = useState([]);
//     const [loading, setLoading] = useState(true);
//     const [error, setError] = useState("");

//     useEffect(() => {

//         const loadSalary = async () => {

//             try {
//                 setLoading(true);
//                 setError("");

//                 const data = await getAllSalaries();

//                 // Assuming SalaryResponseDTO contains employeeId
//                 const mySalaries = data.filter(
//                     (salary) => Number(salary.employeeId) === employeeId
//                 );

//                 setSalaries(mySalaries);

//             } catch (err) {
//                 console.error(err);
//                 setError("Unable to load salary records.");
//             } finally {
//                 setLoading(false);
//             }
//         };

//         loadSalary();

//     }, [employeeId]);

//     return (
//         <DashboardLayout role="USER">

//             <div className="page-header">
//                 <div>
//                     <h1>My Salary</h1>
//                     <p>View your salary records</p>
//                 </div>
//             </div>

//             {loading && (
//                 <p>Loading salary records...</p>
//             )}

//             {error && (
//                 <div className="error-message">
//                     {error}
//                 </div>
//             )}

//             {!loading && !error && salaries.length === 0 && (
//                 <p>No salary records found.</p>
//             )}

//             {!loading && salaries.length > 0 && (

//                 <div className="salary-table-container">

//                     <table className="salary-table">

//                         <thead>
//                             <tr>
//                                 {/* <th>Effective Date</th>
//                                 <th>Basic Salary</th>
//                                 <th>Bonus</th>
//                                 <th>Deduction</th> */}
//                                 <th>Net Salary</th>
//                             </tr>
//                         </thead>

//                         <tbody>

//                             {salaries.map((salary) => (

//                                 <tr key={salary.id}>

//                                     {/* <td>
//                                         {salary.effectiveDate || "-"}
//                                     </td>

//                                     <td>
//                                         ₹{salary.basic ?? 0}
//                                     </td>

//                                     <td>
//                                         ₹{salary.bonus ?? 0}
//                                     </td>

//                                     <td>
//                                         ₹{salary.deduction ?? 0}
//                                     </td> */}

//                                     <td>
//                                         ₹{salary.netSalary ?? 0}
//                                     </td>

//                                 </tr>

//                             ))}

//                         </tbody>

//                     </table>

//                 </div>
//             )}

//         </DashboardLayout>
//     );
// };

// export default MySalaryEMP;