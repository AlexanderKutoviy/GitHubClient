package com.githubclient.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.githubclient.R;
import com.githubclient.dto.GithubUserDto;
import com.githubclient.presenter.FeedPresenter;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserRecyclerAdapter extends RecyclerView.Adapter<UserRecyclerAdapter.UserViewHolder> {

    public static final int TYPE_ITEM = 0;
    public static final int TYPE_ITEM_REFLECTION = 1;

    private List<GithubUserDto> githubUserDtos;
    private LayoutInflater inflater;
    private Context context;
    private FeedPresenter feedPresenter = FeedPresenter.INSTANCE;

    private boolean isLoadingAdded = false;
    private boolean retryPageLoad = false;
    private String errorMsg;

    public UserRecyclerAdapter(Context context, List<GithubUserDto> githubUserDtos) {
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.githubUserDtos = githubUserDtos;
        this.context = context;
    }

    @Override
    public int getItemViewType(int position) {
        if ((position + 1) % 3 == 0) {
            return TYPE_ITEM_REFLECTION;
        } else {
            return TYPE_ITEM;
        }
    }

    @Override
    public UserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_ITEM_REFLECTION) {
            View view = inflater.inflate(R.layout.item_list_reflected, parent, false);
            return new UserViewHolder(view);
        } else {
            View view = inflater.inflate(R.layout.item_list, parent, false);
            return new UserViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(final UserViewHolder holder, final int position) {
        if (githubUserDtos == null) {
            return;
        }
        GithubUserDto item = githubUserDtos.get(position);

        holder.loginTextView.setText(item.login);
        holder.login = item.login;

        Glide.with(context)
                .load(item.avatarUrl)
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model,
                                               Target<GlideDrawable> target,
                                               boolean isFirstResource) {
                        // TODO: handle failure
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model,
                                                   Target<GlideDrawable> target,
                                                   boolean isFromMemoryCache,
                                                   boolean isFirstResource) {
                        return false;
                    }
                })
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop()
                .crossFade()
                .into(holder.avatar);
    }

    @Override
    public int getItemCount() {
        return githubUserDtos.size();
    }

    class UserViewHolder extends RecyclerView.ViewHolder {
        public TextView loginTextView;
        public CircleImageView avatar;
        public Button followersBtn;
        public String login;

        public UserViewHolder(View view) {
            super(view);
            loginTextView = (TextView) view.findViewById(R.id.login);
            avatar = (CircleImageView) view.findViewById(R.id.avatar);
            followersBtn = (Button) view.findViewById(R.id.followers_btn);
            followersBtn.setOnClickListener(holder -> feedPresenter.showUsersFollowers(login));
        }
    }

}