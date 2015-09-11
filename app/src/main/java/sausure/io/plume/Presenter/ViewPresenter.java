package sausure.io.plume.Presenter;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.yalantis.phoenix.PullToRefreshView;

import java.text.ParseException;
import java.util.List;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import sausure.io.personallibrary.Utils.DateUtil;
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
    private LinearLayoutManager layoutManager;
    private int offset = 0;
    private boolean isLoading = false;

    public ViewPresenter(Context context,ViewView viewView)
    {
        this.viewView = viewView;
        viewModel = new ViewModelImpl();
        layoutManager = new LinearLayoutManager(context);
    }

    public void initialized()
    {
        if(adapter == null)
            adapter = new ViewListAdapter();

        if(!isLoading)
            LoadLatest().subscribe(
                    viewPoints -> Log.i("test", "onNext"),
                    e -> Log.i("test", "onError"),
                    () -> Log.i("test", "onComplete"));

        viewView.initialList(layoutManager,adapter,getOnScrollListener());
        viewView.initialRefresh(getRefreshListener());
    }

    private RecyclerView.OnScrollListener getOnScrollListener()
    {
        return new RecyclerView.OnScrollListener()
        {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy)
            {
                super.onScrolled(recyclerView, dx, dy);
                int lastVisibleItem = layoutManager.findLastVisibleItemPosition();
                int totalItemCount = layoutManager.getItemCount();
                if(!isLoading && lastVisibleItem >= totalItemCount - 4 && dy > 0)
                {
                    Observable<List<ViewPoint>> observable = LoadBefore();

                    if(observable != null)
                        observable.subscribe(
                                viewPoints -> Log.i("test", "onNext"),
                                e -> Log.i("test", "onError"),
                                () -> Log.i("test", "onComplete"));
                }
            }
        };
    }

    private Observable<List<ViewPoint>> LoadLatest()
    {
        isLoading = true;
        offset = 0;

        return viewModel.getLatestViews()
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(adapter::addAllAfterClear);
    }

    private Observable<List<ViewPoint>> LoadBefore()
    {
        isLoading = true;

        String before = getBeforeDate(--offset);

        if(Integer.valueOf(before) < ZhiHuService.startDay)
        {
            Snackbar.make(viewView.getRefreshView(), "以上是全部内容", Snackbar.LENGTH_SHORT).show();
            return null;
        }

        return viewModel.getBeforeViews(before)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(adapter::addAll);
    }

    private String getBeforeDate(int offset)
    {
        String date = null;

        try
        {
            date = DateUtil.getAddDay(DateUtil.DEFAULT_DATE, offset, DateUtil.DEFAULT_DATE_FORMAT);
        }
        catch (ParseException e)
        {
            e.printStackTrace();
        }

        return date != null ? date.replace("-","") : null;
    }

    private PullToRefreshView.OnRefreshListener getRefreshListener()
    {
        return ()-> {
            if(!isLoading)
                LoadLatest().subscribe(
                        viewPoints -> {},
                        e -> {
                            Snackbar.make(viewView.getRefreshView(),"加载失败"+e.getMessage(),Snackbar.LENGTH_SHORT).show();
                            viewView.getRefreshView().setRefreshing(false);
                        },
                        () -> viewView.getRefreshView().setRefreshing(false));
            else
                viewView.getRefreshView().setRefreshing(false);
        };
    }

    private class ViewModelImpl implements ViewModel
    {
        @Override
        public Observable<List<ViewPoint>> getLatestViews()
        {
            return toggleObservable(APP.getZhiHuService().getLatestViews());
        }

        @Override
        public Observable<List<ViewPoint>> getBeforeViews(String date)
        {
            return toggleObservable(APP.getZhiHuService().getBeforeViews(date));
        }

        private Observable<List<ViewPoint>> toggleObservable(Observable<ViewList> observable)
        {
            return observable
                    .subscribeOn(Schedulers.io())
                    .doOnNext(viewList -> isLoading = false)
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
        Observable<List<ViewPoint>> getLatestViews();

        Observable<List<ViewPoint>> getBeforeViews(String date);
    }

    public interface ViewView
    {
        void initialList(RecyclerView.LayoutManager layoutManager,RecyclerView.Adapter<?> adapter,RecyclerView.OnScrollListener onScrollListener);

        void initialRefresh(PullToRefreshView.OnRefreshListener refreshListener);

        PullToRefreshView getRefreshView();
    }
}
