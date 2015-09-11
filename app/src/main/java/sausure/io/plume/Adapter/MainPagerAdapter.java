package sausure.io.plume.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

import sausure.io.personallibrary.Fragment.LazyFragment;

/**
 * Created by JOJO on 2015/9/10.
 */
public class MainPagerAdapter extends FragmentPagerAdapter
{
    private List<LazyFragment> fragments;

    public MainPagerAdapter(FragmentManager fm,List<LazyFragment> fragments)
    {
        super(fm);
        this.fragments = fragments;
    }

    @Override
    public Fragment getItem(int position)
    {
        if(position > fragments.size())
            return null;

        return fragments.get(position);
    }

    @Override
    public int getCount()
    {
        return fragments != null ? fragments.size() : 0;
    }

    @Override
    public CharSequence getPageTitle(int position)
    {
        return fragments != null && fragments.size() > position ?
                fragments.get(position).getArguments().getString(LazyFragment.CATEGORY) : null;
    }
}
