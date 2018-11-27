package com.framgia.moviedb_31.screen.listgenres;

import android.databinding.ObservableField;
import com.framgia.moviedb_31.data.model.BaseModel;
import com.framgia.moviedb_31.data.repository.MovieRepository;
import com.framgia.moviedb_31.data.source.remote.RemoteDataSource;
import com.framgia.moviedb_31.utils.ItemClickListener;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class ListGenresViewModel {

    public ObservableField<ListGenresAdapter> mAdapterObservableField;
    private CompositeDisposable mCompositeDisposable;
    private MovieRepository mMovieRepository;
    private ListGenresAdapter mListGenresAdapter;

    public ListGenresViewModel(ItemClickListener listener) {
        mAdapterObservableField = new ObservableField<>();
        mCompositeDisposable = new CompositeDisposable();
        mMovieRepository = MovieRepository.getInstance(RemoteDataSource.getsInstance());
        mListGenresAdapter = new ListGenresAdapter(listener);
        mAdapterObservableField.set(mListGenresAdapter);
    }

    public void initData() {
        Disposable disposable = mMovieRepository.getListGenres()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<BaseModel>() {
                    @Override
                    public void accept(BaseModel genresBaseModel) throws Exception {
                        mListGenresAdapter.setGenresList(genresBaseModel.getGenres());
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                    }
                });
        mCompositeDisposable.add(disposable);
    }
}
