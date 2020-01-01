package com.yasoka.eazyscreenrecord.server.model.LiveTwitchModels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.List;

public class LiveTwitchGameListOutputModel {
    @SerializedName("games")
    @Expose
    private List<Game> games = null;

    public List<Game> getGames() {
        return this.games;
    }

    public void setGames(List<Game> list) {
        this.games = list;
    }
}
