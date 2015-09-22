package sausure.io.personallibrary.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;

import com.readystatesoftware.systembartint.SystemBarTintManager;

import butterknife.ButterKnife;
import rx.Observable;
import sausure.io.personallibrary.Enum.NetState;
import sausure.io.personallibrary.Enum.TransitionMode;
import sausure.io.personallibrary.R;
import sausure.io.personallibrary.Receiver.NetMonitor;

/**
 * Created by JOJO on 2015/9/1.
 */
public abstract class BaseAppCompatActivity extends AppCompatActivity
        implements NetMonitor.HandleNetChangeListener
{
    /**
     * Log tag
     */
    protected String TAG_LOG;

    /**
     * Activity
     */
    protected Activity activity;

    /**
     * root view
     */
    protected View contentView;

    /**
     * whether can finish
     */
    private boolean canFinish = false;

    /**
     * post delay runnable
     */
    private Runnable toggleFinish = ()-> canFinish = false;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        activity = this;
        TAG_LOG = getClass().getSimpleName();

        overridePendingTransition();
        getBundleExtras();
        setContentView(getLayoutResId());
        setStatusBarColor();

        if(monitorNetWork())
            NetMonitor.registerNetworkMonitor(this);

        onActivityCreated();
    }

    @Override
    public void setContentView(int layoutResID)
    {
        if(layoutResID == 0)
            throw new IllegalArgumentException("You must return a right contentView layout resource Id");

        super.setContentView(layoutResID);
        ButterKnife.bind(this);
        contentView = ButterKnife.findById(this, android.R.id.content);
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
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if(event.getKeyCode() == KeyEvent.KEYCODE_BACK && doubleClickFinish())
            return handleDoubleBackClick();

        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        ButterKnife.unbind(this);

        if(monitorNetWork())
            NetMonitor.unRegisterNetworkMonitor(this);

        activity = null;
        contentView = null;
    }

    /**
     * whether you want to monitor network change
     * @return
     */
    protected boolean monitorNetWork()
    {
        return false;
    }

    @Override
    public void handleConnectivityChange(NetState netState)
    {
        if(netState == NetState.NONE)
            getSnackbar().show();
    }

    /**
     * bind action to Network setting
     * @return
     */
    private Snackbar getSnackbar()
    {
        Snackbar bar = Snackbar.make(contentView, R.string.network_offline, Snackbar.LENGTH_SHORT);
        bar.setAction(R.string.toggleNetwork, (view -> startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS))));
        return bar;
    }

    /**
     * handle KEYCODE_BACK
     * @return
     */
    private boolean handleDoubleBackClick()
    {
        if(!canFinish)
        {
            canFinish = true;
            Snackbar.make(contentView,getString(R.string.finish_tip),Snackbar.LENGTH_SHORT).show();
            new Handler().postDelayed(toggleFinish,2000);
        }
        else
            finish();

        return true;
    }

    /**
     * whether this activity is in the bottom
     * @return
     */
    protected boolean doubleClickFinish()
    {
        return false;
    }

//    /**
//     * when user click toolbar,scrollView can scroll to top quickly
//     * @param scrollView
//     * @param <S>
//     */
//    public <S> void scrollToTopQuickly(S scrollView)
//    {
//        try
//        {
//            if (toolbar != null)
//            {
//                if (scrollView instanceof AbsListView)
//                    toolbar.setOnClickListener(view -> ((AbsListView) scrollView).smoothScrollToPosition(0));
//                else if(scrollView instanceof RecyclerView)
//                    toolbar.setOnClickListener(view -> ((RecyclerView)scrollView).smoothScrollToPosition(0));
//            }
//        }
//        catch (Exception e)
//        {
//            //sorry,it does not work
//            throw new RuntimeException("sorry,it can not smooth scroll to top");
//        }
//    }

    /**
     * get bundle which emitted by the previous component
     */
    private void getBundleExtras()
    {
        Observable.just(getIntent().getExtras())
                .filter(extras -> extras != null)
                .subscribe(this::handleBundleExtras);
    }

    /**
     * handle the extras which is not null
     * @param extras
     */
    protected void handleBundleExtras(Bundle extras) {}


    /**
     * override pending transition which
     * TransitionMode return from getOverridePendingTransitionMode()
     */
    private void overridePendingTransition()
    {
        TransitionMode mode = getOverridePendingTransitionMode();

        if(mode != null)
            switch (mode)
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

    /**
     * set status bar's background to target color which return from getStatusBarColor()
     */
    private void setStatusBarColor()
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP)
        {
            int tintColor = getStatusBarColor();
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
     * return the status bar background color
     * @return
     */
    protected int getStatusBarColor()
    {
        return 0;
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
     * get this activity's layout resource
     * @return
     */
    protected abstract int getLayoutResId();

    /**
     * you should initialize all view in this method
     */
    protected abstract void onActivityCreated();
}
