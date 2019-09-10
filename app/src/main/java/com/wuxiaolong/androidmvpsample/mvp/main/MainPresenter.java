package com.wuxiaolong.androidmvpsample.mvp.main;

import com.wuxiaolong.androidmvpsample.bean.TestBean;
import com.wuxiaolong.androidmvpsample.mvp.other.BasePresenter;
import com.wuxiaolong.androidmvpsample.retrofit.ApiCallback;

import java.util.List;

public class MainPresenter extends BasePresenter<MainView> {

    public MainPresenter(MainView view) {
        attachView(view);
    }

    public void loadDataByRetrofitRxjava(String cityId) {
        mvpView.showLoading();
        addSubscription(
                apiStores.loadDataByRetrofitRxJava(cityId),
                new ApiCallback<MainModel>() {
                    @Override
                    public void onSuccess(MainModel model) {
                        mvpView.getDataSuccess(model);
                    }

                    @Override
                    public void onFailure(String msg) {
                        mvpView.getDataFail(msg);
                    }


                    @Override
                    public void onFinish() {
                        mvpView.hideLoading();
                    }

                });
    }

    public void loadDataByRetrofitRxjava2() {
        mvpView.showLoading();
        addSubscription(
                apiStores.getImageList2(),
                new ApiCallback<TestBean>() {
                    @Override
                    public void onSuccess(TestBean model) {
                        mvpView.getDataSuccess(model);
                    }

                    @Override
                    public void onFailure(String msg) {
                        mvpView.getDataFail(msg);
                    }


                    @Override
                    public void onFinish() {
                        mvpView.hideLoading();
                    }

                });
    }

}
