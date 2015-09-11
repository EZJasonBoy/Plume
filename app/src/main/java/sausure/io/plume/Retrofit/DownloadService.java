package sausure.io.plume.Retrofit;

import retrofit.client.Response;
import retrofit.http.GET;
import retrofit.http.Headers;
import retrofit.http.Path;
import rx.Observable;

/**
 * Created by JOJO on 2015/9/10.
 */
public interface DownloadService
{
    /**
     * download the start image
     * @param path
     * @return
     */
    @GET("/{path}")
    @Headers({"Content-Type: image/jpeg"})
    Observable<Response> downloadStartImage(@Path("path")String path);
}
