import { BrowserRouter, Routes, Route, Navigate } from "react-router-dom";
import { AuthProvider } from "./context/AuthContext";
import AdminDashboard from "./pages/admin/AdminDashboard";
import RoleRoute from "./components/RoleRoute";
import Landing from "./pages/Landing";
import EmployeesHR from "./pages/hr/EmployeesHR";
import RoleLogin from "./pages/RoleLogin";
import ProtectedRoute from "./components/ProtectedRoute";
import HRDashboard from "./pages/hr/HRDashboard";
import DashboardLayout from "./components/DashboardLayout";
import AddEmployeeHR from "./pages/hr/AddEmployeeHR";
import AttendanceHR from "./pages/hr/AttendanceHR";
import LeavesHR from "./pages/hr/LeavesHR";
import SalariesHR from "./pages/hr/SalariesHR";
import EmployeeDashboard from "./pages/employee/EmployeeDashboard";
import MyAttendanceEMP from "./pages/employee/MyAttendanceEMP";
import DepartmentsADMIN from "./pages/admin/DepartmentsADMIN";
import AddDepartmentsADMIN from "./pages/admin/AddDepartmentsADMIN";
import HRADMIN from "./pages/admin/HRADMIN";
import AttendanceADMIN from "./pages/admin/AttendanceADMIN";
import AuditLogsADMIN from "./pages/admin/AuditLogsADMIN";
import SalariesADMIN from "./pages/admin/SalariesADMIN";
import LeavesADMIN from "./pages/admin/LeavesADMIN";
import ProfileADMIN from "./pages/admin/ProfileADMIN";
import EmployeesADMIN from "./pages/admin/EmployeesADMIN";
import MyLeavesEMP from "./pages/employee/MyLeavesEMP";
import MySalaryEMP from "./pages/employee/MySalaryEMP";
import AddEmployeeADMIN from "./pages/admin/AddEmployeeADMIN";
import AddHrADMIN from "./pages/admin/AddHrAdmin";




function App() {
  return (
    <AuthProvider>
      <BrowserRouter>
        <Routes>
          {/* Landing */}
          <Route path="/" element={<Landing />} />

          {/* Role Login */}
          <Route path="/login/:role" element={<RoleLogin />} />

          {/* ================= ADMIN ================= */}

          <Route
            path="/admin"
            element={
              <RoleRoute role="ADMIN">
                <AdminDashboard />
              </RoleRoute>
            }
          />

          <Route
            path="/admin/hr"
            element={
              <RoleRoute role="ADMIN">
                <HRADMIN />
              </RoleRoute>
            }
          />
          <Route
            path="/admin/hr/add"
            element={
              <RoleRoute role="ADMIN">
                <AddHrADMIN />
              </RoleRoute>
            }
          />

          <Route
            path="/admin/employees"
            element={
              <RoleRoute role="ADMIN">
                <EmployeesADMIN />
              </RoleRoute>
            }
          />
          <Route
            path="/admin/employees/add"
            element={
              <RoleRoute role="ADMIN">
                <AddEmployeeADMIN />
              </RoleRoute>
            }
          />

          <Route
            path="/admin/departments"
            element={
              <RoleRoute role="ADMIN">
                <DepartmentsADMIN />
              </RoleRoute>
            }
          />

          <Route
            path="/admin/departments/add"
            element={
              <RoleRoute role="ADMIN">
                <AddDepartmentsADMIN />
              </RoleRoute>
            }
          />

          <Route
            path="/admin/attendance"
            element={
              <RoleRoute role="ADMIN">
                <AttendanceADMIN />
              </RoleRoute>
            }
          />

          <Route
            path="/admin/audit-logs"
            element={
              <RoleRoute role="ADMIN">
                <AuditLogsADMIN />
              </RoleRoute>
            }
          />

          <Route
            path="/admin/salaries"
            element={
              <RoleRoute role="ADMIN">
                <SalariesADMIN />
              </RoleRoute>
            }
          />

          <Route
            path="/admin/leaves"
            element={
              <RoleRoute role="ADMIN">
                <LeavesADMIN />
              </RoleRoute>
            }
          />

          <Route
            path="/admin/profile"
            element={
              <RoleRoute role="ADMIN">
                <ProfileADMIN />
              </RoleRoute>
            }
          />


          {/* ================= HR ================= */}

          <Route
            path="/hr"
            element={
              <RoleRoute role="HR">
                <HRDashboard />
              </RoleRoute>
            }
          />

          <Route
            path="/hr/employees"
            element={
              <RoleRoute role="HR">
                <EmployeesHR />
              </RoleRoute>
            }
          />

          <Route
            path="/hr/employees/add"
            element={
              <RoleRoute role="HR">
                <AddEmployeeHR />
              </RoleRoute>
            }
          />

          <Route
            path="/hr/attendance"
            element={
              <RoleRoute role="HR">
                <AttendanceHR />
              </RoleRoute>
            }
          />

          <Route
            path="/hr/leaves"
            element={
              <RoleRoute role="HR">
                <LeavesHR />
              </RoleRoute>
            }
          />

          <Route
            path="/hr/salaries"
            element={
              <RoleRoute role="HR">
                <SalariesHR />
              </RoleRoute>
            }
          />


          {/* ================= EMPLOYEE ================= */}

          <Route
            path="/employee"
            element={
              <RoleRoute role="USER">
                <EmployeeDashboard />
              </RoleRoute>
            }
          />

          <Route
            path="/employee/attendance"
            element={
              <RoleRoute role="USER">
                <MyAttendanceEMP />
              </RoleRoute>
            }
          />

          <Route
            path="/employee/salary"
            element={
              <RoleRoute role="USER">
                <MySalaryEMP />
              </RoleRoute>
            }
          />

          <Route
            path="/employee/leaves"
            element={
              <RoleRoute role="USER">
                <MyLeavesEMP />
              </RoleRoute>
            }
          />

          {/* Unknown URL */}
          <Route path="*" element={<Navigate to="/" replace />} />
        </Routes>
      </BrowserRouter>
    </AuthProvider>
  );
}

export default App;
