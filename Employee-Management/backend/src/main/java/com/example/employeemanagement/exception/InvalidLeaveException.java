package com.example.employeemanagement.exception;

public class InvalidLeaveException extends RuntimeException {

    public InvalidLeaveException(String message) {
        super(message);
    }
}