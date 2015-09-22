package sausure.io.personallibrary.Fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import rx.Observable;

/**
 * Created by JOJO on 2015/9/9.
 */
public abstract class LazyFragment extends Fragment
{
    public static final String CATEGORY = "category";

    /**
     * Log tag
     */
    protected String TAG_LOG;

    /**
     * activity
     */
    protected Activity activity;

    /**
     *
     */
    private boolean isFirstVisible;

    /**
     *
     */
    private boolean isPrepared;

    @Override
    public void onAttach(Activity activity)
    {
        super.onAttach(activity);
        this.activity = activity;
        TAG_LOG = this.getClass().getSimpleName();
        isFirstVisible = true;
        isPrepared = false;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        getBundleExtras();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        if(getLayoutResId() != 0)
            return inflater.inflate(getLayoutResId(),null);
        else
            return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
    }

    @Override
    public void onDestroyView()
    {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void onDetach()
    {
        super.onDetach();
        activity = null;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
        togglePrepare();
    }

    @Override
    public void onResume()
    {
        super.onResume();

        if(!isFirstVisible && getUserVisibleHint())
            onResumeActually();
    }

    @Override
    public void onPause()
    {
        super.onPause();

        if(!isFirstVisible && getUserVisibleHint())
            onPauseActually();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser)
    {
        super.setUserVisibleHint(isVisibleToUser);

        if(isVisibleToUser)
        {
            if(isFirstVisible)
            {
                togglePrepare();
                isFirstVisible = false;
            }
            else
                onResumeActually();
        }
        else
            onPauseActually();
    }

    /**
     * invoke onFirstUserVisible() or change isPrepared
     */
    private void togglePrepare()
    {
        if(isPrepared)
            onFirstUserVisible();
        else
            isPrepared = true;
    }

    /**
     * getArguments
     */
    private void getBundleExtras()
    {
        Observable.just(getArguments())
                .filter(bundle -> bundle != null)
                .subscribe(this::handleBundleExtras);
    }

    protected void handleBundleExtras(Bundle bundle){}

    protected void onPauseActually(){}

    protected void onResumeActually(){}

    protected abstract void onFirstUserVisible();

    protected abstract int getLayoutResId();
}
