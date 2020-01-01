package com.yasoka.eazyscreenrecord.activities.live_twitch;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.p003v7.widget.AppCompatEditText;
import android.support.p003v7.widget.AppCompatImageView;
import android.support.p003v7.widget.AppCompatTextView;
import android.support.p003v7.widget.LinearLayoutManager;
import android.support.p003v7.widget.RecyclerView;
import android.support.p003v7.widget.RecyclerView.Adapter;
import android.support.p003v7.widget.RecyclerView.ViewHolder;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import com.ezscreenrecorder.C0793R;
import com.ezscreenrecorder.activities.BaseToolbarActivity;
import com.ezscreenrecorder.server.model.LiveTwitchModels.Game;
import com.ezscreenrecorder.utils.LiveTwitchHelper;
import com.ezscreenrecorder.utils.LiveTwitchHelper.OnGameListListener;
import java.util.ArrayList;
import java.util.List;

public class LiveTwitchGameListActivity extends BaseToolbarActivity implements OnClickListener {
    private AppCompatImageView closeButton;
    /* access modifiers changed from: private */
    public TwitchGameListAdapter mAdapter;
    /* access modifiers changed from: private */
    public List<Game> mGameList;
    private ProgressBar progressBar;
    /* access modifiers changed from: private */
    public RecyclerView recyclerView;
    private AppCompatImageView searchButton;
    private AppCompatEditText searchEditText;

    private class TwitchGameListAdapter extends Adapter<TwitchGameListViewHolder> {
        public TwitchGameListAdapter() {
            LiveTwitchGameListActivity.this.mGameList = new ArrayList();
        }

        public void addList(List<Game> list) {
            LiveTwitchGameListActivity.this.mGameList.clear();
            LiveTwitchGameListActivity.this.mGameList.addAll(list);
            notifyItemRangeInserted(0, LiveTwitchGameListActivity.this.mGameList.size());
        }

        @NonNull
        public TwitchGameListViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            return new TwitchGameListViewHolder(LiveTwitchGameListActivity.this.getLayoutInflater().inflate(C0793R.layout.custom_live_twitch_game_list_item, viewGroup, false));
        }

        public void onBindViewHolder(@NonNull TwitchGameListViewHolder twitchGameListViewHolder, int i) {
            if (i != -1) {
                twitchGameListViewHolder.gameName.setText(((Game) LiveTwitchGameListActivity.this.mGameList.get(i)).getName());
            }
        }

        public int getItemCount() {
            return LiveTwitchGameListActivity.this.mGameList.size();
        }
    }

    private class TwitchGameListViewHolder extends ViewHolder implements OnClickListener {
        private AppCompatImageView addButton;
        /* access modifiers changed from: private */
        public AppCompatTextView gameName;

        public TwitchGameListViewHolder(View view) {
            super(view);
            this.gameName = (AppCompatTextView) view.findViewById(C0793R.C0795id.id_custom_live_twitch_game_list_item_game_name_text_view);
            this.addButton = (AppCompatImageView) view.findViewById(C0793R.C0795id.id_custom_live_twitch_game_list_item_add_image_view);
            this.addButton.setOnClickListener(this);
            view.setOnClickListener(this);
        }

        public void onClick(View view) {
            int adapterPosition = getAdapterPosition();
            if (adapterPosition != -1) {
                view.getId();
                LiveTwitchGameListActivity liveTwitchGameListActivity = LiveTwitchGameListActivity.this;
                liveTwitchGameListActivity.sendResult((Game) liveTwitchGameListActivity.mGameList.get(adapterPosition));
            }
        }
    }

    /* access modifiers changed from: protected */
    public void onCreate(@Nullable Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) C0793R.layout.activity_live_twitch_game_list);
        init();
    }

    private void init() {
        this.closeButton = (AppCompatImageView) findViewById(C0793R.C0795id.id_live_twitch_game_list_close_image_view);
        this.searchButton = (AppCompatImageView) findViewById(C0793R.C0795id.id_live_twitch_game_list_search_image_view);
        this.searchEditText = (AppCompatEditText) findViewById(C0793R.C0795id.id_live_twitch_game_list_search_edit_text);
        this.recyclerView = (RecyclerView) findViewById(C0793R.C0795id.id_live_twitch_game_list_recycler_view);
        this.progressBar = (ProgressBar) findViewById(C0793R.C0795id.id_live_twitch_game_list_progress_view);
        this.closeButton.setOnClickListener(this);
        this.searchButton.setOnClickListener(this);
        this.searchEditText.setOnEditorActionListener(new OnEditorActionListener() {
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i != 3) {
                    return false;
                }
                LiveTwitchGameListActivity.this.performSearch();
                return true;
            }
        });
    }

    /* access modifiers changed from: private */
    public void performSearch() {
        if (this.searchEditText.getText() != null && !TextUtils.isEmpty(this.searchEditText.getText().toString().trim())) {
            LiveTwitchHelper.getInstance().searchGameListForText(this.searchEditText.getText().toString().trim(), new OnGameListListener() {
                public void onStart() {
                    LiveTwitchGameListActivity.this.showProgress(true);
                }

                public void onFailure() {
                    LiveTwitchGameListActivity.this.showProgress(false);
                }

                public void onComplete(List<Game> list) {
                    LiveTwitchGameListActivity.this.showProgress(false);
                    if (list != null && list.size() > 0) {
                        if (LiveTwitchGameListActivity.this.mAdapter == null) {
                            LiveTwitchGameListActivity liveTwitchGameListActivity = LiveTwitchGameListActivity.this;
                            liveTwitchGameListActivity.mAdapter = new TwitchGameListAdapter();
                        }
                        if (LiveTwitchGameListActivity.this.recyclerView.getLayoutManager() == null) {
                            LiveTwitchGameListActivity.this.recyclerView.setLayoutManager(new LinearLayoutManager(LiveTwitchGameListActivity.this.getApplicationContext(), 1, false));
                        }
                        if (LiveTwitchGameListActivity.this.recyclerView.getAdapter() == null) {
                            LiveTwitchGameListActivity.this.recyclerView.setAdapter(LiveTwitchGameListActivity.this.mAdapter);
                        }
                        LiveTwitchGameListActivity.this.mAdapter.addList(list);
                    }
                }
            });
        }
    }

    /* access modifiers changed from: private */
    public void sendResult(Game game) {
        if (!isFinishing()) {
            LiveTwitchHelper.getInstance().setGameSelectedForLive(game);
            setResult(-1, new Intent());
            finish();
        }
    }

    public void onClick(View view) {
        int id = view.getId();
        if (id != C0793R.C0795id.id_live_twitch_game_list_close_image_view) {
            if (id == C0793R.C0795id.id_live_twitch_game_list_search_image_view) {
                performSearch();
            }
        } else if (!isFinishing()) {
            finish();
        }
    }

    /* access modifiers changed from: private */
    public void showProgress(boolean z) {
        if (!z) {
            this.progressBar.setVisibility(8);
        } else if (this.progressBar.getVisibility() == 8) {
            this.progressBar.setVisibility(0);
        }
    }
}
