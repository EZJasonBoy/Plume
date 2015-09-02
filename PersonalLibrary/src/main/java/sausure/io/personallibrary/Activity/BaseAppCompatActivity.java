package sausure.io.personallibrary.Activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.Toast;

import com.readystatesoftware.systembartint.SystemBarTintManager;

import butterknife.ButterKnife;
import rx.Observable;
import sausure.io.personallibrary.Base.LogicAgent;
import sausure.io.personallibrary.Base.TransitionMode;
import sausure.io.personallibrary.Base.impl.LogicAgentImpl;
import sausure.io.personallibrary.R;
import sausure.io.personallibrary.Utils.NetUtil;

/**
 * Created by JOJO on 2015/9/1.
 */
public abstract class BaseAppCompatActivity extends AppCompatActivity implements LogicAgent
{
    /**
     * Activity Tool Bar，remember to bind!!
     */
//    @Bind(R.id.toolbar)
    @Nullable
    public Toolbar toolbar;

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

    /**
     * put common methods into agent
     */
    private LogicAgent agent;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        context = this;
        agent = new LogicAgentImpl<>(context);
        TAG_LOG = getClass().getSimpleName();
        overridePendingTransition();
        getBundleExtras();
        setContentView(getLayoutResId());
        setStatusBarTranslucent(true);
        bindNetChangeListener();
        initializeAllView();
    }

    @Override
    public void setContentView(int layoutResID)
    {
        if(layoutResID == 0)
            throw new IllegalArgumentException("You must return a right contentView layout resource Id");

        super.setContentView(layoutResID);
        ButterKnife.bind(context);

        if(toolbar != null)
        {
            setSupportActionBar(toolbar);

            if(canSwipeBack())
                Observable.just(getSupportActionBar())
                    .filter(actionBar -> actionBar != null)
                    .doOnNext(actionBar -> actionBar.setHomeButtonEnabled(true))
                    .subscribe(actionBar ->actionBar.setDisplayHomeAsUpEnabled(true));
        }
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

    @Override
    public void readyGo(Class<?> clazz)
    {
        agent.readyGo(clazz);
    }

    @Override
    public void readyGo(Class<?> clazz, Bundle bundle)
    {
        agent.readyGo(clazz, bundle);
    }

    @Override
    public void readyGoForResult(Class<?> clazz, int requestCode)
    {
        agent.readyGoForResult(clazz, requestCode);
    }

    @Override
    public void readyGoForResult(Class<?> clazz, int requestCode, Bundle bundle)
    {
        agent.readyGoForResult(clazz, requestCode, bundle);
    }

    /**
     * when user click toolbar,scrollView can scroll to top quickly
     * @param scrollView
     * @param <S>
     */
    public <S> void scrollToTopQuickly(S scrollView)
    {
        try
        {
            if (toolbar != null)
            {
                if (scrollView instanceof AbsListView)
                    toolbar.setOnClickListener(view -> ((AbsListView) scrollView).smoothScrollToPosition(0));
                else if(scrollView instanceof RecyclerView)
                    toolbar.setOnClickListener(view -> ((RecyclerView)scrollView).smoothScrollToPosition(0));
            }
        }
        catch (Exception e)
        {
            //sorry,it does not work
            throw new RuntimeException("sorry,it can not smooth scroll to top");
        }
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
     * this activity can swipe back(I wil implement own swipe back mechanism)
     * @return
     */
    protected boolean canSwipeBack()
    {
        return false;
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

    /**
     * you should initialize all view in this method
     */
    protected abstract void initializeAllView();
}
