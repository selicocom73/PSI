package com.example.psi.response;

public class ChangePasswordResponse implements Response {

    private final boolean isSuccess;

    private String error = "";

    public ChangePasswordResponse(boolean isSuccess) {
        this.isSuccess = isSuccess;
        this.error = "";
    }

    public ChangePasswordResponse(String error) {
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
