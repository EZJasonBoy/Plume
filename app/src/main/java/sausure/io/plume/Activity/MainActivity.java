package sausure.io.plume.Activity;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;

import com.ogaclejapan.smarttablayout.SmartTabLayout;

import butterknife.Bind;
import sausure.io.personallibrary.Enum.TransitionMode;
import sausure.io.plume.Presenter.MainPresenter;
import sausure.io.plume.Presenter.Presenter;
import sausure.io.plume.R;

/**
 * Created by JOJO on 2015/9/6.
 */
public class MainActivity extends BaseActivity implements MainPresenter.MainView
{
    @Bind(R.id.smart_tab)
    SmartTabLayout tabLayout;

    @Bind(R.id.view_pager)
    ViewPager viewPager;

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
    protected Presenter getPresenter() {
        return new MainPresenter(this,this);
    }

    @Override
    public void initializePagerViews(PagerAdapter pagerAdapter)
    {
        viewPager.setOffscreenPageLimit(pagerAdapter.getCount());
        viewPager.setAdapter(pagerAdapter);

        tabLayout.setViewPager(viewPager);
    }
}
