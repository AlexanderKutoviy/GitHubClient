package com.githubclient.dto;

import com.githubclient.util.Objects;
import com.google.gson.annotations.SerializedName;

public class GithubUserDto {

    @SerializedName("id")
    public String id;
    @SerializedName("login")
    public String login;
    @SerializedName("avatar_url")
    public String avatarUrl;
    @SerializedName("followers_url")
    public String followersUrl;

    @Override
    public int hashCode() {
        return Objects.hash(id,
                login,
                avatarUrl,
                followersUrl);
    }

    @Override
    public boolean equals(Object object) {
        if (object == null || !(object instanceof GithubUserDto)) {
            return false;
        }
        GithubUserDto dto = (GithubUserDto) object;
        return Objects.equal(id, dto.id)
                && Objects.equal(login, dto.login)
                && Objects.equal(avatarUrl, dto.avatarUrl)
                && Objects.equal(followersUrl, dto.followersUrl);
    }

    @Override
    public String toString() {
        return "GithubUserDto{" +
                "id='" + id + '\'' +
                ", loginTextView='" + login + '\'' +
                ", avatarUrl='" + avatarUrl + '\'' +
                ", followersUrl='" + followersUrl + '\'' +
                '}';
    }
}