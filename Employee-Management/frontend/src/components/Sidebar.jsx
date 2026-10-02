import { NavLink } from "react-router-dom";
import { useState } from "react";

const Sidebar = ({ role }) => {

    const [openMenu, setOpenMenu] = useState(null);

    const menus = {
        ADMIN: [
            {
                label: "Dashboard",
                path: "/admin"
            },

            {
                label: "HR",
                children: [
                    {
                        label: "View HR",
                        path: "/admin/hr"
                    }
                ]
            },

            {
                label: "Employees",
                children: [
                    {
                        label: "View Employees",
                        path: "/admin/employees"
                    },
                    {
                        label: "Add Employee",
                        path: "/admin/employees/add"
                    }
                ]
            },

            {
                label: "Departments",
                children: [
                    {
                        label: "View Departments",
                        path: "/admin/departments"
                    },
                    {
                        label: "Add Department",
                        path: "/admin/departments/add"
                    }
                ]
            },

            {
                label: "Salaries",
                path: "/admin/salaries"
            },

            {
                label: "Attendance",
                path: "/admin/attendance"
            },

            {
                label: "Leaves",
                path: "/admin/leaves"
            },

            {
                label: "Audit Logs",
                path: "/admin/audit-logs"
            },

            {
                label: "Profile",
                path: "/admin/profile"
            }
        ],

        HR: [
            {
                label: "Dashboard",
                path: "/hr"
            },
            {
                label: "Employees",
                path: "/hr/employees"
            },
            {
                label: "Attendance",
                path: "/hr/attendance"
            },
            {
                label: "Leaves",
                path: "/hr/leaves"
            },
            {
                label: "Salaries",
                path: "/hr/salaries"
            }
        ],

        USER: [
            {
                label: "Dashboard",
                path: "/employee"
            },
            {
                label: "My Attendance",
                path: "/employee/attendance"
            },
            {
                label: "My Salary",
                path: "/employee/salary"
            },
            {
                label: "My Leaves",
                path: "/employee/leaves"
            }
        ]
    };

    const handleMenuClick = (label) => {
        setOpenMenu(openMenu === label ? null : label);
    };

    return (
        <aside className="sidebar">

            <div className="sidebar-title">
                EMS
            </div>

            <nav>

                {menus[role]?.map((menu) => {

                    // Normal menu item
                    if (!menu.children) {
                        return (
                            <NavLink
                                key={menu.path}
                                to={menu.path}
                                className={({ isActive }) =>
                                    isActive
                                        ? "sidebar-link active"
                                        : "sidebar-link"
                                }
                            >
                                {menu.label}
                            </NavLink>
                        );
                    }

                    // Menu with children
                    return (
                        <div
                            className="sidebar-group"
                            key={menu.label}
                        >

                            <button
                                className="sidebar-group-title"
                                onClick={() => handleMenuClick(menu.label)}
                            >
                                <span>{menu.label}</span>

                                <span>
                                    {openMenu === menu.label ? "−" : "+"}
                                </span>
                            </button>

                            {openMenu === menu.label && (
                                <div className="sidebar-submenu">

                                    {menu.children.map((child) => (
                                        <NavLink
                                            key={child.path}
                                            to={child.path}
                                            className={({ isActive }) =>
                                                isActive
                                                    ? "sidebar-link sidebar-sub-link active"
                                                    : "sidebar-link sidebar-sub-link"
                                            }
                                        >
                                            {child.label}
                                        </NavLink>
                                    ))}

                                </div>
                            )}

                        </div>
                    );
                })}

            </nav>

        </aside>
    );
};

export default Sidebar;






