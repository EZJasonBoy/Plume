package sausure.io.plume.Activity;

import sausure.io.personallibrary.Activity.BaseAppCompatActivity;
import sausure.io.plume.R;

/**
 * Created by JOJO on 2015/9/5.
 */
public abstract class BaseActivity extends BaseAppCompatActivity
{
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
}
