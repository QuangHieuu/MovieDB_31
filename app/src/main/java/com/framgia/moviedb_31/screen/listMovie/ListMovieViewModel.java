package com.framgia.moviedb_31.screen.listMovie;

import android.content.Context;
import android.widget.Toast;
import com.framgia.moviedb_31.data.model.BaseModel;
import com.framgia.moviedb_31.data.repository.MovieRepository;
import com.framgia.moviedb_31.data.source.remote.RemoteDataSource;
import com.framgia.moviedb_31.screen.movieDetail.MovieDetailActivity;
import com.framgia.moviedb_31.utils.Constant;
import com.framgia.moviedb_31.utils.ItemClickListener;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class ListMovieViewModel implements ItemClickListener {

    private ListMovieAdapter mListMovieAdapter;
    private CompositeDisposable mCompositeDisposable;
    private MovieRepository mMovieRepository;
    private Context mContext;
    private String mKey;
    private String mValue;

    public ListMovieViewModel(Context context, String key, String value) {
        mContext = context;
        mListMovieAdapter = new ListMovieAdapter();
        mCompositeDisposable = new CompositeDisposable();
        mMovieRepository = MovieRepository.getInstance(RemoteDataSource.getsInstance());
        mListMovieAdapter.setItemClickListener(this);
        mKey = key;
        mValue = value;
        setQueryData();
    }

    public ListMovieAdapter getListMovieAdapter() {
        return mListMovieAdapter;
    }

    private void setQueryData() {
        switch (mKey) {
            case Constant.TYPE_SEARCH:
                getMovieBySearch(mValue, Constant.PAGE_DEFAULT);
                break;
            case Constant.TYPE_CATEGORY:
                getListMovieByCategory(mValue, Constant.PAGE_DEFAULT);
                break;
            case Constant.TYPE_GENRES:
                getMovieByGenres(mValue, Constant.PAGE_DEFAULT);
                break;
        }
    }

    public void loadMoreData(int page) {
        switch (mKey) {
            case Constant.TYPE_SEARCH:
                getMovieBySearch(mValue, page);
                break;
            case Constant.TYPE_CATEGORY:
                getListMovieByCategory(mValue, page);
                break;
            case Constant.TYPE_GENRES:
                getMovieByGenres(mValue, page);
                break;
        }
    }

    private void onSetData(int page, BaseModel baseModel) {
        if (page == Constant.PAGE_DEFAULT) {
            mListMovieAdapter.updateAdapter(baseModel.getResults());
        } else {
            mListMovieAdapter.loadMoreMovie(baseModel.getResults());
        }
    }

    private void getListMovieByCategory(String category, final int page) {
        Disposable disposable = mMovieRepository.getMovieByCategory(category, page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<BaseModel>() {
                    @Override
                    public void accept(BaseModel baseModel) throws Exception {
                        onSetData(page, baseModel);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Toast.makeText(mContext, throwable.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
        mCompositeDisposable.add(disposable);
    }

    private void getMovieBySearch(String query, final int page) {
        Disposable disposable = mMovieRepository.getMovieBySearch(query, page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<BaseModel>() {
                    @Override
                    public void accept(BaseModel baseModel) throws Exception {
                        onSetData(page, baseModel);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Toast.makeText(mContext, throwable.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
        mCompositeDisposable.add(disposable);
    }

    private void getMovieByGenres(String query, final int page) {
        Disposable disposable = mMovieRepository.getMovieByGenres(query, page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<BaseModel>() {
                    @Override
                    public void accept(BaseModel baseModel) throws Exception {
                        onSetData(page, baseModel);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Toast.makeText(mContext, throwable.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
        mCompositeDisposable.add(disposable);
    }

    @Override
    public void onItemClicked(String id) {
        mContext.startActivity(MovieDetailActivity.newIntent(mContext, id));
    }
}
