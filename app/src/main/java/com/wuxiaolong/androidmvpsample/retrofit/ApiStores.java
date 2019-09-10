package com.wuxiaolong.androidmvpsample.retrofit;

import com.wuxiaolong.androidmvpsample.bean.BannerBean;
import com.wuxiaolong.androidmvpsample.bean.TestBean;
import com.wuxiaolong.androidmvpsample.http.BaseResponse;
import com.wuxiaolong.androidmvpsample.mvp.main.MainModel;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ApiStores {
    //baseUrl
//    String API_SERVER_URL = "http://www.weather.com.cn/";
    String API_SERVER_URL = "http://gank.io/api/data/%E7%A6%8F%E5%88%A9/";
//    String API_SERVER_URL = "https://www.wanandroid.com/";
    //网络请求时长
    int HTTP_TIME = 0;


    @GET("banner/json")
    Call<BannerBean> getBanner();

    //加载天气
    @GET("adat/sk/{cityId}.html")
    Call<MainModel> loadDataByRetrofit(@Path("cityId") String cityId);

    //加载天气
    @GET("adat/sk/{cityId}.html")
    Observable<MainModel> loadDataByRetrofitRxJava(@Path("cityId") String cityId);

    @GET("1000/1")
    Call<TestBean> getImageList();

    @GET("1000/1")
    Observable<TestBean> getImageList2();

    @GET("1000/1")
    Observable<BaseResponse<List<TestBean.ResultsBean>>> getImageList3();

}
