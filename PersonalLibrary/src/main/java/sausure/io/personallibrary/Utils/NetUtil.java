package sausure.io.personallibrary.Utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import sausure.io.personallibrary.Enum.NetState;

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

    /**
     * return the network state
     * @param context
     * @return
     */
    public static NetState getNetworkState(Context context)
    {
        ConnectivityManager connManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        // Wifi
        NetworkInfo.State state = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState();
        if (state == NetworkInfo.State.CONNECTED || state == NetworkInfo.State.CONNECTING)
            return NetState.WIFI;

        // 3G
        state = connManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState();
        if (state == NetworkInfo.State.CONNECTED || state == NetworkInfo.State.CONNECTING)
            return NetState.MOBILE;

        return NetState.NONE;
    }
}
