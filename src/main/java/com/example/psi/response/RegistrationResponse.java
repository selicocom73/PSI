package com.example.psi.response;

public class RegistrationResponse implements Response {

    private final boolean isSuccess;

    private String error = "";

    public RegistrationResponse(boolean isSuccess) {
        this.isSuccess = isSuccess;
        this.error = "";
    }

    public RegistrationResponse(String error) {
        this.isSuccess = false;
        this.error = error;
    }

    @Override
    public boolean isSuccess() {
        return isSuccess;
    }

    @Override
    public String error() {
        return error;
    }
}
