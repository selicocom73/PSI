package com.example.psi.response;

public class RemindPasswordResponse implements Response {

    private final boolean isSuccess;

    private String error = "";

    public RemindPasswordResponse(boolean isSuccess) {
        this.isSuccess = isSuccess;
        this.error = "";
    }

    public RemindPasswordResponse(String error) {
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
