package com.example.BlaBlaBackend.Dto;

import org.springframework.http.HttpStatus;

public class ApiResponse{
    private  String message;
    private  Object data;
    private HttpStatus httpStatus;

    public ApiResponse() {

    }

    public String getMessage() {
        return message;
    }

    public Object getData() {
        return data;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public void setHttpStatus(HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
