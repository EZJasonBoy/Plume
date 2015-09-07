package sausure.io.personallibrary.Helper.impl;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.support.v4.content.LocalBroadcastManager;

import rx.Observable;
import sausure.io.personallibrary.Helper.BindNetChangeHelper;
import sausure.io.personallibrary.Utils.NetUtil;

/**
 * Created by JOJO on 2015/9/3.
 */
public class BindNetChangeHelperImpl implements BindNetChangeHelper
{
    private HandleNetChangeListener handleNetChangeListener;
    private Context context;

    public BindNetChangeHelperImpl(Context context, HandleNetChangeListener handleNetChangeListener)
    {
        this.context = context;
        this.handleNetChangeListener = handleNetChangeListener;
    }

    @Override
    public BroadcastReceiver bindNetChangeListener()
    {
        if(!handleNetChangeListener.needNetWork())
            return null;

        BroadcastReceiver netReceiver = new BroadcastReceiver()
            {
                @Override
                public void onReceive(Context context, Intent intent)
                {
                    Observable.just(intent.getAction())
                            .filter(action -> action.equals(ConnectivityManager.CONNECTIVITY_ACTION))
                            .map(filter -> NetUtil.isNetworkAvailable(context))
                            .subscribe(isAvailable -> handleNetChangeListener.handleConnectivityChange(isAvailable));
                }
            };
        LocalBroadcastManager.getInstance(context).registerReceiver(netReceiver,
                new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));

        return netReceiver;
    }

    public interface HandleNetChangeListener
    {
        /**
         * show toast to remind user if network is available
         * @param isAvailable
         */
        void handleConnectivityChange(boolean isAvailable);

        /**
         * whether you want to monitor network change
         * @return
         */
        boolean needNetWork();
    }
}
