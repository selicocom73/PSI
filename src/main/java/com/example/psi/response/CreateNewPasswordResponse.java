package com.example.psi.response;

public class CreateNewPasswordResponse implements Response{
    private final boolean isSuccess;

    private String error = "";

    public CreateNewPasswordResponse(boolean isSuccess) {
        this.isSuccess = isSuccess;
        this.error = "";
    }

    public CreateNewPasswordResponse(String error) {
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
