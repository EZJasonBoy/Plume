package sausure.io.plume.Retrofit;

import retrofit.http.GET;
import retrofit.http.Path;
import rx.Observable;
import sausure.io.plume.Retrofit.Entity.StartImage;
import sausure.io.plume.Retrofit.Entity.ViewList;

/**
 * Created by JOJO on 2015/9/6.
 */
public interface ZhiHuService
{
    String ZHIHU_API = "http://news-at.zhihu.com/api/4";
    String START_IMAGE = "1080*1776";
    String LATEST = "latest";
    String BEFORE = "before/";
    int startDay = 20130520;

    /**
     * get splash activity's start image
     * @param img
     * @return
     */
    @GET("/start-image/{img}")
    Observable<StartImage> getStartImage(@Path("img")String img);

    @GET("/news/latest")
    Observable<ViewList> getLatestViews();

    @GET("/news/before/{before}")
    Observable<ViewList> getBeforeViews(@Path("before")String before);
}
