package sausure.io.plume.Fragment;

import sausure.io.personallibrary.Fragment.LazyFragment;
import sausure.io.plume.Presenter.Presenter;

/**
 * Created by JOJO on 2015/9/10.
 */
public abstract class BaseFragment extends LazyFragment
{
    protected Presenter presenter;

    @Override
    protected void onFirstUserVisible()
    {
        if(presenter == null)
            presenter = getPresenter();

        if(presenter == null)
            throw new IllegalArgumentException("you must provide a Presenter implement");
        else
            presenter.initialized();
    }

    protected abstract Presenter getPresenter();
}
