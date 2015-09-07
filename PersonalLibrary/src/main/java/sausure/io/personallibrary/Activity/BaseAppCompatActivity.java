package sausure.io.personallibrary.Activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.os.Build;
import android.os.Bundle;
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
import sausure.io.personallibrary.Enum.TransitionMode;
import sausure.io.personallibrary.Helper.BindNetChangeHelper;
import sausure.io.personallibrary.Helper.StartComponentHelper;
import sausure.io.personallibrary.Helper.impl.BindNetChangeHelperImpl;
import sausure.io.personallibrary.Helper.impl.StartComponentHelperImpl;
import sausure.io.personallibrary.R;

/**
 * Created by JOJO on 2015/9/1.
 */
public abstract class BaseAppCompatActivity extends AppCompatActivity
        implements BindNetChangeHelperImpl.HandleNetChangeListener
{
    /**
     * Activity Tool Bar
     */
//    @Bind(R.id.toolbar)
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
    protected BroadcastReceiver netReceiver;

    /**
     * put start component common methods into startComponentHelper
     */
    private StartComponentHelper startComponentHelper;

    /**
     * put net change monitor into bindNetChangeAgent
     */
    private BindNetChangeHelper bindNetChangeHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        context = this;
        TAG_LOG = getClass().getSimpleName();
        overridePendingTransition();
        getBundleExtras();
        setContentView(getLayoutResId());
        setStatusBarTranslucent();
        setStatusBarColor();
        startComponentHelper = new StartComponentHelperImpl<>(this);
        bindNetChangeHelper = new BindNetChangeHelperImpl(this,this);
        netReceiver = bindNetChangeHelper.bindNetChangeListener();
        onActivityCreated();
    }

    @Override
    public void setContentView(int layoutResID)
    {
        if(layoutResID == 0)
            throw new IllegalArgumentException("You must return a right contentView layout resource Id");

        super.setContentView(layoutResID);
        ButterKnife.bind(this);

        toolbar = ButterKnife.findById(this,R.id.toolbar);
        if(toolbar != null)
        {
            setSupportActionBar(toolbar);

            if(canNaviBack())
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
        LocalBroadcastManager.getInstance(context).unregisterReceiver(netReceiver);
        context = null;
    }

    @Override
    public boolean needNetWork()
    {
        return false;
    }

    @Override
    public void handleConnectivityChange(boolean isAvailable)
    {
        if(!isAvailable)
            Toast.makeText(context, R.string.network_offline, Toast.LENGTH_SHORT).show();
    }

    /**
     * convenient way to start activity
     * @param clazz
     */
    public void startActivity(Class<?> clazz)
    {
        startComponentHelper.readyGo(clazz);
    }

    /**
     * convenient way to start activity
     * @param clazz
     * @param bundle
     */
    public void startActivity(Class<?> clazz, Bundle bundle)
    {
        startComponentHelper.readyGo(clazz, bundle);
    }

    /**
     * convenient way to start activity
     * @param clazz
     * @param requestCode
     */
    public void startActivity(Class<?> clazz, int requestCode)
    {
        startComponentHelper.readyGoForResult(clazz, requestCode);
    }

    /**
     * convenient way to start activity
     * @param clazz
     * @param requestCode
     * @param bundle
     */
    public void startActivity(Class<?> clazz, int requestCode, Bundle bundle)
    {
        startComponentHelper.readyGoForResult(clazz, requestCode, bundle);
    }

    /**
     * start activity before finish
     * @param clazz
     */
    public void startActivityBeforeFinish(Class<?> clazz)
    {
        startComponentHelper.readyGo(clazz, true);
    }

    /**
     * start activity before finish
     * @param clazz
     * @param bundle
     */
    public void startActivityBeforeFinish(Class<?> clazz,Bundle bundle)
    {
        startComponentHelper.readyGo(clazz,bundle,true);
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
     * set status bar to be translucent
     */
    private void setStatusBarTranslucent()
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
        {
            Window win = getWindow();
            WindowManager.LayoutParams winParams = win.getAttributes();
            final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
            if (statusBarNeedTranslucent())
                winParams.flags |= bits;
            else
                winParams.flags &= ~bits;
        }

//        Observable.just(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
//                .filter(compatible -> compatible)
//                .map(compatible -> getWindow().getAttributes())
//                .subscribe(layoutParams -> {
//                    if (statusBarNeedTranslucent())
//                        layoutParams.flags |= WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
//                    else
//                        layoutParams.flags &= ~WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
//                });
    }

    /**
     * whether status bar needs to be translucent
     * @return
     */
    protected boolean statusBarNeedTranslucent()
    {
        return false;
    }

    /**
     * set status bar's background to target color which return from getStatusBarColor()
     */
    private void setStatusBarColor()
    {
        try
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
        catch (Exception e)
        {
            throw new IllegalArgumentException("unable to set status bar to target color");
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
     * this activity can finish by navigation
     * @return
     */
    protected boolean canNaviBack()
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
     * get this activity's layout resource
     * @return
     */
    protected abstract int getLayoutResId();

    /**
     * you should initialize all view in this method
     */
    protected abstract void onActivityCreated();
}
