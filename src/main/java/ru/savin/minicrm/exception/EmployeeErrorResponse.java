package ru.savin.minicrm.exception;

public class EmployeeErrorResponse {
    private int status;
    private String message;

    // todo not used constructor
    public EmployeeErrorResponse(int status, String message) {
        this.status = status;
        this.message = message;
    }

    public EmployeeErrorResponse() {
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
