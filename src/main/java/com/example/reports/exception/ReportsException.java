package com.example.reports.exception;

import java.util.ArrayList;
import java.util.List;

public class ReportsException extends RuntimeException{
    private static final long serialVersionUID = 1L;
    protected int statusCode;
    protected List<Error> errors;

    public ReportsException() {
    }

    public ReportsException(Throwable cause) {
        super(cause);
    }

    public ReportsException(List<Error> errors) {
        this.errors = errors;
    }

    public ReportsException(List<Error> errors, String message) {
        super(message);
        this.errors = errors;
    }

    public ReportsException(Error error) {
        super(error.getMessage());
        this.errors = new ArrayList();
        this.errors.add(error);
    }

    public ReportsException(List<Error> errors, String message, int statusCode) {
        super(message);
        this.statusCode = statusCode;
        this.errors = errors;
    }

    public List<Error> getErrorList() {
        return this.errors;
    }

    public void setErrorList(List<Error> errors) {
        this.errors = errors;
    }

    public int getStatusCode() {
        return this.statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }
}
