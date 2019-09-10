package com.wuxiaolong.androidmvpsample.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.wuxiaolong.androidmvpsample.R;
import com.wuxiaolong.androidmvpsample.base.OnLoadDatasListener;
import com.wuxiaolong.androidmvpsample.bean.BannerBean;
import com.wuxiaolong.androidmvpsample.bean.TestBean;
import com.wuxiaolong.androidmvpsample.http.BaseResponse;
import com.wuxiaolong.androidmvpsample.mvp.main.MainModel;
import com.wuxiaolong.androidmvpsample.mvp.main.MainPresenter;
import com.wuxiaolong.androidmvpsample.mvp.main.MainView;
import com.wuxiaolong.androidmvpsample.mvp.other.MvpActivity;
import com.wuxiaolong.androidmvpsample.retrofit.ApiCallback;
import com.wuxiaolong.androidmvpsample.http.BaseObserver;
import com.wuxiaolong.androidmvpsample.retrofit.RetrofitCallback;
import com.wuxiaolong.androidmvpsample.http.RetrofitFactory;

import java.util.List;
import java.util.Random;

import retrofit2.Call;

public class MainActivity extends MvpActivity<MainPresenter> implements MainView {

    private static final String TAG = "MainActivity";

    private TextView text;

    private ImageView image;
    public static TestBean testBean = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected MainPresenter createPresenter() {
        return new MainPresenter(this);
    }


    @Override
    public void getDataSuccess(MainModel model) {
    }

    @Override
    public void getDataSuccess(TestBean model) {
        dataSuccess(model);
    }

    @Override
    public void getDataSuccess(List<TestBean.ResultsBean> model) {
        dataSuccess(model);
    }

    @Override
    public void getDataFail(String msg) {
    }


                //请求接口
                /**
                 * retrofit
                 */
//                loadData2();
                /**
                 * retrofit + rxjava
                 */
//                loadDataByRetrofitRxJava_TestBean();
                /**
                 * mvp + retrofit + rxjava
                 */
//                mvpPresenter.loadDataByRetrofitRxjava2();

    private void loadData2(){
        showProgressDialog();
        Call<TestBean> call = apiStores().getImageList();
        call.enqueue(new RetrofitCallback<TestBean>() {
            @Override
            public void onSuccess(TestBean model) {
                dataSuccess(model);
                Intent i = new Intent(MainActivity.this, com.wuxiaolong.androidmvpsample.testcard.MainActivity.class);
                startActivity(i);
            }

            @Override
            public void onFailure(int code, String msg) {
                Log.e(TAG, "onFailure: " + msg );
                toastShow(msg);
            }

            @Override
            public void onThrowable(Throwable t) {
                toastShow(t.getMessage());
            }

            @Override
            public void onFinish() {
                dismissProgressDialog();
            }
        });
        addCalls(call);
    }

    private void loadDataByRetrofitRxJava_TestBean() {
        showProgressDialog();
        addSubscription(
                apiStores().getImageList2(),
                new ApiCallback<TestBean>() {

                    @Override
                    public void onSuccess(TestBean model) {
                        dataSuccess(model);
                    }

                    @Override
                    public void onFailure(String msg) {
                        toastShow(msg);
                    }

                    @Override
                    public void onFinish() {
                        dismissProgressDialog();
                    }
                });
    }

    private void loadDataByRetrofitRxJava_TestBean_ResultsBean() {
        showProgressDialog();
        addSubscription(
                apiStores().getImageList3(),
                new ApiCallback<BaseResponse<List<TestBean.ResultsBean>>>() {

                    @Override
                    public void onSuccess(BaseResponse<List<TestBean.ResultsBean>> model) {
                        dataSuccess(model);
                    }

                    @Override
                    public void onFailure(String msg) {
                        toastShow(msg);
                    }

                    @Override
                    public void onFinish() {
                        dismissProgressDialog();
                    }
                });
    }

    public void handleHomeList2(final OnLoadDatasListener<TestBean> onLoadDatasListener) {
        RetrofitFactory
                .getInstance()
                .getImageList2(new BaseObserver<TestBean>() {
                    @Override
                    protected void onSuccess(TestBean resultsBeans) throws Exception {
                        onLoadDatasListener.onSuccess(resultsBeans);
                    }

                    @Override
                    protected void onFailure(String error, boolean isNetWorkError) throws Exception {

                    }
                });
    }

    public void handleHomeList3(final OnLoadDatasListener<List<TestBean.ResultsBean>> onLoadDatasListener) {
        RetrofitFactory
                .getInstance()
                .getImageList3(new BaseObserver<List<TestBean.ResultsBean>>() {
                    @Override
                    protected void onSuccess(List<TestBean.ResultsBean> resultsBeans) throws Exception {
                        onLoadDatasListener.onSuccess(resultsBeans);
                    }

                    @Override
                    protected void onFailure(String error, boolean isNetWorkError) throws Exception {

                    }
                });
    }

    private void dataSuccess(TestBean model) {
        testBean = model;
        int name = getNum(0,model.getData().size());
//        text.setText(
//                "id: "+model.getData().get(name).get_id() + "\n" +Glide.with(this)
//                .load(model.getData().get(name).getUrl())
//                .into(image);
//                "CreatedAt: "+model.getData().get(name).getCreatedAt() + "\n" +
//                "PublishedAt: "+model.getData().get(name).getPublishedAt() + "\n" +
//                "Type: "+model.getData().get(name).getType() + "\n" +
//                "isUsed: "+model.getData().get(name).isUsed() + "\n" +
//                "Desc: "+model.getData().get(name).getDesc() + "\n" +
//                "Who: " + model.getData().get(name).getWho() + "\n" +
//                "Source: " + model.getData().get(name).getSource()
//        );



    }

    private void dataSuccess(List<TestBean.ResultsBean> model) {
        Log.e(TAG, "dataSuccess: " + model);
        text.setText(model.get(1).getWho());
    }

    private void dataSuccess(BaseResponse<List<TestBean.ResultsBean>> model) {
        Log.e(TAG, "dataSuccess: " + model);
//        text.setText(model.data.get(1).getWho());
    }

    public static int getNum(int startNum,int endNum){
        if(endNum > startNum){
            Random random = new Random();
            return random.nextInt(endNum - startNum) + startNum;
        }
        return 0;
    }
}
