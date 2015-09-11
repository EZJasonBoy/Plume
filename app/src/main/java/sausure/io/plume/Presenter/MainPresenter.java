package sausure.io.plume.Presenter;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;

import java.util.ArrayList;
import java.util.List;

import sausure.io.personallibrary.Fragment.LazyFragment;
import sausure.io.plume.Adapter.MainPagerAdapter;
import sausure.io.plume.Fragment.BaseFragment;
import sausure.io.plume.Fragment.ViewFragment;
import sausure.io.plume.R;

/**
 * Created by JOJO on 2015/9/9.
 */
public class MainPresenter
{
    private MainView mainView;
    private MainModel mainModel;

    public MainPresenter(FragmentActivity activity,MainView mainView)
    {
        this.mainView = mainView;
        this.mainModel = new MainModelImpl(activity);
    }

    public void initialized()
    {
        mainView.initializePagerViews(mainModel.getPagerAdapter());
    }

    private class MainModelImpl implements MainModel
    {
        private FragmentActivity activity;

        public MainModelImpl(FragmentActivity activity)
        {
            this.activity = activity;
        }

        private List<LazyFragment> getFragments()
        {
            String[] category = activity.getResources().getStringArray(R.array.category);
            List<LazyFragment> fragments = new ArrayList<>(category.length);

            for (int i = 0;i < category.length;++i)
            {
                switch (i)
                {
                    case 0:
                        LazyFragment view = new ViewFragment();
                        view.setArguments(getBundle(category[i]));
                        fragments.add(view);
                        break;

                    default:
                        LazyFragment test = new BaseFragment();
                        test.setArguments(getBundle(category[i]));
                        fragments.add(test);
                }
            }

            return fragments;
        }

        private Bundle getBundle(String category)
        {
            Bundle bundle = new Bundle();
            bundle.putString(LazyFragment.CATEGORY,category);
            return bundle;
        }

        @Override
        public PagerAdapter getPagerAdapter()
        {
            return new MainPagerAdapter(activity.getSupportFragmentManager(),getFragments());
        }
    }

    public interface MainView
    {
        void initializePagerViews(PagerAdapter pagerAdapter);
    }

    public interface MainModel
    {
        PagerAdapter getPagerAdapter();
    }
}
