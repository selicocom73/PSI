package com.example.psi.response;

public class VerifyCodeResponse implements Response {
    private final boolean isSuccess;

    private String error = "";

    public VerifyCodeResponse(boolean isSuccess) {
        this.isSuccess = isSuccess;
        this.error = "";
    }

    public VerifyCodeResponse(String error) {
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
