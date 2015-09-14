package sausure.io.plume.Presenter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import java.io.File;

import retrofit.RestAdapter;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import sausure.io.personallibrary.Utils.FileUtil;
import sausure.io.personallibrary.Utils.LogUtil;
import sausure.io.personallibrary.Utils.PreferencesUtil;
import sausure.io.plume.APP;
import sausure.io.plume.R;
import sausure.io.plume.Retrofit.DownloadService;
import sausure.io.plume.Retrofit.Entity.StartImage;
import sausure.io.plume.Retrofit.ZhiHuService;

/**
 * Created by JOJO on 2015/9/6.
 */
public class SplashPresenter implements Presenter
{
    private SplashView splashView;
    private SplashModel splashModel;

    public SplashPresenter(Context context,SplashView splashView)
    {
        this.splashView = splashView;
        this.splashModel = new SplashModelImpl(context);
    }

    @Override
    public void initialized()
    {
        splashView.initializeView(null,splashModel.getBackgroundImage());

        Animation animation = splashModel.getBackgroundImageAnimation();
        animation.setAnimationListener(new Animation.AnimationListener()
        {
            @Override
            public void onAnimationStart(Animation animation)
            {
                splashModel.UpdateBackgroundImage();
            }

            @Override
            public void onAnimationEnd(Animation animation)
            {
                splashView.navigateToMainActivity();
            }

            @Override
            public void onAnimationRepeat(Animation animation)
            {}
        });

        splashView.animateBackgroundImage(animation);
    }

    private class SplashModelImpl implements SplashModel
    {
        private static final String IMG_NAME = "START_IMAGE";

        File targetFile;
        Context context;

        public  SplashModelImpl(Context context)
        {
            this.context = context;
            this.targetFile = new File(context.getFilesDir(),IMG_NAME);
        }

        @Override
        public Observable<Bitmap> getBackgroundImage()
        {
            return Observable.just(targetFile)
                    .filter(File::exists)
                    .observeOn(AndroidSchedulers.mainThread())
                    .map(file -> BitmapFactory.decodeFile(file.getAbsolutePath()));
        }

        @Override
        public void UpdateBackgroundImage()
        {
            APP.getZhiHuService()
                .getStartImage(ZhiHuService.START_IMAGE)
                .subscribeOn(Schedulers.io())
                .map(StartImage::getImg)
                .doOnNext(startImg -> LogUtil.i("there is latest start image "+ startImg))
                .filter(startImg -> {
                    String old = PreferencesUtil.getString(context, IMG_NAME,"");

                    if (old.equals(startImg)) {
                        LogUtil.i("do not need to update start image");
                        return false;
                    }
                    else {
                        LogUtil.i("prepare to update start image");
                        PreferencesUtil.putString(context, IMG_NAME, startImg);
                        return true;
                    }
                })
                .flatMap(img ->
                        new RestAdapter
                                .Builder()
                                .setEndpoint(img.substring(0, img.lastIndexOf("/")))
                                .build()
                                .create(DownloadService.class)
                                .downloadStartImage(img.substring(img.lastIndexOf("/") + 1)))
                .subscribeOn(Schedulers.io())
                .doOnNext(response -> LogUtil.i("start image's url is " + response.getUrl()))
                .subscribe(response -> FileUtil.saveFile(targetFile, response));
        }

        @Override
        public Animation getBackgroundImageAnimation()
        {
            return AnimationUtils.loadAnimation(context, R.anim.splash);
        }
    }

    /**
     * interface which SplashActivity should implement
     */
    public interface SplashView
    {
        void initializeView(String copyright,Observable<Bitmap> bitmap);

        void animateBackgroundImage(Animation animation);

        void navigateToMainActivity();
    }

    /**
     * get data from Model
     */
    public interface SplashModel
    {
        Observable<Bitmap> getBackgroundImage();

        void UpdateBackgroundImage();

        Animation getBackgroundImageAnimation();
    }
}
