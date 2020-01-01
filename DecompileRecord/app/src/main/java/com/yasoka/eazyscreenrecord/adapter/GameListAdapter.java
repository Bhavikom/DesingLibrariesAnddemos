package com.yasoka.eazyscreenrecord.adapter;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.support.p000v4.content.ContextCompat;
import android.support.p003v7.app.AppCompatActivity;
import android.support.p003v7.widget.AppCompatButton;
import android.support.p003v7.widget.AppCompatImageView;
import android.support.p003v7.widget.AppCompatTextView;
import android.support.p003v7.widget.RecyclerView.Adapter;
import android.support.p003v7.widget.RecyclerView.ViewHolder;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import com.bumptech.glide.Glide;
import com.ezscreenrecorder.C0793R;
import com.ezscreenrecorder.FloatingService;
import com.ezscreenrecorder.RecorderApplication;
import com.ezscreenrecorder.activities.RecordingActivity;
import com.ezscreenrecorder.server.model.GamesListModels.GameDataModel;
import com.ezscreenrecorder.utils.FirebaseEventsNewHelper;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import p009io.reactivex.Single;
import p009io.reactivex.SingleObserver;
import p009io.reactivex.disposables.Disposable;

public class GameListAdapter extends Adapter<GameListHolder> {
    /* access modifiers changed from: private */
    public AppCompatActivity mActivity;
    /* access modifiers changed from: private */
    public List<GameDataModel> mList;
    private List<GameDataModel> mTempList;

    class GameListHolder extends ViewHolder implements OnClickListener {
        /* access modifiers changed from: private */
        public AppCompatButton actionButton;
        /* access modifiers changed from: private */
        public AppCompatImageView gameImage;
        /* access modifiers changed from: private */
        public AppCompatTextView subTitleTextView;
        /* access modifiers changed from: private */
        public AppCompatTextView titleTextView;

        public GameListHolder(View view) {
            super(view);
            this.gameImage = (AppCompatImageView) view.findViewById(C0793R.C0795id.id_game_item_image_view);
            this.titleTextView = (AppCompatTextView) view.findViewById(C0793R.C0795id.id_game_item_title_text_view);
            this.subTitleTextView = (AppCompatTextView) view.findViewById(C0793R.C0795id.id_game_item_subtitle_text_view);
            this.actionButton = (AppCompatButton) view.findViewById(C0793R.C0795id.id_game_item_action_button);
            this.actionButton.setOnClickListener(this);
            view.setOnClickListener(this);
        }

        public void onClick(View view) {
            if (!GameListAdapter.this.mActivity.isFinishing()) {
                int adapterPosition = getAdapterPosition();
                if (adapterPosition != -1) {
                    GameDataModel gameDataModel = (GameDataModel) GameListAdapter.this.mList.get(adapterPosition);
                    if (gameDataModel != null) {
                        if (gameDataModel.isInstalled()) {
                            launchAndRecordGame(gameDataModel.getPackageName());
                            FirebaseEventsNewHelper.getInstance().sendGameRecordStartEvent(gameDataModel.getPackageName());
                        } else {
                            openPlayStoreForInstall(gameDataModel.getPackageName());
                            FirebaseEventsNewHelper.getInstance().sendGameInstallStartEvent(gameDataModel.getPackageName());
                        }
                    }
                }
            }
        }

        private void launchAndRecordGame(final String str) {
            if (GameListAdapter.this.mActivity.getPackageManager().getLaunchIntentForPackage(str) != null) {
                Single.timer(20, TimeUnit.MILLISECONDS).subscribe((SingleObserver<? super T>) new SingleObserver<Long>() {
                    public void onSubscribe(Disposable disposable) {
                    }

                    public void onSuccess(Long l) {
                        Intent intent = new Intent(GameListAdapter.this.mActivity, RecordingActivity.class);
                        intent.putExtra(FloatingService.KEY_ACTION_TYPE_FROM_MAIN_FLOATING_BUTTONS, FloatingService.EXTRA_MAIN_ACTION_TYPE_GAME_RECORD);
                        intent.putExtra(RecordingActivity.EXTRA_GAME_LIST_PACKAGE_NAME, str);
                        intent.addFlags(268435456);
                        GameListAdapter.this.mActivity.startActivity(intent);
                        GameListAdapter.this.mActivity.finish();
                    }

                    public void onError(Throwable th) {
                        GameListAdapter.this.mActivity.finish();
                    }
                });
            }
        }

        private void openPlayStoreForInstall(String str) {
            String str2 = "android.intent.action.VIEW";
            try {
                AppCompatActivity access$400 = GameListAdapter.this.mActivity;
                StringBuilder sb = new StringBuilder();
                sb.append("http://play.google.com/store/apps/details?id=");
                sb.append(str.trim());
                access$400.startActivity(new Intent(str2, Uri.parse(sb.toString())));
            } catch (ActivityNotFoundException unused) {
                StringBuilder sb2 = new StringBuilder();
                sb2.append("market://details?id=");
                sb2.append(str.trim());
                Intent intent = new Intent(str2, Uri.parse(sb2.toString()));
                intent.addFlags(1208483840);
                GameListAdapter.this.mActivity.startActivity(intent);
            }
        }
    }

