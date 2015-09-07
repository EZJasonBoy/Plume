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
import sausure.io.plume.R;
import sausure.io.plume.Retrofit.RequestService;
import sausure.io.personallibrary.Utils.FileUtil;

/**
 * Created by JOJO on 2015/9/6.
 */
public class SplashPresenter
{
    private SplashView splashView;
    private SplashModel splashModel;

    public SplashPresenter(Context context,SplashView splashView)
    {
        this.splashView = splashView;
        this.splashModel = new SplashModelImpl(context);
    }

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
                splashView.navigateToHomeActivity();
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
                    .filter(file -> file.exists())
                    .observeOn(AndroidSchedulers.mainThread())
                    .map(file -> BitmapFactory.decodeFile(file.getAbsolutePath()));
        }

        @Override
        public void UpdateBackgroundImage()
        {
            new RestAdapter
                .Builder()
                .setEndpoint(RequestService.ZHIHU_API)
                .build()
                .create(RequestService.class)
                .getStartImage(RequestService.START_IMAGE)
                .subscribeOn(Schedulers.io())
                .map(startImage -> startImage.getImg())
                .flatMap(img ->
                        new RestAdapter
                                .Builder()
                                .setEndpoint(img.substring(0, img.lastIndexOf("/")))
                                .build()
                                .create(RequestService.class)
                                .downloadStartImage(img.substring(img.lastIndexOf("/") + 1)))
                .subscribeOn(Schedulers.io())
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
    public static interface SplashView
    {
        void initializeView(String copyright,Observable<Bitmap> bitmap);

        void animateBackgroundImage(Animation animation);

        void navigateToHomeActivity();
    }

    /**
     * get data from Model
     */
    private static interface SplashModel
    {
        Observable<Bitmap> getBackgroundImage();

        void UpdateBackgroundImage();

        Animation getBackgroundImageAnimation();
    }
}
