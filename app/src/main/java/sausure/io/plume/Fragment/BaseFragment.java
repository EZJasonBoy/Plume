package sausure.io.plume.Fragment;

import sausure.io.personallibrary.Fragment.LazyFragment;
import sausure.io.plume.R;

/**
 * Created by JOJO on 2015/9/10.
 */
public class BaseFragment extends LazyFragment {
    @Override
    protected void onFirstUserVisible() {

    }

    @Override
    protected int getLayoutResId() {
        return R.layout.test;
    }
}
