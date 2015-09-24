package sausure.io.plume.Retrofit;

import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Path;
import sausure.io.plume.Retrofit.Entity.StartImage;
import sausure.io.plume.Retrofit.Entity.ViewList;
import sausure.io.plume.Retrofit.Entity.ViewPointDetail;

/**
 * Created by JOJO on 2015/9/6.
 */
public interface ZhiHuService
{
    String ZHIHU_API = "http://news-at.zhihu.com/api/4/";
    String START_IMAGE = "1080*1776";
    int startDay = 20130520;

    /**
     * get splash activity's start image
     * @param img
     * @return
     */
    @GET("start-image/{img}")
    Call<StartImage> getStartImage(@Path("img")String img);

    @GET("news/latest")
    Call<ViewList> getLatestViews();

    @GET("news/before/{before}")
    Call<ViewList> getBeforeViews(@Path("before")String before);

    @GET("news/{id}")
    Call<ViewPointDetail> getViewPointDetail(@Path("id")int id);
}
