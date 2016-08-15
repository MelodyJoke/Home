package com.teamsolo.home.structure.page;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.melody.base.template.activity.HandlerActivity;
import com.teamsolo.home.R;
import com.teamsolo.home.structure.widget.LoadingView;

import org.jetbrains.annotations.NotNull;

/**
 * description: main page
 * author: Melody
 * date: 2016/8/13
 * version: 0.0.0.1
 */
public class MainActivity extends HandlerActivity implements
        NavigationView.OnNavigationItemSelectedListener {

    private LoadingView mLoadingView;

    private FloatingActionButton mFab;

    private DrawerLayout mDrawerLayout;

    private NavigationView mNavigationView;

    private BottomNavigationBar mBottomNavigationBar;

    private TabLayout mTabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getBundle(getIntent());
        initViews();
        bindListeners();

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mBottomNavigationBar.hide();
            }
        }, 1000);

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mLoadingView.dismiss();
            }
        }, 3000);
    }

    @Override
    protected void getBundle(@NotNull Intent intent) {

    }

    @Override
    protected void initViews() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO:
            }
        });
        setSupportActionBar(toolbar);

        mLoadingView = (LoadingView) findViewById(R.id.loading);
        mLoadingView.setReactView(findViewById(R.id.content));

        mFab = (FloatingActionButton) findViewById(R.id.fab);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        mNavigationView = (NavigationView) findViewById(R.id.nav_view);
        mBottomNavigationBar = (BottomNavigationBar) findViewById(R.id.bottom);
        mTabLayout = (TabLayout) findViewById(R.id.tab);

        initBottomNavigationBar();
        initTabLayout();
    }

    private void initBottomNavigationBar() {
        mBottomNavigationBar.initialise();
    }

    private void initTabLayout() {

    }

    @Override
    protected void bindListeners() {
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mBottomNavigationBar.isHidden()) mBottomNavigationBar.show();
                else mBottomNavigationBar.hide();
            }
        });

        mNavigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    protected void handleMessage(HandlerActivity activity, Message msg) {

    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START))
            mDrawerLayout.closeDrawer(GravityCompat.START);
        else super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_camera) {

        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}
