package com.wuxiaolong.androidmvpsample.http;

public class BaseResponse<T>{

    private static final String TAG = "BaseResponse";

    public T data;

    public boolean error;

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }
}
