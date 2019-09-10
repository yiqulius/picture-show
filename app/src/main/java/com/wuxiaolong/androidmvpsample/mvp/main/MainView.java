package com.wuxiaolong.androidmvpsample.mvp.main;

import com.wuxiaolong.androidmvpsample.bean.TestBean;

import java.util.List;

public interface MainView extends BaseView {

    void getDataSuccess(MainModel model);

    void getDataSuccess(TestBean model);

    void getDataSuccess(List<TestBean.ResultsBean> model);

    void getDataFail(String msg);

}
