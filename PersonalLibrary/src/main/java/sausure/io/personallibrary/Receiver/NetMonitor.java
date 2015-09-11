package sausure.io.personallibrary.Receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import sausure.io.personallibrary.Enum.NetState;
import sausure.io.personallibrary.Utils.NetUtil;

/**
 * Created by JOJO on 2015/9/7.
 */
public class NetMonitor extends BroadcastReceiver
{
    /**
     * subscriber list
     */
    public static List<HandleNetChangeListener> listeners = new ArrayList<>();

    /**
     * current net state
     */
    public static NetState currentNetState;

    /**
     * register subscriber
     * @param listener
     */
    public static void registerNetworkMonitor(HandleNetChangeListener listener)
    {
        listeners.add(listener);
    }

    /**
     * unregister subscriber
     * @param listener
     */
    public static void unRegisterNetworkMonitor(HandleNetChangeListener listener)
    {
        listeners.remove(listener);
    }

    @Override
    public void onReceive(Context context, Intent intent)
    {
        Observable.just(intent.getAction())
                .filter(action -> action.equals(ConnectivityManager.CONNECTIVITY_ACTION))
                .doOnNext(action -> currentNetState = NetUtil.getNetworkState(context))
                .flatMap(action -> Observable.from(listeners))
                .subscribe(listener -> listener.handleConnectivityChange(currentNetState));
    }

    public static interface HandleNetChangeListener
    {
        /**
         * show toast to remind user if network is available
         * @param netState
         */
        void handleConnectivityChange(NetState netState);
    }
}
