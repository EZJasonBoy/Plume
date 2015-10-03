package sausure.io.plume.Activity;

import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.webkit.WebView;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.io.IOException;

import butterknife.Bind;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import sausure.io.plume.Presenter.Presenter;
import sausure.io.plume.Presenter.ViewDetailPresenter;
import sausure.io.plume.R;
import sausure.io.plume.Retrofit.Entity.ViewListItem;

/**
 * Created by JOJO on 2015/9/17.
 */
public class ViewDetailActivity extends BaseActivity implements ViewDetailPresenter.ViewDetailView
{
    public static String VIEW_DETAIL = "VIEW_DETAIL";

    private ViewListItem viewListItem;

    @Bind(R.id.image)
    protected ImageView imageView;

    @Bind(R.id.collapsing_toolbar)
    protected CollapsingToolbarLayout collapsingToolbarLayout;

    @Bind(R.id.web_view)
    protected WebView webView;

    @Override
    protected void onActivityCreated() {
        super.onActivityCreated();
        collapsingToolbarLayout.setTitle(viewListItem.getTitle());
        Picasso.with(activity).load(viewListItem.getImages().get(0)).into(imageView);
    }

    @Override
    protected Presenter getPresenter()
    {
        return new ViewDetailPresenter(activity,this);
    }

    @Override
    protected int getLayoutResId()
    {
        return R.layout.activity_view_detail;
    }

    @Override
    protected void handleBundleExtras(Bundle extras)
    {
        viewListItem = (ViewListItem) extras.getSerializable(VIEW_DETAIL);
    }

    @Override
    protected boolean canNaviBack() {
        return true;
    }

    @Override
    public ViewListItem getViewListItem() {
        return viewListItem;
    }

    @Override
    public void refreshImage(String imageUrl)
    {
        Observable.just(imageUrl)
                .map(url -> {
                    try {
                        return Picasso.with(activity).load(url).get();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return null;
                })
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .filter(bitmap1 -> bitmap1 != null)
                .subscribe(imageView::setImageBitmap);
    }

    @Override
    public void initializeWebView(String html) {
        webView.loadDataWithBaseURL("x-data://base", html, "text/html", "UTF-8", null);
    }
}
