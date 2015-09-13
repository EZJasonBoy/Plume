package sausure.io.plume.Fragment;

import android.support.v7.widget.RecyclerView;

import com.yalantis.phoenix.PullToRefreshView;

import butterknife.Bind;
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
    PullToRefreshView refreshView;

    @Override
    protected Presenter getPresenter() {
        return new ViewPresenter(context,this);
    }

    @Override
    protected int getLayoutResId()
    {
        return R.layout.fragment_view;
    }

    @Override
    public void initialList(RecyclerView.LayoutManager layoutManager,RecyclerView.Adapter<?> adapter,
                            RecyclerView.OnScrollListener onScrollListener)
    {
        viewList.setLayoutManager(layoutManager);
        viewList.setAdapter(adapter);
        viewList.addOnScrollListener(onScrollListener);
    }

    @Override
    public void initialRefresh(PullToRefreshView.OnRefreshListener refreshListener)
    {
        refreshView.setOnRefreshListener(refreshListener);
    }

    @Override
    public PullToRefreshView getRefreshView()
    {
        return refreshView;
    }
}
