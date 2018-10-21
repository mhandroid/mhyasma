package com.android.mh.yasma.ui;

import android.os.Bundle;

import com.android.mh.yasma.R;
import com.android.mh.yasma.ui.fragment.PostFragment;

/**
 * Created by @author Mubarak Hussain.
 */
public class HomeActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        PostFragment posFragment = (PostFragment) mFragmentManager.findFragmentByTag("Post_fragment");
        if (posFragment == null) {
            addFragment(PostFragment.newInstance(), false, "Post_fragment");
        } else {
            mFragmentManager.beginTransaction().show(posFragment).commit();
        }

        setHomeButtonEnabled(true);

        showProgressDialog(getString(R.string.app_name), getString(R.string.loding));
    }

    public void hideProgress() {
        hideProgressDialog();
    }

    public void showProgress(String title, String msg) {
        showProgressDialog(title, msg);
    }
}
