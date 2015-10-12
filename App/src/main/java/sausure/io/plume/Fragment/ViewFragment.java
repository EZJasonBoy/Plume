package sausure.io.plume.Fragment;

import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.io.Serializable;

import butterknife.Bind;
import sausure.io.personallibrary.Utils.ActivityUtil;
import sausure.io.personallibrary.Utils.LogUtil;
import sausure.io.plume.Activity.ViewDetailActivity;
import sausure.io.plume.Presenter.Presenter;
import sausure.io.plume.Presenter.ViewPresenter;
import sausure.io.plume.R;

/**
 * Created by JOJO on 2015/9/10.
 */
public class ViewFragment extends BaseFragment implements ViewPresenter.ViewView
{
    @Bind(R.id.view_list)
    RecyclerView viewList;

    @Bind(R.id.pull_to_refresh)
    SwipeRefreshLayout refreshView;

    @Override
    protected Presenter getPresenter() {
        return new ViewPresenter(activity,this);
    }

    @Override
    protected int getLayoutResId()
    {
        return R.layout.fragment_view;
    }

    @Override
    public void initialList(RecyclerView.LayoutManager layoutManager,
                            RecyclerView.Adapter<?> adapter,
                            RecyclerView.OnScrollListener onScrollListener,
                            RecyclerView.OnItemTouchListener onItemTouchListener)
    {
        viewList.setLayoutManager(layoutManager);
        viewList.setAdapter(adapter);
        viewList.addOnScrollListener(onScrollListener);
        viewList.addOnItemTouchListener(onItemTouchListener);
    }

    @Override
    public void initialRefresh(SwipeRefreshLayout.OnRefreshListener refreshListener)
    {
        refreshView.setColorSchemeColors(R.color.green,R.color.yellow,R.color.red,R.color.blue,R.color.brown);
        refreshView.setOnRefreshListener(refreshListener);
    }

    @Override
    public SwipeRefreshLayout getRefreshView()
    {
        return refreshView;
    }

    @Override
    public boolean onItemClick(View view,Serializable tag, int position)
    {
        LogUtil.i("position: " + position + " on touch");

        Bundle bundle = new Bundle();
        bundle.putSerializable(ViewDetailActivity.VIEW_DETAIL, tag);

        ActivityOptionsCompat options = ActivityOptionsCompat
                .makeSceneTransitionAnimation(activity,view.findViewById(R.id.image),getString(R.string.view_img_transition));
        ActivityUtil.readyGoWithAnimation(activity, ViewDetailActivity.class, bundle, options);

        return true;
    }
}
