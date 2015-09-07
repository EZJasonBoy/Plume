package sausure.io.personallibrary.Helper.impl;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;

import sausure.io.personallibrary.Helper.StartComponentHelper;

/**
 * Created by JOJO on 2015/9/2.
 */
public class StartComponentHelperImpl<T> implements StartComponentHelper
{
    private T receiver;

    @SuppressWarnings("unchecked")
    public StartComponentHelperImpl(T receiver)
    {
        if(receiver instanceof Activity || receiver instanceof Fragment)
            this.receiver = receiver;
        else
            throw new IllegalArgumentException("the receiver should instanceof Activity or Fragment");
    }

    @Override
    public void readyGo(Class<?> clazz)
    {
        readyGo(clazz,false);
    }

    @Override
    public void readyGo(Class<?> clazz, boolean finish)
    {
        if(instanceofActivity())
        {
            ((Activity) receiver).startActivity(getIntentByActivity(clazz));
            if(finish)
                ((Activity) receiver).finish();
        }
        else if(instanceofFragment())
            ((Fragment)receiver).startActivity(getIntentByFragment(clazz));
    }

    @Override
    public void readyGo(Class<?> clazz, Bundle bundle)
    {
        readyGo(clazz,bundle,false);
    }

    @Override
    public void readyGo(Class<?> clazz, Bundle bundle, boolean finish)
    {
        if(instanceofActivity())
        {
            ((Activity) receiver).startActivity(getIntentByActivity(clazz, bundle));
            if(finish)
                ((Activity) receiver).finish();
        }
        else if(instanceofFragment())
            ((Fragment)receiver).startActivity(getIntentByFragment(clazz, bundle));
    }

    @Override
    public void readyGoForResult(Class<?> clazz, int requestCode)
    {
        if(instanceofActivity())
            ((Activity)receiver).startActivityForResult(getIntentByActivity(clazz),requestCode);
        else if(instanceofFragment())
            ((Fragment)receiver).startActivityForResult(getIntentByFragment(clazz),requestCode);
    }

    @Override
    public void readyGoForResult(Class<?> clazz, int requestCode, Bundle bundle)
    {
        if(instanceofActivity())
            ((Activity)receiver).startActivityForResult(getIntentByActivity(clazz,bundle),requestCode);
        else if(instanceofFragment())
            ((Fragment)receiver).startActivityForResult(getIntentByFragment(clazz,bundle),requestCode);
    }

    private Intent getIntentByActivity(Class<?> clazz,Bundle bundle)
    {
        return new Intent(((Activity)receiver),clazz).putExtras(bundle);
    }

    private Intent getIntentByFragment(Class<?> clazz,Bundle bundle)
    {
        return new Intent(((Fragment)receiver).getActivity(),clazz).putExtras(bundle);
    }

    private Intent getIntentByActivity(Class<?> clazz)
    {
        return new Intent((Activity)receiver,clazz);
    }

    private Intent getIntentByFragment(Class<?> clazz)
    {
        return new Intent(((Fragment)receiver).getActivity(),clazz);
    }

    private boolean instanceofActivity()
    {
        return receiver instanceof Activity;
    }

    private boolean instanceofFragment()
    {
        return receiver instanceof Fragment;
    }
}
