package sausure.io.plume.Presenter;

import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.yalantis.phoenix.PullToRefreshView;

import java.util.List;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import sausure.io.personallibrary.Utils.StringUtil;
import sausure.io.plume.APP;
import sausure.io.plume.Adapter.ViewListAdapter;
import sausure.io.plume.Retrofit.Entity.ViewList;
import sausure.io.plume.Retrofit.Entity.ViewPoint;
import sausure.io.plume.Retrofit.ZhiHuService;

/**
 * Created by JOJO on 2015/9/10.
 */
public class ViewPresenter
{
    private ViewView viewView;
    private ViewModel viewModel;
    private ViewListAdapter adapter;

    public ViewPresenter(ViewView viewView)
    {
        this.viewView = viewView;
        viewModel = new ViewModelImpl();
    }

    public void initialized()
    {
        if(adapter == null)
            adapter = new ViewListAdapter();

        LoadLatest().subscribe(viewPoints -> Log.i("test","onNext"),e->Log.i("test","onError"),()->Log.i("test","onComplete"));
        viewView.initialList(adapter);
        viewView.initialRefresh(getRefreshListener());
    }

    private Observable<List<ViewPoint>> LoadLatest()
    {
        return viewModel.getViews(ZhiHuService.LATEST)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(adapter::addAllAfterClear);
    }

    private PullToRefreshView.OnRefreshListener getRefreshListener()
    {
        return ()-> LoadLatest().subscribe(viewPoints -> {},
                e -> Snackbar.make(viewView.getRefreshView(),"加载失败",Snackbar.LENGTH_SHORT).show(),
                () -> new Handler().postDelayed(()->viewView.getRefreshView().setRefreshing(false),300));
    }

    private class ViewModelImpl implements ViewModel
    {
        @Override
        public Observable<List<ViewPoint>> getViews(String date)
        {
            return APP.getZhiHuService().getViewList(date)
                    .subscribeOn(Schedulers.io())
                    .map(ViewList::getStories)
                    .doOnNext(viewPoints -> {
                        for (ViewPoint viewPoint : viewPoints)
                            if (StringUtil.isBlank(viewPoint.getTitle()) || StringUtil.isBlank(viewPoint.getImages().get(0)))
                                viewPoints.remove(viewPoint);
                    });
        }
    }

    public interface ViewModel
    {
//        RecyclerView.Adapter<?> getAdapter();
        Observable<List<ViewPoint>> getViews(String date);
//        PullToRefreshView.OnRefreshListener getRefreshListener();
    }

    public interface ViewView
    {
        void initialList(RecyclerView.Adapter<?> adapter);

        void initialRefresh(PullToRefreshView.OnRefreshListener refreshListener);

        PullToRefreshView getRefreshView();
    }
}
