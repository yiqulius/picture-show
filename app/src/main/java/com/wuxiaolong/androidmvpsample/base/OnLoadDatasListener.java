package com.wuxiaolong.androidmvpsample.base;

public interface OnLoadDatasListener<T> {

    /**
     * 成功
     * @param t 数据
     */
    void onSuccess(T t);


    /**
     * 失败
     * @param error 错误信息
     */
    void onFailure(String error);

}
