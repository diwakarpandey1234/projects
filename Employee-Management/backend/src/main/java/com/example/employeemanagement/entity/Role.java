package com.example.employeemanagement.entity;

import java.util.Set;

public enum Role {

    ADMIN(Set.of(
            Permission.EMPLOYEE_READ,
            Permission.EMPLOYEE_WRITE,
            Permission.EMPLOYEE_DELETE,

            Permission.DEPARTMENT_READ,
            Permission.DEPARTMENT_WRITE,
            Permission.DEPARTMENT_DELETE,

            Permission.SALARY_READ,
            Permission.SALARY_WRITE,
            Permission.SALARY_DELETE,

            Permission.ATTENDANCE_READ,
            Permission.ATTENDANCE_WRITE,

            Permission.LEAVE_READ,
            Permission.LEAVE_APPLY,
            Permission.LEAVE_APPROVE,
            Permission.LEAVE_REJECT,
            Permission.LEAVE_CANCEL,

            Permission.NOTIFICATION_READ,
            Permission.NOTIFICATION_WRITE,

            Permission.AUDIT_READ,

            Permission.DASHBOARD_READ
    )),

    HR(Set.of(
            Permission.EMPLOYEE_READ,
            Permission.EMPLOYEE_WRITE,

            Permission.DEPARTMENT_READ,

            Permission.SALARY_READ,

            Permission.ATTENDANCE_READ,
            Permission.ATTENDANCE_WRITE,

            Permission.LEAVE_READ,
            Permission.LEAVE_APPROVE,
            Permission.LEAVE_REJECT,

            Permission.NOTIFICATION_READ,
            Permission.NOTIFICATION_WRITE,

            Permission.AUDIT_READ,

            Permission.DASHBOARD_READ
    )),

    USER(Set.of(
            Permission.EMPLOYEE_READ,

            Permission.ATTENDANCE_READ,
            Permission.ATTENDANCE_WRITE,

            Permission.LEAVE_READ,
            Permission.LEAVE_APPLY,
            Permission.LEAVE_CANCEL,

            Permission.NOTIFICATION_READ
    ));


    private final Set<Permission>permission;

    Role(Set<Permission> permission) {
        this.permission=permission;
    }

    public Set<Permission>getPermission(){
        return permission;
    }
}
