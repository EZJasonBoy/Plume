package sausure.io.personallibrary.Activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.readystatesoftware.systembartint.SystemBarTintManager;

import butterknife.ButterKnife;
import rx.Observable;
import sausure.io.personallibrary.Base.TransitionMode;
import sausure.io.personallibrary.R;
import sausure.io.personallibrary.Utils.NetUtil;

/**
 * Created by JOJO on 2015/9/1.
 */
public abstract class BaseAppCompatActivity extends AppCompatActivity
{
    /**
     * Log tag
     */
    protected String TAG_LOG;

    /**
     * Activity Context
     */
    protected Activity context;

    /**
     * monitor net change
     */
    protected BroadcastReceiver NetReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        context = this;
        TAG_LOG = getClass().getSimpleName();
        overridePendingTransition();
        getBundleExtras();
        setContentView(getLayoutResId());
        setStatusBarTranslucent(true);
        bindNetChangeListener();
    }

    @Override
    public void setContentView(View view)
    {
        super.setContentView(view);
        ButterKnife.bind(context);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        if (item.getItemId() == android.R.id.home)
        {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void finish()
    {
        super.finish();
        overridePendingTransition();
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        ButterKnife.unbind(this);
        LocalBroadcastManager.getInstance(context).unregisterReceiver(NetReceiver);
        context = null;
    }

    /**
     * get bundle which emitted by the previous component
     */
    private void getBundleExtras()
    {
        Observable.just(getIntent().getExtras())
                .filter(extras -> extras != null)
                .subscribe(extras -> handleBundleExtras(extras));
    }

    /**
     * handle the extras which is not null
     * @param extras
     */
    protected void handleBundleExtras(Bundle extras) {}


    /**
     * override pending transition if toggleOverridePendingTransition() return
     * true and getOverridePendingTransitionMode() return TransitionMode Enum
     */
    private void overridePendingTransition()
    {
        if (toggleOverridePendingTransition())
        {
            switch (getOverridePendingTransitionMode())
            {
                case LEFT:
                    overridePendingTransition(R.anim.left_in,R.anim.left_out);
                    break;
                case RIGHT:
                    overridePendingTransition(R.anim.right_in,R.anim.right_out);
                    break;
                case TOP:
                    overridePendingTransition(R.anim.top_in,R.anim.top_out);
                    break;
                case BOTTOM:
                    overridePendingTransition(R.anim.bottom_in,R.anim.bottom_out);
                    break;
                case SCALE:
                    overridePendingTransition(R.anim.scale_in,R.anim.scale_out);
                    break;
                case FADE:
                    overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
                    break;

                default:
                    break;
            }
        }
    }

    /**
     * monitor network change
     */
    private void bindNetChangeListener()
    {
        if(needNetWork())
        {
            if(NetReceiver == null)
                NetReceiver = new BroadcastReceiver()
                {
                    @Override
                    public void onReceive(Context context, Intent intent)
                    {
                        Observable.just(intent.getAction())
                                .filter(action -> action.equals(ConnectivityManager.CONNECTIVITY_ACTION))
                                .map(filter -> NetUtil.isNetworkAvailable(context))
                                .subscribe(isAvailable -> handleConnectivityChange(isAvailable));
                    }
                };
            LocalBroadcastManager.getInstance(context).registerReceiver(NetReceiver,
                    new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        }
    }

    /**
     * set status bar translucent
     * @param on
     */
    protected void setStatusBarTranslucent(boolean on)
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
        {
            Window win = getWindow();
            WindowManager.LayoutParams winParams = win.getAttributes();
            final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
            if (on)
                winParams.flags |= bits;
            else
                winParams.flags &= ~bits;
        }
    }

    /**
     * use tintColor to draw status bar's background
     * @param tintColor
     */
    protected void setStatusBarColor(int tintColor)
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
        {
            SystemBarTintManager mTintManager = new SystemBarTintManager(this);
            if (tintColor != 0)
            {
                mTintManager.setStatusBarTintEnabled(true);
                mTintManager.setTintColor(tintColor);
            }
            else
            {
                mTintManager.setStatusBarTintEnabled(false);
                mTintManager.setTintDrawable(null);
            }
        }
    }

    /**
     * whether you want to monitor network change
     * @return
     */
    protected  boolean needNetWork()
    {
        return false;
    }

    /**
     * whether you want to override Pending transition
     * @return
     */
    protected boolean toggleOverridePendingTransition()
    {
        return false;
    }

    /**
     * get transition mode you want
     * @return
     */
    protected TransitionMode getOverridePendingTransitionMode()
    {
        return null;
    }

    /**
     * show toast to remind user if network is available
     * @param isAvailable
     */
    protected void handleConnectivityChange(boolean isAvailable)
    {
        if(!isAvailable)
            Toast.makeText(context, R.string.network_offline,Toast.LENGTH_SHORT).show();
    }

    /**
     * get this activity's layout resource
     * @return
     */
    protected abstract int getLayoutResId();
}
