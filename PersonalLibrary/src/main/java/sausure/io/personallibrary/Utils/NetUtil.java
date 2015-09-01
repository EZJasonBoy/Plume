package sausure.io.personallibrary.Utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by JOJO on 2015/9/1.
 */
public class NetUtil
{
    /**
     * return whether network is available
     * @param context
     * @return
     */
    public static boolean isNetworkAvailable(Context context)
    {
        if (context != null)
        {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context.
                    getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();

            if (mNetworkInfo != null)
                return mNetworkInfo.isAvailable();
        }
        return false;
    }
}
