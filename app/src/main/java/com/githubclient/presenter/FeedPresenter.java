package com.githubclient.presenter;

import android.content.Context;
import android.net.ConnectivityManager;
import android.util.Log;

import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;
import com.githubclient.R;
import com.githubclient.api.GitHubService;
import com.githubclient.api.RestApi;
import com.githubclient.dto.GithubUserDto;
import com.githubclient.util.Schedulers;
import com.githubclient.view.FeedView;
import com.githubclient.view.FeedViewFragment;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeoutException;

import rx.Observable;
import rx.subscriptions.CompositeSubscription;

public enum FeedPresenter {
    INSTANCE;

    private final String TAG = FeedPresenter.class.getSimpleName();

    private Context context;
    private FeedView feedView;
    private List<GithubUserDto> userDtoList;
    private static final int PAGE_START = 1;

    private int currentPage = PAGE_START;
    private String since;
    private CompositeSubscription subscriptions;
    private GitHubService gitHubService;

    public void attachView(FeedView feedView) {
        this.feedView = feedView;
        context = feedView.getContext();
        gitHubService = RestApi.getClient(RestApi.GIT_HUB_URL).create(GitHubService.class);
        userDtoList = new ArrayList<>(200);
        subscriptions = new CompositeSubscription();
        Log.d(TAG, "Attach");
    }

    public void detachView(FeedView feedView) {
        if (this.feedView == feedView) {
            this.feedView = null;
            userDtoList = null;
            gitHubService = null;
            subscriptions.unsubscribe();
            Log.d(TAG, "Detach");
        }
    }

    public void initializeData(String login) {
        feedView.hideErrorView();
        subscriptions.add(getFollowers(login).subscribeOn(Schedulers.io())
                .observeOn(Schedulers.main())
                .subscribe((dto) -> {
                    feedView.hideErrorView();
                    List<GithubUserDto> followersList = new ArrayList<>();
                    followersList = dto;
                    Log.d(TAG, "followers list size" + followersList.size());

                    if (followersList.isEmpty()) {
                        feedView.showErrorView(context.getResources().getString(R.string.no_followers_msg));
                    }

                    feedView.setAdapter(Stream.of(followersList)
                            .sorted((object1, object2) ->
                                    object1.login.compareTo(object2.login))
                            .collect(Collectors.toList()));
                }, error -> {
                    error.printStackTrace();
                    Log.e(TAG, "ON ERROR FOLLOWERS");
                    feedView.showErrorView(fetchErrorMessage(error));
                })
        );
    }

    public void initializeData() {
        feedView.hideErrorView();
        subscriptions.add(callGithubUsersApi().subscribeOn(Schedulers.io())
                .observeOn(Schedulers.main())
                .subscribe((dto) -> {
                    feedView.hideErrorView();
                    userDtoList = dto;
                    since = userDtoList.get(userDtoList.size() - 1).id;
                    loadSecondPage(since);
                }, error -> {
                    error.printStackTrace();
                    feedView.showErrorView(fetchErrorMessage(error));
                })
        );
    }

    private void loadSecondPage(String since) {
        if (since != null) {
            subscriptions.add(callGithubUsersApi(since).subscribeOn(Schedulers.io())
                    .observeOn(Schedulers.main())
                    .subscribe((dto) -> {
                        userDtoList.addAll(dto);
                        Log.d(TAG, "users list size" + userDtoList.size());

                        feedView.setAdapter(Stream.of(userDtoList)
                                .sorted((object1, object2) ->
                                        object1.login.compareTo(object2.login))
                                .collect(Collectors.toList()));
                    }, error -> {
                        error.printStackTrace();
                        feedView.showErrorView(fetchErrorMessage(error));
                    })
            );
        }
    }

    private String fetchErrorMessage(Throwable throwable) {
        String errorMsg = context.getResources().getString(R.string.error_msg_unknown);
        if (!isNetworkConnected()) {
            errorMsg = context.getResources().getString(R.string.error_msg_no_internet);
        } else if (throwable instanceof TimeoutException) {
            errorMsg = context.getResources().getString(R.string.error_msg_timeout);
        }

        Log.e(TAG, throwable.getMessage());
        Log.e(TAG, errorMsg);

        return errorMsg;
    }

    public boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }

    private Observable<List<GithubUserDto>> callGithubUsersApi() {
        return gitHubService.getUsers(currentPage,
                100,
                context.getString(R.string.my_client_id),
                context.getString(R.string.my_client_secret));
    }

    private Observable<List<GithubUserDto>> callGithubUsersApi(String since) {
        return gitHubService.getUsers(currentPage,
                100,
                since,
                context.getString(R.string.my_client_id),
                context.getString(R.string.my_client_secret));
    }

    private Observable<List<GithubUserDto>> getFollowers(String login) {
        return gitHubService.getFollowers(login,
                context.getString(R.string.my_client_id),
                context.getString(R.string.my_client_secret));
    }

    public void showUsersFollowers(String login) {
        feedView.showFollowers(FeedViewFragment.newInstance(login));
    }
}