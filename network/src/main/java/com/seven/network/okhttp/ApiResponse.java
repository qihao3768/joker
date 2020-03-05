package com.seven.network.okhttp;

public class ApiResponse<T> {
    public boolean success;
    public int status;
    public String message;
    public T body;
}
