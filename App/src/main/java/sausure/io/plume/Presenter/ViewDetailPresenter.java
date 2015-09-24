package sausure.io.plume.Presenter;

import android.content.Context;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import sausure.io.personallibrary.Utils.LogUtil;
import sausure.io.plume.APP;
import sausure.io.plume.Retrofit.Entity.ViewPoint;
import sausure.io.plume.Retrofit.Entity.ViewPointDetail;

/**
 * Created by JOJO on 2015/9/20.
 */
public class ViewDetailPresenter implements Presenter
{
    private ViewDetailModel viewDetailModel;
    private ViewDetailView viewDetailView;

    public ViewDetailPresenter(Context context,ViewDetailView viewDetailView)
    {
        this.viewDetailView = viewDetailView;
        viewDetailModel = new ViewDetailModelImpl();
    }

    @Override
    public void initialized()
    {
        viewDetailModel.getViewPointDetail(viewDetailView.getViewPoint().getId())
                .doOnNext(viewPointDetail -> viewDetailView.refreshImage(viewPointDetail.getImage()))
                .map(this::generateHtml)
                .map(html -> html.replace("<div class=\"img-place-holder\">", ""))
                .subscribe(viewDetailView::initializeWebView,
                        e -> {
                            LogUtil.e("--onError：" + e.getMessage());
                        e.printStackTrace();
                        });
    }

    private String generateHtml(ViewPointDetail detail)
    {
        StringBuilder builder = new StringBuilder();
        String Link = "<link rel=\"stylesheet\" href=\"%s\" type=\"text/css\">";

        builder.append("<html><head>");

        for(String i : detail.getCss())
        {
            builder.append(String.format(Link, i));
            builder.append("\n");
        }

        builder.append("</head><body>");
        builder.append(detail.getBody());
        builder.append("\n");
        builder.append("</body></html>");

        return builder.toString();
    }

    private class ViewDetailModelImpl implements ViewDetailModel
    {
        @Override
        public Observable<ViewPointDetail> getViewPointDetail(int id) {
            return APP.toggleRetrofitCall(APP.getZhiHuService().getViewPointDetail(id))
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread());
        }
    }

    public interface ViewDetailModel
    {
        Observable<ViewPointDetail> getViewPointDetail(int id);
    }

    public interface ViewDetailView
    {
        ViewPoint getViewPoint();
        void refreshImage(String imageUrl);
        void initializeWebView(String html);
    }
}
