package com.framgia.moviedb_31.screen.movieDetail;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.ObservableField;
import com.framgia.moviedb_31.BR;
import com.framgia.moviedb_31.data.model.BaseCredit;
import com.framgia.moviedb_31.data.model.Movie;
import com.framgia.moviedb_31.data.repository.MovieRepository;
import com.framgia.moviedb_31.data.source.remote.RemoteDataSource;
import com.framgia.moviedb_31.utils.Constant;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class MovieDetailViewModel extends BaseObservable {
    private ObservableField<Movie> mMovie;
    private MovieDetailProductionAdapter mMovieDetailProductionAdapter;
    private MovieDetailActorAdapter mMovieDetailActorAdapter;
    private MovieRepository mMovieRepository;
    private CompositeDisposable mCompositeDisposable;
    private String mKeyYoutube;

    void onStop() {
        mCompositeDisposable.clear();
    }

    public MovieDetailProductionAdapter getMovieDetailProductionAdapter() {
        return mMovieDetailProductionAdapter;
    }

    public MovieDetailActorAdapter getMovieDetailActorAdapter() {
        return mMovieDetailActorAdapter;
    }

    MovieDetailViewModel(String id) {
        mMovie = new ObservableField<>();
        mMovieDetailProductionAdapter = new MovieDetailProductionAdapter();
        mMovieDetailActorAdapter = new MovieDetailActorAdapter();
        mMovieRepository = MovieRepository.getInstance(RemoteDataSource.getsInstance());
        mCompositeDisposable = new CompositeDisposable();
        getDataMovieContent(id);
        getDataItemMovie(id);
        getMovieVideo(id);
    }

    public void getDataMovieContent(String id) {
        Disposable disposable = mMovieRepository.getMovieDetail(id, Constant.TYPE)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Movie>() {
                    @Override
                    public void accept(Movie movie) {
                        setMovie(movie);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) {
                        throwable.printStackTrace();
                    }
                });
        mCompositeDisposable.add(disposable);
    }

    public void getDataItemMovie(String id) {
        Disposable disposable = mMovieRepository.getMovieCredit(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<BaseCredit>() {
                    @Override
                    public void accept(BaseCredit baseCredit) {
                        setDataAdapter(baseCredit);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) {
                        throwable.printStackTrace();
                    }
                });
        mCompositeDisposable.add(disposable);
    }

    private void getMovieVideo(String id) {
        Disposable disposable = mMovieRepository.getMovieVideo(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<BaseCredit>() {
                    @Override
                    public void accept(BaseCredit baseCredit) {
                        mKeyYoutube = baseCredit.getVideoList().get(0).getKey();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) {
                        throwable.printStackTrace();
                    }
                });
        mCompositeDisposable.add(disposable);
    }

    private void setDataAdapter(BaseCredit baseCredit) {
        mMovieDetailActorAdapter.updateAdapter(baseCredit.getActorList());
        mMovieDetailProductionAdapter.updateAdapter(baseCredit.getProductionList());
    }

    @Bindable
    public Movie getMovie() {
        return mMovie.get();
    }

    public void setMovie(Movie movie) {
        mMovie.set(movie);
        notifyPropertyChanged(BR.movie);
    }

    public String getKeyYoutube() {
        return mKeyYoutube;
    }
}
