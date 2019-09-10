package com.wuxiaolong.androidmvpsample.http;


import android.util.Log;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;


public abstract class BaseObserver<T> implements Observer<BaseResponse<T>> {

    private static final String TAG = "BaseObserver";

    protected BaseObserver() {
    }
    @Override
    public void onNext(BaseResponse<T> tBaseEntity) {

        if (!tBaseEntity.isError()) {
            Log.e(TAG, "onNext: " + tBaseEntity.getData() );
            try {
                onSuccess(tBaseEntity.data);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    protected abstract void onSuccess(T t) throws Exception;

    protected abstract void onFailure(String error, boolean isNetWorkError) throws Exception;

    @Override
    public void onComplete() {
    }

    @Override
    public void onError(Throwable e) {

    }

    @Override
    public void onSubscribe(Disposable d) {
    }
}
