package com.framgia.moviedb_31.data.source;

import com.framgia.moviedb_31.data.model.BaseCredit;
import com.framgia.moviedb_31.data.model.BaseModel;
import com.framgia.moviedb_31.data.model.Movie;
import io.reactivex.Flowable;

public interface MovieDataSource {
    interface Remote {
        Flowable<BaseModel> getMovieByCategory(String category, int page);

        Flowable<Movie> getMovieDetail(String id, String type);

        Flowable<BaseCredit> getMovieCredit(String id);

        Flowable<BaseModel> getListGenres();

        Flowable<BaseModel> getMovieBySearch(String query, int page);

        Flowable<BaseModel> getMovieByGenres(String query, int page);

        Flowable<BaseCredit> getMovieVideo(String query);
    }
}
