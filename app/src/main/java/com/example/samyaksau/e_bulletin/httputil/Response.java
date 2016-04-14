package com.example.samyaksau.e_bulletin.httputil;

/**
 * @Author   Samyak Sau
 * @College Jaipur National University, Jaipur
 */
public class Response {

    private boolean isSuccess = true;
    private String result = "";
    private String message = "";

    public String getErromMessage() {
        return message;
    }

    public void setErrorMessage (String msg) {
        message = msg;
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    public void setSuccess(boolean isSuccess) {
        this.isSuccess = isSuccess;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}
