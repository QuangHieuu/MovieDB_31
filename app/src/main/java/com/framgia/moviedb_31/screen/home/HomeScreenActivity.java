package com.framgia.moviedb_31.screen.home;

import android.app.SearchManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.databinding.DataBindingUtil;
import android.databinding.Observable;
import android.databinding.ObservableField;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import com.framgia.moviedb_31.R;
import com.framgia.moviedb_31.databinding.ActivityHomeScreenBinding;
import com.framgia.moviedb_31.screen.listMovie.ListMovieActivity;
import com.framgia.moviedb_31.screen.listgenres.ListGenresActivity;
import com.framgia.moviedb_31.screen.movieDetail.MovieDetailActivity;
import com.framgia.moviedb_31.utils.Constant;
import com.framgia.moviedb_31.utils.ItemClickListener;

public class HomeScreenActivity extends AppCompatActivity
        implements View.OnClickListener, ItemClickListener,
        NavigationView.OnNavigationItemSelectedListener, DrawerLayout.DrawerListener {
    private static final String CHECK_INTERNET_CONNECTION = "CHECK INTERNET";
    public static ObservableField<Boolean> mIsConnected;
    private ActivityHomeScreenBinding mBinding;
    private ActionBar mActionBar;
    private BroadcastReceiver mNetworkStatusReceiver;
    private MainViewModel mMainViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mIsConnected = new ObservableField<>();
        mNetworkStatusReceiver = networkStatusReceiver();
        registerReceiver(mNetworkStatusReceiver,
                new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        mIsConnected.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                if (mIsConnected.get()) {
                    initView();
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mNetworkStatusReceiver);
        mMainViewModel.onStop();
    }

    private void initView() {
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_home_screen);
        mMainViewModel = new MainViewModel(this);
        mBinding.setMainviewmodel(mMainViewModel);
        mMainViewModel.initData();
        mMainViewModel.getImagePoster();
        mActionBar = getSupportActionBar();
        if (mActionBar != null) {
            mActionBar.setDisplayHomeAsUpEnabled(true);
            mActionBar.setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);
        }
        SlideAdapter slideAdapter =
                new SlideAdapter(this, mBinding.getMainviewmodel().getImagePoster());
        mBinding.viewSlide.setAdapter(slideAdapter);
        mBinding.navView.setNavigationItemSelectedListener(this);
        mBinding.drawerLayout.addDrawerListener(this);

        mBinding.textShowMoreTopRate.setOnClickListener(this);
        mBinding.textShowMorePopular.setOnClickListener(this);
        mBinding.textShowMoreNowPlaying.setOnClickListener(this);
        mBinding.textShowMoreUpComing.setOnClickListener(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home:
                if (mBinding.drawerLayout.isDrawerOpen(Gravity.START)) {
                    mBinding.drawerLayout.closeDrawer(Gravity.START);
                } else {
                    mBinding.drawerLayout.openDrawer(Gravity.START);
                }
        }
        return (super.onOptionsItemSelected(menuItem));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search_view, menu);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.menu_search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                startActivity(ListMovieActivity.getListMovieIntent(HomeScreenActivity.this,
                        Constant.TYPE_SEARCH, s));
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.text_show_more_top_rate:
                startActivity(ListMovieActivity.getListMovieIntent(this, Constant.TYPE_CATEGORY,
                        Constant.TOP_RATED));
                break;
            case R.id.text_show_more_popular:
                startActivity(ListMovieActivity.getListMovieIntent(this, Constant.TYPE_CATEGORY,
                        Constant.POPULAR));
                break;
            case R.id.text_show_more_now_playing:
                startActivity(ListMovieActivity.getListMovieIntent(this, Constant.TYPE_CATEGORY,
                        Constant.NOW_PLAYING));
                break;
            case R.id.text_show_more_up_coming:
                startActivity(ListMovieActivity.getListMovieIntent(this, Constant.TYPE_CATEGORY,
                        Constant.UP_COMING));
                break;
        }
    }

    @Override
    public void onItemClicked(String id) {
        startActivity(MovieDetailActivity.newIntent(this, id));
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.nav_genres:
                startActivity(ListGenresActivity.getListGenresIntent(this));
                break;
        }
        return true;
    }

    @Override
    public void onDrawerSlide(@NonNull View view, float v) {

    }

    @Override
    public void onDrawerOpened(@NonNull View view) {
        mActionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back_black_24dp);
    }

    @Override
    public void onDrawerClosed(@NonNull View view) {
        mActionBar.setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);
    }

    @Override
    public void onDrawerStateChanged(int i) {

    }

    private static BroadcastReceiver networkStatusReceiver() {
        return new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {
                ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(
                        Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
                mIsConnected.set(networkInfo != null && networkInfo.isConnected());
                if (!mIsConnected.get()) {
                    Toast.makeText(context, CHECK_INTERNET_CONNECTION, Toast.LENGTH_SHORT).show();
                }
            }
        };
    }
}
