package com.example.psi.response;

public class LoginResponse implements Response{

    private final boolean isSuccess;

    private String error = "";

    public LoginResponse(boolean isSuccess) {
        this.isSuccess = isSuccess;
        this.error = "";
    }

    public LoginResponse(String error) {
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
