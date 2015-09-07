package sausure.io.plume.Activity;

import sausure.io.personallibrary.Enum.TransitionMode;
import sausure.io.plume.R;

/**
 * Created by JOJO on 2015/9/6.
 */
public class MainActivity extends BaseActivity
{
    @Override
    protected int getLayoutResId()
    {
        return R.layout.activity_main;
    }

    @Override
    protected boolean doubleClickFinish()
    {
        return true;
    }

    @Override
    protected boolean toggleOverridePendingTransition()
    {
        return true;
    }

    @Override
    protected TransitionMode getOverridePendingTransitionMode()
    {
        return TransitionMode.FADE;
    }

    @Override
    public boolean monitorNetWork()
    {
        return true;
    }

    @Override
    protected void onActivityCreated()
    {

    }
}
