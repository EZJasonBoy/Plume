package sausure.io.personallibrary.Utils;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;

/**
 * Created by JOJO on 2015/9/7.
 */
public class ActivityUtil
{
    /**
     * start activity
     * @param activity
     * @param className
     */
    public static  void readyGo(Activity activity,Class<?> className)
    {
        readyGo(activity,className,false);
    }

    /**
     * start activity before finish or not
     * @param activity
     * @param className
     * @param finish
     */
    public static  void readyGo(Activity activity,Class<?> className,boolean finish)
    {
        activity.startActivity(getIntent(activity, className));

        if(finish)
            activity.finish();
    }

    /**
     * start activity whit bundle
     * @param activity
     * @param className
     * @param bundle
     */
    public static void readyGo(Activity activity,Class<?> className,Bundle bundle) {
        readyGo(activity, className, bundle, false);
    }

    /**
     * start activity whit bundle before finish or not
     * @param activity
     * @param className
     * @param bundle
     * @param finish
     */
    public static void readyGo(Activity activity,Class<?> className,Bundle bundle,boolean finish)
    {
        activity.startActivity(getIntent(activity, className).putExtras(bundle));

        if(finish)
            activity.finish();
    }

    /**
     * start activity for result
     * @param activity
     * @param className
     * @param requestCode
     */
    public static void readyGoForResult(Activity activity,Class<?> className,int requestCode)
    {
        activity.startActivityForResult(getIntent(activity, className), requestCode);
    }

    /**
     * start activity for result whit bundle
     * @param activity
     * @param className
     * @param bundle
     * @param requestCode
     */
    public static void readyGoForResult(Activity activity,Class<?> className,Bundle bundle,int requestCode)
    {
        activity.startActivityForResult(getIntent(activity, className).putExtras(bundle), requestCode);
    }

    /**
     * start activity for result
     * @param fragment
     * @param className
     * @param requestCode
     */
    public static void readyGoForResult(Fragment fragment,Class<?> className,int requestCode)
    {
        fragment.startActivityForResult(getIntent(fragment.getActivity(), className), requestCode);
    }

    /**
     * start activity for result whit bundle
     * @param fragment
     * @param className
     * @param bundle
     * @param requestCode
     */
    public static void readyGoForResult(Fragment fragment,Class<?> className,Bundle bundle,int requestCode)
    {
        fragment.startActivityForResult(getIntent(fragment.getActivity(), className).putExtras(bundle), requestCode);
    }

    /**
     * start activity with animation
     * @param activity
     * @param className
     * @param option
     */
    public static void readyGoWithAnimation(Activity activity, Class<?> className, @NonNull ActivityOptionsCompat option)
    {
        ActivityCompat.startActivity(activity, getIntent(activity, className), option.toBundle());
    }

    /**
     * start activity with animation
     * @param activity
     * @param className
     * @param bundle
     * @param option
     */
    public static void readyGoWithAnimation(Activity activity, Class<?> className, Bundle bundle, @NonNull ActivityOptionsCompat option)
    {
        ActivityCompat.startActivity(activity, getIntent(activity, className).putExtras(bundle), option.toBundle());
    }

    /**
     * start activity with animation
     * @param activity
     * @param className
     * @param option
     * @param requestCode
     */
    public static void readyGoWithAnimatorForResult(Activity activity,Class<?> className,@NonNull ActivityOptionsCompat option,int requestCode)
    {
        ActivityCompat.startActivityForResult(activity, getIntent(activity, className), requestCode, option.toBundle());
    }

    /**
     * start activity with animation
     * @param activity
     * @param className
     * @param bundle
     * @param option
     * @param requestCode
     */
    public static void readyGoWithAnimatorForResult(Activity activity,Class<?> className,Bundle bundle,@NonNull ActivityOptionsCompat option,int requestCode)
    {
        ActivityCompat.startActivityForResult(activity, getIntent(activity, className).putExtras(bundle), requestCode,option.toBundle());
    }

    /**
     * generate target intent
     * @param activity
     * @param clazz
     * @return
     */
    private static Intent getIntent(Activity activity,Class<?> clazz)
    {
        return new Intent(activity,clazz);
    }
}
