package com.framgia.moviedb_31.screen.listMovie;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import com.framgia.moviedb_31.R;
import com.framgia.moviedb_31.databinding.ActivityListMovieBinding;
import com.framgia.moviedb_31.utils.Constant;

public class ListMovieActivity extends AppCompatActivity {
    private static final String EXTRA_KEY = "EXTRA_KEY";
    private static final String EXTRA_VALUE = "EXTRA_VALUE";
    private int mTotalItemCount;
    private int mLastVisibleItem;
    private int mVisible = 15;
    private boolean mIsLoading;
    private ListMovieViewModel mViewModel;

    public static Intent getListMovieIntent(Context context, String key, String value) {
        Intent intent = new Intent(context, ListMovieActivity.class);
        intent.putExtra(EXTRA_KEY, key);
        intent.putExtra(EXTRA_VALUE, value);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    private void initView() {
        ActivityListMovieBinding binding =
                DataBindingUtil.setContentView(this, R.layout.activity_list_movie);
        mViewModel = new ListMovieViewModel(this, getIntent().getStringExtra(EXTRA_KEY),
                getIntent().getStringExtra(EXTRA_VALUE));
        binding.setListMovieViewModel(mViewModel);
        addScrollListener(binding.recyclerListMovie);
    }

    private void addScrollListener(RecyclerView recyclerView) {
        final LinearLayoutManager linearLayoutManager =
                (LinearLayoutManager) recyclerView.getLayoutManager();
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            int page = Constant.PAGE_DEFAULT;

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                mTotalItemCount = recyclerView.getChildCount();
                mLastVisibleItem = linearLayoutManager.getItemCount();
                mVisible = linearLayoutManager.findFirstVisibleItemPosition();
                if (!mIsLoading && (mTotalItemCount + mVisible == mLastVisibleItem)) {
                    page++;
                    mIsLoading = false;
                    mViewModel.loadMoreData(page);
                }
            }
        });
    }
}
