package com.yasoka.eazyscreenrecord.activities;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.p000v4.widget.SwipeRefreshLayout;
import android.support.p000v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.support.p003v7.widget.AppCompatTextView;
import android.support.p003v7.widget.LinearLayoutManager;
import android.support.p003v7.widget.RecyclerView;
import android.view.MenuItem;
import com.ezscreenrecorder.C0793R;
import com.ezscreenrecorder.RecorderApplication;
import com.ezscreenrecorder.adapter.SocialFeedAdapter;
import com.ezscreenrecorder.server.ServerAPI;
import com.ezscreenrecorder.server.model.SocialFeedModels.SocialFeedModel;
import com.ezscreenrecorder.server.model.SocialFeedModels.SocialFeedsMainModel;
import com.ezscreenrecorder.utils.Logger;
import org.reactivestreams.Publisher;
import p009io.reactivex.Flowable;
import p009io.reactivex.FlowableSubscriber;
import p009io.reactivex.functions.Function;
import p009io.reactivex.subscribers.DisposableSubscriber;

public class SocialFeedsActivity extends BaseToolbarActivity implements OnRefreshListener {
    private AppCompatTextView emptyTextView;
    /* access modifiers changed from: private */
    public SocialFeedAdapter mAdapter;
    private RecyclerView recyclerView;
    /* access modifiers changed from: private */
    public SwipeRefreshLayout swipeRefreshLayout;

    /* access modifiers changed from: protected */
    public void onCreate(@Nullable Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) C0793R.layout.activity_social_feeds);
        setupToolbar();
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle((CharSequence) "Social");
        }
        this.recyclerView = (RecyclerView) findViewById(C0793R.C0795id.id_recycler_view);
        this.swipeRefreshLayout = (SwipeRefreshLayout) findViewById(C0793R.C0795id.id_swipe_refresh);
        this.emptyTextView = (AppCompatTextView) findViewById(C0793R.C0795id.id_empty_error_text_view);
        this.swipeRefreshLayout.setOnRefreshListener(this);
        new Handler().postDelayed(new Runnable() {
            public void run() {
                SocialFeedsActivity.this.getSocialFeedList();
            }
        }, 100);
    }

    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == 16908332 && !isFinishing()) {
            finish();
        }
        return super.onOptionsItemSelected(menuItem);
    }

    /* access modifiers changed from: private */
    public void setRefreshing(final boolean z) {
        new Handler().postDelayed(new Runnable() {
            public void run() {
                if (SocialFeedsActivity.this.swipeRefreshLayout != null) {
                    SocialFeedsActivity.this.swipeRefreshLayout.setRefreshing(z);
                }
            }
        }, 30);
    }

    /* access modifiers changed from: private */
    public void getSocialFeedList() {
        if (!RecorderApplication.getInstance().isNetworkAvailable()) {
            SocialFeedAdapter socialFeedAdapter = this.mAdapter;
            if (socialFeedAdapter == null || socialFeedAdapter.getItemCount() <= 0) {
                this.emptyTextView.setVisibility(0);
                setRefreshing(false);
                return;
            }
            return;
        }
        this.emptyTextView.setVisibility(8);
        if (this.recyclerView.getLayoutManager() == null) {
            this.recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), 1, false));
        }
        SocialFeedAdapter socialFeedAdapter2 = this.mAdapter;
        if (socialFeedAdapter2 == null) {
            this.mAdapter = new SocialFeedAdapter();
            this.recyclerView.setAdapter(this.mAdapter);
        } else {
            socialFeedAdapter2.clearList();
        }
        setRefreshing(true);
        ServerAPI.getInstance().getSocialFeeds().flatMapPublisher(new Function<SocialFeedsMainModel, Publisher<SocialFeedModel>>() {
            public Publisher<SocialFeedModel> apply(SocialFeedsMainModel socialFeedsMainModel) throws Exception {
                return Flowable.fromIterable(socialFeedsMainModel.getData().getSocialFeeds());
            }
        }).subscribe((FlowableSubscriber<? super T>) new DisposableSubscriber<SocialFeedModel>() {
            public void onNext(SocialFeedModel socialFeedModel) {
                Logger.getInstance().error(socialFeedModel.getSource());
                SocialFeedsActivity.this.mAdapter.addItem(socialFeedModel);
            }

            public void onError(Throwable th) {
                SocialFeedsActivity.this.setRefreshing(false);
            }

            public void onComplete() {
                SocialFeedsActivity.this.setRefreshing(false);
                if (SocialFeedsActivity.this.mAdapter == null || SocialFeedsActivity.this.mAdapter.getItemCount() > 0) {
                }
            }
        });
    }

    public void onRefresh() {
        getSocialFeedList();
    }
}
