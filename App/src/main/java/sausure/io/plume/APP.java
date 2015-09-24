package sausure.io.plume;

import android.app.Application;

import com.squareup.okhttp.OkHttpClient;

import retrofit.Call;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;
import rx.Observable;
import sausure.io.plume.Retrofit.ZhiHuService;

/**
 * Created by JOJO on 2015/9/10.
 */
public class APP extends Application
{
    private static OkHttpClient okHttpClient;

    private static ZhiHuService zhiHuService;

    public static OkHttpClient getOkHttpClient()
    {
        if(okHttpClient == null)
            okHttpClient = new OkHttpClient();

        return okHttpClient;
    }

    public static ZhiHuService getZhiHuService()
    {
        if(zhiHuService == null)
            zhiHuService =  new Retrofit
                                .Builder()
                                .client(getOkHttpClient())
                                .baseUrl(ZhiHuService.ZHIHU_API)
                                .addConverterFactory(GsonConverterFactory.create())
                                .build()
                                .create(ZhiHuService.class);

        return zhiHuService;
    }

    /**
     * retrofit2.0 does not provide support to Rxjava,so i write a method to toggle
     * @param call
     * @param <R>
     * @return
     */
    public static <R> Observable<R> toggleRetrofitCall(Call<R> call)
    {
        return Observable.create((subscriber -> {
            try {
                Response<R> response = call.execute();

                if (response == null || !response.isSuccess())
                    throw new Exception("failed to request Network");

                subscriber.onNext(response.body());
            } catch (Exception e)
            {
                e.printStackTrace();
                subscriber.onError(e);
            }
            finally
            {
                subscriber.onCompleted();
            }
        }));
    }
}
