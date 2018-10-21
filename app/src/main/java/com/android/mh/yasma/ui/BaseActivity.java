package com.android.mh.yasma.ui;

import android.app.ProgressDialog;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.android.mh.yasma.R;
import com.android.mh.yasma.ui.fragment.AlbumsFragment;
import com.android.mh.yasma.ui.fragment.PostFragment;
import com.android.mh.yasma.utils.Utils;

/**
 * Base activity foa handle common functionality
 * Created by @author Mubarak Hussain.
 */
public abstract class BaseActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private final String TAG = BaseActivity.class.getSimpleName();
    protected Resources mResources;
    protected FragmentManager mFragmentManager;
    protected Toolbar mToolbar;
    protected ActionBar mActionBar;
    private DrawerLayout mNavigationDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.navigation_drawer_layout);

        mResources = getResources();
        mFragmentManager = getSupportFragmentManager();

        setupActionBar();

    }


    /**
     * Method to initialize drawer layout
     */
    protected void initDrawerLayout() {

        mDrawerToggle = new ActionBarDrawerToggle(this, mNavigationDrawerLayout, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawerToggle.syncState();

        mNavigationDrawerLayout.addDrawerListener(mDrawerToggle);
    }


    /**
     * Method to enable disable home button
     * @param isEnabled
     */
    protected void setHomeButtonEnabled(boolean isEnabled) {
        mNavigationDrawerLayout = findViewById(R.id.drawer_layout);
        if (mActionBar != null) {
            if (isEnabled) {
                mActionBar.setHomeButtonEnabled(true);
                initDrawerLayout();

                NavigationView navigationView = findViewById(R.id.nav_view);
                navigationView.setNavigationItemSelectedListener(this);
            } else {
                mActionBar.setDisplayHomeAsUpEnabled(true);
                mNavigationDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
            }
        }
    }

    /**
     * Method to setup action bar
     */
    protected void setupActionBar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        if (mToolbar != null) {
            setSupportActionBar(mToolbar);
            mActionBar = getSupportActionBar();
            if (mActionBar != null) {
                mActionBar.setTitle(mResources.getString(R.string.app_name));
            }
        }
    }

    /**
     * Method to set toolbar title
     * @param title
     */
    protected void setToolbarTitle(String title) {
        if (mActionBar != null) {
            mActionBar.setTitle(title);
        }
    }

    /**
     * Method to add layout with extended activity
     * @param layoutId
     */
    protected void addLayout(int layoutId) {
        try {
            FrameLayout mainContainer = (FrameLayout) findViewById(R.id.main_container_layout);
            if (mainContainer != null) {
                mainContainer.addView(getLayoutInflater().inflate(layoutId, null));
            }
        } catch (Exception exception) {
            Log.e(TAG, exception.getMessage(), exception);
        }
    }

    /**
     * Method to close drawer layout
     */
    public void closeDrawer() {
        if (mNavigationDrawerLayout != null) {
            mNavigationDrawerLayout.closeDrawer(GravityCompat.START);
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        if (mDrawerToggle != null)
            mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (mDrawerToggle != null)
            mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {

        if (mNavigationDrawerLayout != null && mNavigationDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            closeDrawer();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int id = menuItem.getItemId();
        switch (id) {
            case R.id.nav_Post:
                if (Utils.isNetworkAvailable(this)) {
                    showProgressDialog(getString(R.string.app_name), getString(R.string.loding));
                    replaceFragment(PostFragment.newInstance(), false, "Post_fragment", true);
                } else {
                    Toast.makeText(this, getString(R.string.no_iternet), Toast.LENGTH_SHORT).show();
                    hideProgressDialog();
                }
                break;

            case R.id.nav_Albums:
                if (Utils.isNetworkAvailable(this)) {
                    showProgressDialog(getString(R.string.album), getString(R.string.loding));
                    replaceFragment(AlbumsFragment.newInstance(), false, "albums_fragment", true);
                } else {
                    Toast.makeText(this, getString(R.string.no_iternet), Toast.LENGTH_SHORT).show();
                    hideProgressDialog();
                }
                break;
        }

        closeDrawer();
        return false;
    }

    /**
     * Method to show progress bar
     * @param title
     * @param message
     */
    protected void showProgressDialog(String title, String message) {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setCancelable(false);
        }

        mProgressDialog.setTitle(title);
        mProgressDialog.setMessage(message);
        mProgressDialog.show();
    }

    /**
     * Method hide progress bar
     */
    protected void hideProgressDialog() {
        if (mProgressDialog != null) {
            mProgressDialog.hide();
        }
    }

    /**
     * Mehtod to add fragment
     * @param fragment
     * @param isAddToBackStack
     * @param fragmentTag
     */
    protected void addFragment(Fragment fragment, boolean isAddToBackStack, String fragmentTag) {
        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
        if (TextUtils.isEmpty(fragmentTag)) {
            fragmentTransaction.add(R.id.main_container_layout, fragment);
        } else {
            fragmentTransaction.add(R.id.main_container_layout, fragment, fragmentTag);
        }
        if (isAddToBackStack) {
            fragmentTransaction.addToBackStack(fragmentTag);
        }

        fragmentTransaction.commitAllowingStateLoss();
    }

    /**
     * Method to replace fragment
     * @param fragment
     * @param isAddToBackStack
     * @param fragmentTag
     * @param showAnim
     */
    public void replaceFragment(Fragment fragment, boolean isAddToBackStack, String fragmentTag, boolean showAnim) {
        final FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
        if (TextUtils.isEmpty(fragmentTag)) {
            fragmentTransaction.replace(R.id.main_container_layout, fragment);
        } else {
            fragmentTransaction.replace(R.id.main_container_layout, fragment, fragmentTag);
        }
        if (isAddToBackStack) {
            fragmentTransaction.addToBackStack(fragmentTag);
        }

        fragmentTransaction.commitAllowingStateLoss();
    }

}
