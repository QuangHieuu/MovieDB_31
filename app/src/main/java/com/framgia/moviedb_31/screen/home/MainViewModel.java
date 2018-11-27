package com.framgia.moviedb_31.screen.home;

import com.framgia.moviedb_31.data.model.BaseModel;
import com.framgia.moviedb_31.data.repository.MovieRepository;
import com.framgia.moviedb_31.data.source.remote.RemoteDataSource;
import com.framgia.moviedb_31.utils.Constant;
import com.framgia.moviedb_31.utils.ItemClickListener;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import java.util.ArrayList;
import java.util.List;

public class MainViewModel {
    private HomeScreenAdapter mAdapterTopRated, mAdapterUpComing, mAdapterPopular,
            mAdapterNowPlaying;
    private CompositeDisposable mCompositeDisposable;
    private MovieRepository mMovieRepository;
    private ItemClickListener mListener;

    MainViewModel(ItemClickListener listener) {
        mListener = listener;
        mAdapterTopRated = new HomeScreenAdapter(mListener);
        mAdapterNowPlaying = new HomeScreenAdapter(mListener);
        mAdapterPopular = new HomeScreenAdapter(mListener);
        mAdapterUpComing = new HomeScreenAdapter(mListener);
        mCompositeDisposable = new CompositeDisposable();
        mMovieRepository = MovieRepository.getInstance(RemoteDataSource.getsInstance());
    }

    public HomeScreenAdapter getAdapterTopRated() {
        return mAdapterTopRated;
    }

    public HomeScreenAdapter getAdapterUpComing() {
        return mAdapterUpComing;
    }

    public HomeScreenAdapter getAdapterPopular() {
        return mAdapterPopular;
    }

    public HomeScreenAdapter getAdapterNowPlaying() {
        return mAdapterNowPlaying;
    }

    public void initData() {
        getMovieByCategory(Constant.TOP_RATED);
        getMovieByCategory(Constant.UP_COMING);
        getMovieByCategory(Constant.POPULAR);
        getMovieByCategory(Constant.NOW_PLAYING);
    }

    private void getMovieByCategory(final String category) {
        Disposable disposable = mMovieRepository.getMovieByCategory(category, Constant.PAGE_DEFAULT)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<BaseModel>() {
                    @Override
                    public void accept(BaseModel baseModel) throws Exception {
                        onGetDataSuccess(category, baseModel);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                    }
                });
        mCompositeDisposable.add(disposable);
    }

    private void onGetDataSuccess(String category, BaseModel baseModel) {
        switch (category) {
            case Constant.TOP_RATED:
                mAdapterTopRated.setMovieList(baseModel.getResults());
                break;
            case Constant.POPULAR:
                mAdapterPopular.setMovieList(baseModel.getResults());
                break;
            case Constant.NOW_PLAYING:
                mAdapterNowPlaying.setMovieList(baseModel.getResults());
                break;
            case Constant.UP_COMING:
                mAdapterUpComing.setMovieList(baseModel.getResults());
        }
    }

    public List<String> getImagePoster() {
        List<String> imageList = new ArrayList();
        imageList.add("https://image.tmdb.org/t/p/w500/nDS8rddEK74HfAwCC5CoT6Cwzlt.jpg");
        imageList.add("https://image.tmdb.org/t/p/w500/g8M39ttfQJDE1ocZFvjGS8iIFi7.jpg");
        imageList.add("https://image.tmdb.org/t/p/w500/urntO2GG4LZanHdYjeCh6Do2krF.jpg");
        imageList.add("https://image.tmdb.org/t/p/w500/12T5sdSInLxMEVHFwrb9kyeHI7D.jpg");
        return imageList;
    }

    public void onStop() {
        mCompositeDisposable.clear();
    }
}
