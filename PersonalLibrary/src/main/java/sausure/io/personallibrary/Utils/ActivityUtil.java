package sausure.io.personallibrary.Utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

/**
 * Created by JOJO on 2015/9/7.
 */
public class ActivityUtil
{
    /**
     * start activity
     * @param context
     * @param className
     */
    public static  void readyGo(Context context,Class<?> className)
    {
        readyGo(context,className,false);
    }

    /**
     * start activity before finish or not
     * @param context
     * @param className
     * @param finish
     */
    public static  void readyGo(Context context,Class<?> className,boolean finish)
    {
        context.startActivity(getIntent(context, className));

        if(finish && context instanceof Activity)
            ((Activity)context).finish();
    }

    /**
     * start activity whit bundle
     * @param context
     * @param className
     * @param bundle
     */
    public static void readyGo(Context context,Class<?> className,Bundle bundle)
    {
        readyGo(context,className,bundle,false);
    }

    /**
     * start activity whit bundle before finish or not
     * @param context
     * @param className
     * @param bundle
     * @param finish
     */
    public static void readyGo(Context context,Class<?> className,Bundle bundle,boolean finish)
    {
        context.startActivity(getIntent(context,className).putExtras(bundle));

        if(finish && context instanceof Activity)
            ((Activity)context).finish();
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
        activity.startActivityForResult(getIntent(activity,className).putExtras(bundle),requestCode);
    }

    /**
     * start activity for result
     * @param fragment
     * @param className
     * @param requestCode
     */
    public static void readyGoForResult(Fragment fragment,Class<?> className,int requestCode)
    {
        fragment.startActivityForResult(getIntent(fragment.getActivity(),className),requestCode);
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
        fragment.startActivityForResult(getIntent(fragment.getActivity(),className).putExtras(bundle),requestCode);
    }

    /**
     * generate target intent
     * @param context
     * @param clazz
     * @return
     */
    private static Intent getIntent(Context context,Class<?> clazz)
    {
        return new Intent(context,clazz);
    }
}
