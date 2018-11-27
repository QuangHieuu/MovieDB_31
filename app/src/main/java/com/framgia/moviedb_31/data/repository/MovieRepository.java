package com.framgia.moviedb_31.data.repository;

import com.framgia.moviedb_31.data.model.BaseCredit;
import com.framgia.moviedb_31.data.model.BaseModel;
import com.framgia.moviedb_31.data.model.Movie;
import com.framgia.moviedb_31.data.source.remote.RemoteDataSource;
import io.reactivex.Flowable;

public class MovieRepository {
    private static MovieRepository sInstance;
    private RemoteDataSource mRemoteDataSource;

    private MovieRepository(RemoteDataSource remoteDataSource) {
        mRemoteDataSource = remoteDataSource;
    }

    public static MovieRepository getInstance(RemoteDataSource remoteDataSource) {
        if (sInstance == null) {
            sInstance = new MovieRepository(remoteDataSource);
        }
        return sInstance;
    }

    public Flowable<BaseModel> getMovieByCategory(String category, int page) {
        return mRemoteDataSource.getMovieByCategory(category, page);
    }

    public Flowable<BaseModel> getListGenres() {
        return mRemoteDataSource.getListGenres();
    }

    public Flowable<BaseModel> getMovieBySearch(String query, int page) {
        return mRemoteDataSource.getMovieBySearch(query, page);
    }

    public Flowable<Movie> getMovieDetail(String id, String type) {
        return mRemoteDataSource.getMovieDetail(id, type);
    }

    public Flowable<BaseCredit> getMovieCredit(String id) {
        return mRemoteDataSource.getMovieCredit(id);
    }

    public Flowable<BaseModel> getMovieByGenres(String genresId, int page) {
        return mRemoteDataSource.getMovieByGenres(genresId, page);
    }

    public Flowable<BaseCredit> getMovieVideo(String movieId) {
        return mRemoteDataSource.getMovieVideo(movieId);
    }
}