    public GameListAdapter(AppCompatActivity appCompatActivity) {
        if (this.mList == null) {
            this.mList = new ArrayList();
        }
        if (this.mTempList == null) {
            this.mTempList = new ArrayList();
        }
        this.mActivity = appCompatActivity;
    }

    public void addItem(GameDataModel gameDataModel) {
        this.mList.add(gameDataModel);
        this.mTempList.add(gameDataModel);
        notifyItemInserted(this.mList.size() - 1);
    }

    public void addList(List<GameDataModel> list) {
        this.mList.addAll(list);
        this.mTempList.addAll(list);
        notifyItemRangeInserted(0, this.mList.size());
    }

    public void clearList() {
        List<GameDataModel> list = this.mList;
        if (list != null) {
            list.clear();
            notifyDataSetChanged();
        }
        List<GameDataModel> list2 = this.mTempList;
        if (list2 != null) {
            list2.clear();
        }
    }

    public List<GameDataModel> getGameList() {
        return this.mList;
    }

    public GameListHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new GameListHolder(LayoutInflater.from(viewGroup.getContext()).inflate(C0793R.layout.custom_game_list_item_view, viewGroup, false));
    }

    public void onBindViewHolder(GameListHolder gameListHolder, int i) {
        if (i != -1) {
            GameDataModel gameDataModel = (GameDataModel) this.mList.get(i);
            if (gameDataModel != null) {
                gameListHolder.titleTextView.setText(gameDataModel.getGameName());
                if (!TextUtils.isEmpty(gameDataModel.getCategory())) {
                    gameListHolder.subTitleTextView.setText(gameDataModel.getCategory());
                } else {
                    gameListHolder.subTitleTextView.setText("");
                }
                if (gameDataModel.isLocalApplication()) {
                    gameListHolder.gameImage.setImageDrawable(gameDataModel.getGameImageDrawable());
                } else {
                    Glide.with(RecorderApplication.getInstance().getApplicationContext()).load(gameDataModel.getGameImage()).centerCrop().placeholder((int) C0793R.C0794drawable.ic_default_game_icon_48dp).error((int) C0793R.C0794drawable.ic_default_game_icon_48dp).dontAnimate().into(gameListHolder.gameImage);
                }
                if (gameDataModel.isInstalled()) {
                    gameListHolder.actionButton.setBackgroundResource(C0793R.C0794drawable.ic_game_list_record_button);
                    gameListHolder.actionButton.setTextColor(ContextCompat.getColor(RecorderApplication.getInstance().getApplicationContext(), 17170455));
                    gameListHolder.actionButton.setText(C0793R.string.id_record_txt);
                    return;
                }
                gameListHolder.actionButton.setBackgroundResource(C0793R.C0794drawable.ic_game_list_install_button);
                gameListHolder.actionButton.setTextColor(ContextCompat.getColor(RecorderApplication.getInstance().getApplicationContext(), 17170453));
                gameListHolder.actionButton.setText(C0793R.string.id_install_txt);
            }
        }
    }

    public int getItemCount() {
        return this.mList.size();
    }

    public void performSearch(String str) {
        animateTo(getFilteredList(str.trim()));
    }

    private List<GameDataModel> getFilteredList(String str) {
        ArrayList arrayList = new ArrayList();
        if (!TextUtils.isEmpty(str)) {
            String lowerCase = str.trim().toLowerCase();
            for (int i = 0; i < this.mTempList.size(); i++) {
                GameDataModel gameDataModel = (GameDataModel) this.mTempList.get(i);
                if (gameDataModel.getGameName().toLowerCase().contains(lowerCase) || (!TextUtils.isEmpty(gameDataModel.getCategory()) && gameDataModel.getCategory().toLowerCase().contains(lowerCase))) {
                    arrayList.add(gameDataModel);
                }
            }
        } else {
            arrayList.addAll(this.mTempList);
        }
        return arrayList;
    }

    private void animateTo(List<GameDataModel> list) {
        applyAndAnimateRemovals(list);
        applyAndAnimateAdditions(list);
        applyAndAnimateMovedItems(list);
    }

    private void applyAndAnimateRemovals(List<GameDataModel> list) {
        for (int size = this.mList.size() - 1; size >= 0; size--) {
            if (!list.contains((GameDataModel) this.mList.get(size))) {
                removeItem(size);
            }
        }
    }

    private void applyAndAnimateAdditions(List<GameDataModel> list) {
        int size = list.size();
        for (int i = 0; i < size; i++) {
            GameDataModel gameDataModel = (GameDataModel) list.get(i);
            if (!this.mList.contains(gameDataModel)) {
                addItem(i, gameDataModel);
            }
        }
    }

    private void applyAndAnimateMovedItems(List<GameDataModel> list) {
        for (int size = list.size() - 1; size >= 0; size--) {
            int indexOf = this.mList.indexOf((GameDataModel) list.get(size));
            if (indexOf >= 0 && indexOf != size) {
                moveItem(indexOf, size);
            }
        }
    }

    private Object removeItem(int i) {
        Object remove = this.mList.remove(i);
        notifyItemRemoved(i);
        return remove;
    }

    private void addItem(int i, GameDataModel gameDataModel) {
        this.mList.add(i, gameDataModel);
        notifyItemInserted(i);
    }

    private void moveItem(int i, int i2) {
        this.mList.add(i2, (GameDataModel) this.mTempList.remove(i));
        notifyItemMoved(i, i2);
    }
}
