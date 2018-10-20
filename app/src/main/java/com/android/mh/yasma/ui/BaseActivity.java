package com.android.mh.yasma.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.android.mh.yasma.R;

/**
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


    protected void initDrawerLayout() {

        mDrawerToggle = new ActionBarDrawerToggle(this, mNavigationDrawerLayout, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawerToggle.syncState();

        mNavigationDrawerLayout.addDrawerListener(mDrawerToggle);
    }


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

    protected void setToolbarTitle(String title) {
        if (mActionBar != null) {
            mActionBar.setTitle(title);
        }
    }

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

    private void openDrawer(int gravity) {
        mNavigationDrawerLayout.openDrawer(gravity);
    }

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
                Intent intent = new Intent(this, HomeActivity.class);
                startActivity(intent);
                break;

            case R.id.nav_Albums:
                Intent intentAlbum = new Intent(this, AlbumsActivity.class);
                startActivity(intentAlbum);
                break;
        }

        closeDrawer();
        return false;
    }

    protected void showProgressDialog(String title, String message) {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setCancelable(false);
        }

        mProgressDialog.setTitle(title);
        mProgressDialog.setMessage(message);
        mProgressDialog.show();
    }

    protected void hideProgressDialog() {
        if (mProgressDialog != null) {
            mProgressDialog.hide();
            mProgressDialog.dismiss();
        }
    }

}
