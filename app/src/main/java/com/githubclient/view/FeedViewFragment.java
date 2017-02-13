package com.githubclient.view;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.githubclient.R;
import com.githubclient.dto.GithubUserDto;
import com.githubclient.presenter.FeedPresenter;

import java.util.List;

public class FeedViewFragment extends Fragment implements FeedView {

    private final String TAG = FeedViewFragment.class.getSimpleName();
    private static final String ARG_LOGIN = "login";
    private Context context;
    private FeedPresenter feedPresenter = FeedPresenter.INSTANCE;
    private UserRecyclerAdapter adapter;
    private View rootView;

    private LinearLayoutManager linearLayoutManager;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private LinearLayout errorLayout;
    private Button btnRetry;
    private TextView txtError;

    public static FeedViewFragment newInstance() {
        FeedViewFragment fragment = new FeedViewFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public static FeedViewFragment newInstance(String login) {
        FeedViewFragment fragment = new FeedViewFragment();
        Bundle args = new Bundle();
        args.putString(ARG_LOGIN, login);
        fragment.setArguments(args);
        return fragment;
    }

    public FeedViewFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.feed_view_layout, container, false);
        context = getActivity();
        initViews();
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        feedPresenter.attachView(this);
        loadData();
    }

    @Override
    public void onPause() {
        feedPresenter.detachView(this);
        super.onPause();
    }

    @Override
    public void setAdapter(List<GithubUserDto> userDtos) {
        adapter = new UserRecyclerAdapter(getActivity(), userDtos);
        recyclerView.setAdapter(adapter);
        progressBar.setVisibility(View.GONE);
        Log.d(TAG, "set adapter");
    }

    @Override
    public void showErrorView(String errorMsg) {
        if (errorLayout.getVisibility() == View.GONE) {
            errorLayout.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);
            txtError.setText(errorMsg);
        }
    }

    @Override
    public void hideErrorView() {
        if (errorLayout.getVisibility() == View.VISIBLE) {
            errorLayout.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public Context getContext() {
        return context;
    }

    @Override
    public void showFollowers(FeedViewFragment feedViewFragment) {
        Log.d("TAG", "show followers");
        getFragmentManager().beginTransaction().replace(R.id.content_fragment,
                feedViewFragment, feedViewFragment.getClass().getName()).addToBackStack(null).commit();
    }

    private void initViews() {
        progressBar = (ProgressBar) rootView.findViewById(R.id.main_progress);
        errorLayout = (LinearLayout) rootView.findViewById(R.id.error_layout);
        btnRetry = (Button) rootView.findViewById(R.id.error_btn_retry);
        txtError = (TextView) rootView.findViewById(R.id.error_txt_cause);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.main_recycler);
        linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        btnRetry.setOnClickListener(btn -> loadData());
    }

    private void loadData() {
        if (getArguments().containsKey(ARG_LOGIN)) {
            feedPresenter.initializeData(getArguments().getString(ARG_LOGIN));
        } else {
            feedPresenter.initializeData();
        }
    }
}