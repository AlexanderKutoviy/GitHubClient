package com.githubclient.view;

import android.content.Context;

import com.githubclient.dto.GithubUserDto;

import java.util.List;

public interface FeedView {

    Context getContext();

    void setAdapter(List<GithubUserDto> userDtos);

    void showErrorView(String errorMsg);

    void showFollowers(FeedViewFragment feedViewFragment);

    void hideErrorView();
}
