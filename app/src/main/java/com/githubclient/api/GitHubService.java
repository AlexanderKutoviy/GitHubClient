package com.githubclient.api;

import com.githubclient.dto.GithubUserDto;

import java.util.List;

import retrofit2.http.Field;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

public interface GitHubService {

    @GET("users")
    Observable<List<GithubUserDto>> getUsers(@Query("page") int page,
                                             @Query("per_page") int perPage,
                                             @Query("client_id") String clientId,
                                             @Query("client_secret") String clientSecret);

    @GET("users")
    Observable<List<GithubUserDto>> getUsers(@Query("page") int page,
                                             @Query("per_page") int perPage,
                                             @Query("since") String since,
                                             @Query("client_id") String clientId,
                                             @Query("client_secret") String clientSecret);

    @GET("/users/{user}/followers")
    Observable<List<GithubUserDto>> getFollowers(@Path("user") String login,
                                                 @Query("client_id") String clientId,
                                                 @Query("client_secret") String clientSecret);
}
