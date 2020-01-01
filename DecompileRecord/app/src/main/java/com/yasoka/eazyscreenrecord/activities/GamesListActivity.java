package com.yasoka.eazyscreenrecord.activities;

import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
/*import android.support.p000v4.widget.SwipeRefreshLayout;
import android.support.p000v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.support.p003v7.app.AppCompatActivity;
import android.support.p003v7.widget.AppCompatTextView;
import android.support.p003v7.widget.LinearLayoutManager;
import android.support.p003v7.widget.RecyclerView;
import android.support.p003v7.widget.SearchView;
import android.support.p003v7.widget.SearchView.OnQueryTextListener;
import android.support.p003v7.widget.Toolbar;*/
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
/*import com.ezscreenrecorder.C0793R;
import com.ezscreenrecorder.RecorderApplication;
import com.ezscreenrecorder.adapter.GameListAdapter;
import com.ezscreenrecorder.server.ServerAPI;
import com.ezscreenrecorder.server.model.GamesListModels.GameDataModel;
import com.ezscreenrecorder.server.model.GamesListModels.GamesMainModel;
import com.ezscreenrecorder.utils.PreferenceHelper;*/
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yasoka.eazyscreenrecord.C0793R;
import com.yasoka.eazyscreenrecord.RecorderApplication;
import com.yasoka.eazyscreenrecord.adapter.GameListAdapter;
import com.yasoka.eazyscreenrecord.server.ServerAPI;
import com.yasoka.eazyscreenrecord.server.model.GamesListModels.GameDataModel;
import com.yasoka.eazyscreenrecord.server.model.GamesListModels.GamesMainModel;
import com.yasoka.eazyscreenrecord.utils.PreferenceHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import org.reactivestreams.Publisher;

import io.reactivex.Flowable;
import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.DisposableSubscriber;
/*import p009io.reactivex.Flowable;
import p009io.reactivex.FlowableSubscriber;
import p009io.reactivex.Single;
import p009io.reactivex.SingleEmitter;
import p009io.reactivex.SingleObserver;
import p009io.reactivex.SingleOnSubscribe;
import p009io.reactivex.android.schedulers.AndroidSchedulers;
import p009io.reactivex.functions.Function;
import p009io.reactivex.observers.DisposableSingleObserver;
import p009io.reactivex.schedulers.Schedulers;
import p009io.reactivex.subscribers.DisposableSubscriber;*/

public class GamesListActivity extends AppCompatActivity implements OnRefreshListener {
    private DisposableSubscriber<GameDataModel> disposableSubscriber;
    /* access modifiers changed from: private */
    public AppCompatTextView emptyTextView;
    /* access modifiers changed from: private */
    public GameListAdapter mAdapter;
    /* access modifiers changed from: private */
    public RecyclerView recyclerView;
    private SearchView searchView;
    /* access modifiers changed from: private */
    public SwipeRefreshLayout swipeRefreshLayout;

