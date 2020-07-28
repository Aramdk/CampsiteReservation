package com.aram.demo.Responses;

public class ObjectResponse {
    int statusCode;
    Object responseBody;
    String errorMessage;

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public void setResponseBody(Object responseBody) {
        this.responseBody = responseBody;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public Object getResponseBody() {
        return responseBody;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
