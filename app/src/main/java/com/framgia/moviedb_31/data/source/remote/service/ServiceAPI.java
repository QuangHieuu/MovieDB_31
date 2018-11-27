package com.framgia.moviedb_31.data.source.remote.service;

import com.framgia.moviedb_31.BuildConfig;
import com.framgia.moviedb_31.data.model.BaseCredit;
import com.framgia.moviedb_31.data.model.BaseModel;
import com.framgia.moviedb_31.data.model.Movie;
import io.reactivex.Flowable;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ServiceAPI {
    @GET("movie/{category}?api_key=" + BuildConfig.API_KEY)
    Flowable<BaseModel> getListMovieByCategory(@Path("category") String category,
            @Query("page") int page);

    @GET("movie/{id}?api_key=" + BuildConfig.API_KEY)
    Flowable<Movie> getMovieDetail(@Path("id") String id, @Query("append_to_response") String type);

    @GET("movie/{id}/credits?api_key=" + BuildConfig.API_KEY)
    Flowable<BaseCredit> getMovieCredit(@Path("id") String id);

    @GET("genre/movie/list?api_key=" + BuildConfig.API_KEY)
    Flowable<BaseModel> getListGenres();

    @GET("search/movie?api_key=" + BuildConfig.API_KEY)
    Flowable<BaseModel> getListMovieBySearch(@Query("query") String query, @Query("page") int page);

    @GET("search/movie?api_key=" + BuildConfig.API_KEY)
    Flowable<BaseModel> getMovieByGenres(@Query("query") String query, @Query("page") int page);

    @GET("movie/{moiveId}/videos?api_key=" + BuildConfig.API_KEY)
    Flowable<BaseCredit> getVideo(@Path("moiveId") String query);
}