    /* access modifiers changed from: protected */
    public void onCreate(@Nullable Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) C0793R.layout.activity_game_list);
        setupToolbar((Toolbar) findViewById(C0793R.C0795id.toolbar));
        this.recyclerView = (RecyclerView) findViewById(C0793R.C0795id.id_recycler_view);
        this.swipeRefreshLayout = (SwipeRefreshLayout) findViewById(C0793R.C0795id.id_swipe_refresh);
        this.emptyTextView = (AppCompatTextView) findViewById(C0793R.C0795id.id_empty_error_text_view);
        this.swipeRefreshLayout.setOnRefreshListener(this);
        new Handler().postDelayed(new Runnable() {
            public void run() {
                GamesListActivity.this.getGameList();
            }
        }, 100);
    }

    public void setupToolbar(Toolbar toolbar) {
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle((CharSequence) "");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(C0793R.C0797menu.menu_game_record, menu);
        this.searchView = (SearchView) menu.findItem(C0793R.C0795id.action_search).getActionView();
        this.searchView.setIconifiedByDefault(false);
        this.searchView.setOnQueryTextListener(new OnQueryTextListener() {
            public boolean onQueryTextSubmit(String str) {
                return false;
            }

            public boolean onQueryTextChange(String str) {
                if (GamesListActivity.this.mAdapter != null) {
                    GamesListActivity.this.mAdapter.performSearch(str);
                    if (GamesListActivity.this.recyclerView != null && str.length() == 0) {
                        GamesListActivity.this.recyclerView.scrollToPosition(0);
                    }
                }
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == 16908332) {
            finish();
        }
        return super.onOptionsItemSelected(menuItem);
    }

    /* access modifiers changed from: protected */
    public void onDestroy() {
        super.onDestroy();
        DisposableSubscriber<GameDataModel> disposableSubscriber2 = this.disposableSubscriber;
        if (disposableSubscriber2 != null && !disposableSubscriber2.isDisposed()) {
            this.disposableSubscriber.dispose();
        }
    }

    /* access modifiers changed from: private */
    public void getGameList() {
        if (this.mAdapter == null) {
            this.mAdapter = new GameListAdapter(this);
        }
        if (this.recyclerView.getLayoutManager() == null) {
            this.recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), 1, false));
        }
        this.emptyTextView.setVisibility(8);
        this.recyclerView.setAdapter(this.mAdapter);
        setRefreshing(true);
        this.mAdapter.clearList();
        SearchView searchView2 = this.searchView;
        if (searchView2 != null) {
            searchView2.setQuery("", false);
        }
        if (RecorderApplication.getInstance().isNetworkAvailable() || !PreferenceHelper.getInstance().hasGameListData()) {
            getGameRemoteData();
        } else {
            getGameLocalData();
        }
    }

    private void getGameRemoteData() {
        ServerAPI.getInstance().getGamesData().flatMapPublisher(new Function<GamesMainModel, Publisher<GameDataModel>>() {
            public Publisher<GameDataModel> apply(GamesMainModel gamesMainModel) throws Exception {
                try {
                    if (gamesMainModel.getData().getGameList().size() > 0) {
                        PreferenceHelper.getInstance().setGameListData(new Gson().toJson((Object) gamesMainModel.getData().getGameList(), new TypeToken<ArrayList<GameDataModel>>() {
                        }.getType()));
                    }
                    Intent intent = new Intent("android.intent.action.MAIN", null);
                    intent.addCategory("android.intent.category.LAUNCHER");
                    List<ResolveInfo> queryIntentActivities = GamesListActivity.this.getPackageManager().queryIntentActivities(intent, 0);
                    ArrayList arrayList = new ArrayList();
                    for (ResolveInfo resolveInfo : queryIntentActivities) {
                        GameDataModel gameDataModel = new GameDataModel();
                        GamesListActivity.this.getPackageManager().getApplicationLabel(resolveInfo.activityInfo.applicationInfo);
                        String str = (String) (resolveInfo.activityInfo.applicationInfo != null ? GamesListActivity.this.getPackageManager().getApplicationLabel(resolveInfo.activityInfo.applicationInfo) : "");
                        if (!TextUtils.isEmpty(str)) {
                            gameDataModel.setGameName(str);
                            gameDataModel.setInstalled(true);
                            gameDataModel.setPackageName(resolveInfo.activityInfo.packageName);
                            gameDataModel.setGameImageDrawable(GamesListActivity.this.getPackageManager().getApplicationIcon(resolveInfo.activityInfo.applicationInfo));
                            gameDataModel.setLocalApplication(true);
                            arrayList.add(gameDataModel);
                        }
                    }
                    for (int i = 0; i < gamesMainModel.getData().getGameList().size(); i++) {
                        GameDataModel gameDataModel2 = (GameDataModel) gamesMainModel.getData().getGameList().get(i);
                        gameDataModel2.setLocalApplication(false);
                        Iterator it = arrayList.iterator();
                        while (true) {
                            if (!it.hasNext()) {
                                break;
                            }
                            GameDataModel gameDataModel3 = (GameDataModel) it.next();
                            if (gameDataModel2.getPackageName().trim().equals(gameDataModel3.getPackageName())) {
                                gameDataModel2.setInstalled(true);
                                arrayList.remove(gameDataModel3);
                                break;
                            }
                        }
                    }
                    ArrayList arrayList2 = new ArrayList();
                    arrayList2.addAll(arrayList);
                    arrayList2.addAll(gamesMainModel.getData().getGameList());
                    return Flowable.fromIterable(arrayList2);
                } catch (Exception unused) {
                    return null;
                }
            }
        }).sorted(new Comparator<GameDataModel>() {
            public int compare(GameDataModel gameDataModel, GameDataModel gameDataModel2) {
                boolean z = false;
                if ((gameDataModel.isInstalled() && gameDataModel.isLocalApplication()) && (gameDataModel2.isInstalled() && gameDataModel2.isLocalApplication())) {
                    return 0;
                }
                if ((gameDataModel.isInstalled() && gameDataModel.isLocalApplication()) && (gameDataModel2.isInstalled() && !gameDataModel2.isLocalApplication())) {
                    return 1;
                }
                boolean z2 = gameDataModel.isInstalled() && !gameDataModel.isLocalApplication();
                if (gameDataModel2.isInstalled() && gameDataModel2.isLocalApplication()) {
                    z = true;
                }
                if ((!z2 || !z) && !gameDataModel.isInstalled() && gameDataModel2.isInstalled()) {
                    return 1;
                }
                return -1;
            }
        }).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread()).subscribe((FlowableSubscriber<? super T>) new DisposableSubscriber<GameDataModel>() {
            public void onNext(GameDataModel gameDataModel) {
                if (!GamesListActivity.this.isFinishing()) {
                    GamesListActivity.this.mAdapter.addItem(gameDataModel);
                }
            }

            public void onError(Throwable th) {
                GamesListActivity.this.setRefreshing(false);
                if (GamesListActivity.this.isListEmpty()) {
                    GamesListActivity.this.emptyTextView.setVisibility(0);
                }
            }

            public void onComplete() {
                GamesListActivity.this.setRefreshing(false);
                if (GamesListActivity.this.isListEmpty()) {
                    GamesListActivity.this.emptyTextView.setVisibility(0);
                }
            }
        });
    }

    private void getGameLocalData() {
        Single.create(new SingleOnSubscribe<List<GameDataModel>>() {
            public void subscribe(SingleEmitter<List<GameDataModel>> singleEmitter) throws Exception {
                List list = (List) new Gson().fromJson(PreferenceHelper.getInstance().getGameListData(), new TypeToken<List<GameDataModel>>() {
                }.getType());
                Intent intent = new Intent("android.intent.action.MAIN", null);
                intent.addCategory("android.intent.category.LAUNCHER");
                List<ResolveInfo> queryIntentActivities = GamesListActivity.this.getPackageManager().queryIntentActivities(intent, 0);
                ArrayList arrayList = new ArrayList();
                for (ResolveInfo resolveInfo : queryIntentActivities) {
                    GamesListActivity.this.getPackageManager().getApplicationLabel(resolveInfo.activityInfo.applicationInfo);
                    String str = (String) (resolveInfo.activityInfo.applicationInfo != null ? GamesListActivity.this.getPackageManager().getApplicationLabel(resolveInfo.activityInfo.applicationInfo) : "");
                    if (!TextUtils.isEmpty(str)) {
                        GameDataModel gameDataModel = new GameDataModel();
                        gameDataModel.setGameName(str);
                        gameDataModel.setInstalled(true);
                        gameDataModel.setPackageName(resolveInfo.activityInfo.packageName);
                        gameDataModel.setGameImageDrawable(GamesListActivity.this.getPackageManager().getApplicationIcon(resolveInfo.activityInfo.applicationInfo));
                        gameDataModel.setLocalApplication(true);
                        arrayList.add(gameDataModel);
                    }
                }
                for (int i = 0; i < list.size(); i++) {
                    GameDataModel gameDataModel2 = (GameDataModel) list.get(i);
                    gameDataModel2.setLocalApplication(false);
                    Iterator it = arrayList.iterator();
                    while (true) {
                        if (!it.hasNext()) {
                            break;
                        }
                        GameDataModel gameDataModel3 = (GameDataModel) it.next();
                        if (gameDataModel2.getPackageName().trim().equals(gameDataModel3.getPackageName().trim())) {
                            gameDataModel2.setInstalled(true);
                            arrayList.remove(gameDataModel3);
                            break;
                        }
                    }
                }
                ArrayList arrayList2 = new ArrayList();
                arrayList2.addAll(arrayList);
                arrayList2.addAll(list);
                Collections.sort(arrayList2, new Comparator<GameDataModel>() {
                    public int compare(GameDataModel gameDataModel, GameDataModel gameDataModel2) {
                        boolean z = false;
                        if ((gameDataModel.isInstalled() && gameDataModel.isLocalApplication()) && (gameDataModel2.isInstalled() && gameDataModel2.isLocalApplication())) {
                            return 0;
                        }
                        if ((gameDataModel.isInstalled() && gameDataModel.isLocalApplication()) && (gameDataModel2.isInstalled() && !gameDataModel2.isLocalApplication())) {
                            return 1;
                        }
                        boolean z2 = gameDataModel.isInstalled() && !gameDataModel.isLocalApplication();
                        if (gameDataModel2.isInstalled() && gameDataModel2.isLocalApplication()) {
                            z = true;
                        }
                        if ((!z2 || !z) && !gameDataModel.isInstalled() && gameDataModel2.isInstalled()) {
                            return 1;
                        }
                        return -1;
                    }
                });
                singleEmitter.onSuccess(arrayList2);
            }
        }).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread()).subscribe((SingleObserver<? super T>) new DisposableSingleObserver<List<GameDataModel>>() {
            public void onSuccess(List<GameDataModel> list) {
                GamesListActivity.this.setRefreshing(false);
                if (!GamesListActivity.this.isFinishing()) {
                    GamesListActivity.this.mAdapter.addList(list);
                }
            }

            public void onError(Throwable th) {
                GamesListActivity.this.setRefreshing(false);
                if (GamesListActivity.this.isListEmpty()) {
                    GamesListActivity.this.emptyTextView.setVisibility(0);
                }
            }
        });
    }

    /* access modifiers changed from: private */
    public boolean isListEmpty() {
        try {
            if (this.mAdapter != null && this.mAdapter.getItemCount() > 0) {
                return false;
            }
        } catch (Exception unused) {
        }
        return true;
    }

    /* access modifiers changed from: private */
    public void setRefreshing(final boolean z) {
        new Handler().postDelayed(new Runnable() {
            public void run() {
                if (GamesListActivity.this.swipeRefreshLayout != null) {
                    GamesListActivity.this.swipeRefreshLayout.setRefreshing(z);
                }
            }
        }, 30);
    }

    public void onRefresh() {
        getGameList();
    }
}
