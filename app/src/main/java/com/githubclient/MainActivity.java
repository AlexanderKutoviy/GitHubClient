package com.githubclient;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.githubclient.dto.GithubUserDto;
import com.githubclient.presenter.FeedPresenter;
import com.githubclient.view.FeedViewFragment;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (getFragmentManager().findFragmentByTag(FeedViewFragment.class.getName()) == null) {
            FeedViewFragment feedViewFragment = FeedViewFragment.newInstance();
            getFragmentManager().beginTransaction()
                    .replace(R.id.content_fragment, feedViewFragment,
                            feedViewFragment.getClass().getName())
                    .commit();
        }
    }
}