package sausure.io.plume.Activity;

import sausure.io.personallibrary.Activity.BaseAppCompatActivity;
import sausure.io.plume.Presenter.Presenter;
import sausure.io.plume.R;

/**
 * Created by JOJO on 2015/9/5.
 */
public abstract class BaseActivity extends BaseAppCompatActivity
{
    protected Presenter presenter;

    @Override
    protected void onActivityCreated()
    {
        if(presenter == null)
            presenter = getPresenter();

        if(presenter == null)
            throw new IllegalArgumentException("you must provide a Presenter implement");
        else
            presenter.initialized();
    }

    @Override
    protected boolean statusBarNeedTranslucent()
    {
        return true;
    }

    @Override
    protected int getStatusBarColor()
    {
        return getResources().getColor(R.color.primary_dark);
    }

    protected abstract Presenter getPresenter();
}
