package sausure.io.personallibrary.Helper.impl;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import rx.Observable;
import sausure.io.personallibrary.Helper.HintViewHelper;
import sausure.io.personallibrary.R;

/**
 * Created by JOJO on 2015/9/4.
 */
public class HintViewHelperImpl implements HintViewHelper
{
    private Context context;
    private View targetView;
    private View currentView;
    private ViewGroup parentView;
    private ViewGroup.LayoutParams params;
    private int viewIndex;
    private HintViewListener hintViewListener;

    public HintViewHelperImpl(Context context,HintViewListener hintViewListener)
    {
        this.context = context;
        this.hintViewListener = hintViewListener;

        if(this.hintViewListener.needHintView())
        {
            if (this.hintViewListener.getHintTargetView() == null)
                throw new IllegalArgumentException("you must tell me which view need to hint !");
        }
        else
            throw new IllegalArgumentException("if you do not need hint view why you instantiate this object ?");

        currentView = targetView = this.hintViewListener.getHintTargetView();
        params = targetView.getLayoutParams();

        if(targetView.getParent() != null)
            parentView = (ViewGroup) targetView.getParent();
        else
            parentView = (ViewGroup) targetView.getRootView().findViewById(android.R.id.content);

        for(int i = 0;i < parentView.getChildCount();++i)
            if(targetView == parentView.getChildAt(i))
            {
                viewIndex = i;
                break;
            }
    }

    @Override
    public void showError(View.OnClickListener listener)
    {
        showError(null, listener);
    }

    @Override
    public void showLoading()
    {
        showLoading(null);
    }

    @Override
    public void cancelHint()
    {
        toggleHintView(targetView);
    }

    @Override
    public void showError(String msg, View.OnClickListener listener)
    {
        View view = View.inflate(context, R.layout.hint_error,null);

        if(msg != null)
            ((TextView)view.findViewById(R.id.hint_error)).setText(msg);

        if(listener != null)
            view.setOnClickListener(listener);

        toggleHintView(view);
    }


    @Override
    public void showLoading(String msg)
    {

    }

    private void toggleHintView(View newView)
    {
        if(parentView != null && parentView.getChildAt(viewIndex) != newView)
        {
            Observable.just((ViewGroup)newView.getParent())
                    .filter(viewGroup -> viewGroup != null)
                    .subscribe(viewGroup -> viewGroup.removeView(newView));

            parentView.removeView(currentView);
            parentView.addView(newView,viewIndex,params);
        }
    }

    public interface HintViewListener
    {
        View getHintTargetView();

        boolean needHintView();
    }
}
