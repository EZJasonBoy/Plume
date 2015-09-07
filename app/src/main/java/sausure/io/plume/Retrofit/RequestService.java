package sausure.io.plume.Retrofit;

import retrofit.client.Response;
import retrofit.http.GET;
import retrofit.http.Headers;
import retrofit.http.Path;
import rx.Observable;
import sausure.io.plume.Retrofit.Bean.StartImage;

/**
 * Created by JOJO on 2015/9/6.
 */
public interface RequestService
{
    String ZHIHU_API = "http://news-at.zhihu.com/api/4";
    String START_IMAGE = "1080*1776";

    /**
     * get splash activity's start image
     * @param img
     * @return
     */
    @GET("/start-image/{img}")
    Observable<StartImage> getStartImage(@Path("img")String img);

    /**
     * download the start image
     * @param path
     * @return
     */
    @GET("/{path}")
    @Headers({"Content-Type: image/jpeg"})
    Observable<Response> downloadStartImage(@Path("path")String path);
}
