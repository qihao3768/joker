package com.seven.joker.network;

public class ApiResponse<T> {
    public boolean success;
    public int status;
    public String message;
    public T body;
}
