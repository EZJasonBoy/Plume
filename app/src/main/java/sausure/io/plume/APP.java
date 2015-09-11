package sausure.io.plume;

import android.app.Application;

import retrofit.RestAdapter;
import sausure.io.plume.Retrofit.ZhiHuService;

/**
 * Created by JOJO on 2015/9/10.
 */
public class APP extends Application
{
    private static ZhiHuService zhiHuService;

    public static ZhiHuService getZhiHuService()
    {
        if(zhiHuService == null)
            zhiHuService =  new RestAdapter
                                .Builder()
                                .setEndpoint(ZhiHuService.ZHIHU_API)
                                .build()
                                .create(ZhiHuService.class);

        return zhiHuService;
    }
}
