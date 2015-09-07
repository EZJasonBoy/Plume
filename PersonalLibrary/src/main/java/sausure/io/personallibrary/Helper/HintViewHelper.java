package sausure.io.personallibrary.Helper;

import android.view.View;

/**
 * Created by JOJO on 2015/9/4.
 */
public interface HintViewHelper
{
    void cancelHint();

//    void showNetworkError(View.OnClickListener listener);

    void showError(View.OnClickListener listener);

//    void showEmpty(View.OnClickListener listener);

    void showLoading();

//    void showNetworkError(String msg,View.OnClickListener listener);

    void showError(String msg,View.OnClickListener listener);

//    void showEmpty(String msg,View.OnClickListener listener);

    void showLoading(String msg);
}
