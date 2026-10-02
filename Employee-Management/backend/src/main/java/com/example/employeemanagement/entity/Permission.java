package com.example.employeemanagement.entity;

public enum Permission {
    // Employee
    EMPLOYEE_READ,
    EMPLOYEE_WRITE,
    EMPLOYEE_DELETE,

    // Department
    DEPARTMENT_READ,
    DEPARTMENT_WRITE,
    DEPARTMENT_DELETE,

    // Salary
    SALARY_READ,
    SALARY_WRITE,
    SALARY_DELETE,

    // Attendance
    ATTENDANCE_READ,
    ATTENDANCE_WRITE,

    // Leave
    LEAVE_READ,
    LEAVE_APPLY,
    LEAVE_APPROVE,
    LEAVE_REJECT,
    LEAVE_CANCEL,

    // Notifications
    NOTIFICATION_READ,
    NOTIFICATION_WRITE,

    // Audit
    AUDIT_READ,

    // dashboard
    DASHBOARD_READ
}
