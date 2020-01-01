package com.yasoka.eazyscreenrecord.activities;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetBehavior.BottomSheetCallback;
import android.support.p000v4.content.ContextCompat;
import android.support.p000v4.widget.SwipeRefreshLayout;
import android.support.p000v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.support.p003v7.app.AppCompatActivity;
import android.support.p003v7.widget.AppCompatButton;
import android.support.p003v7.widget.AppCompatImageView;
import android.support.p003v7.widget.AppCompatTextView;
import android.support.p003v7.widget.LinearLayoutManager;
import android.support.p003v7.widget.RecyclerView;
import android.support.p003v7.widget.RecyclerView.OnScrollListener;
import android.support.p003v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Filter;
import android.widget.Filter.FilterResults;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.ezscreenrecorder.C0793R;
import com.ezscreenrecorder.RecorderApplication;
import com.ezscreenrecorder.adapter.RecordingsRemoteSubListAdapter;
import com.ezscreenrecorder.model.CountryFilterModel;
import com.ezscreenrecorder.model.VideosRemoteDataModel;
import com.ezscreenrecorder.server.ServerAPI;
import com.ezscreenrecorder.server.model.MoreVideosModels.MoreVideosDataModel;
import com.ezscreenrecorder.server.model.VideoMainScreenModels.VideosData;
import java.util.ArrayList;
import java.util.List;
import org.reactivestreams.Publisher;
import p009io.reactivex.Flowable;
import p009io.reactivex.FlowableSubscriber;
import p009io.reactivex.Single;
import p009io.reactivex.SingleObserver;
import p009io.reactivex.SingleOnSubscribe;
import p009io.reactivex.android.schedulers.AndroidSchedulers;
import p009io.reactivex.disposables.Disposable;
import p009io.reactivex.functions.Function;
import p009io.reactivex.schedulers.Schedulers;
import p009io.reactivex.subscribers.DisposableSubscriber;

public class MoreVideosActivity extends AppCompatActivity implements OnRefreshListener, OnClickListener {
    private static final String SORT_TYPE_EXTRA_ALPHABETIC = "name";
    private static final String SORT_TYPE_EXTRA_DATE = "date";
    private static final String SORT_TYPE_EXTRA_VIEWS = "views";
    /* access modifiers changed from: private */
    public AutoCompleteTextView autoCompleteTextView;
    private BottomSheetBehavior bottomSheetBehavior;
    private LinearLayout bottomSheetLayout;
    /* access modifiers changed from: private */
    public String countryFilterName = "";
    /* access modifiers changed from: private */
    public int currentPage = 1;
    private String currentSortType;
    /* access modifiers changed from: private */
    public LinearLayout dimBg;
    /* access modifiers changed from: private */
    public AppCompatButton filterApplyButton;
    /* access modifiers changed from: private */
    public RecordingsRemoteSubListAdapter mAdapter;
    /* access modifiers changed from: private */
    public LinearLayoutManager mLayoutManager;
    /* access modifiers changed from: private */
    public AppCompatTextView noInternetError;
    private RecyclerView recyclerView;
    private LinearLayout sortLayout;
    /* access modifiers changed from: private */
    public SwipeRefreshLayout swipeRefreshLayout;
    private Toolbar toolbar;
    /* access modifiers changed from: private */
    public int totalPages;

    private class AutoCompleteTVAdapter extends ArrayAdapter<CountryFilterModel> {
        private Context context;
        private List<CountryFilterModel> items;
        Filter nameFilter = new Filter() {
            public CharSequence convertResultToString(Object obj) {
                return ((CountryFilterModel) obj).getCountryName();
            }

            /* access modifiers changed from: protected */
            public FilterResults performFiltering(CharSequence charSequence) {
                if (charSequence == null) {
                    return new FilterResults();
                }
                AutoCompleteTVAdapter.this.suggestions.clear();
                for (CountryFilterModel countryFilterModel : AutoCompleteTVAdapter.this.tempItems) {
                    if (countryFilterModel.getCountryName().toLowerCase().contains(charSequence.toString().toLowerCase())) {
                        AutoCompleteTVAdapter.this.suggestions.add(countryFilterModel);
                    }
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = AutoCompleteTVAdapter.this.suggestions;
                filterResults.count = AutoCompleteTVAdapter.this.suggestions.size();
                return filterResults;
            }

            /* access modifiers changed from: protected */
            public void publishResults(CharSequence charSequence, FilterResults filterResults) {
                ArrayList<CountryFilterModel> arrayList = (ArrayList) filterResults.values;
                if (filterResults != null && filterResults.count > 0) {
                    AutoCompleteTVAdapter.this.clear();
                    for (CountryFilterModel add : arrayList) {
                        AutoCompleteTVAdapter.this.add(add);
                        AutoCompleteTVAdapter.this.notifyDataSetChanged();
                    }
                }
            }
        };
        private int resource;
        /* access modifiers changed from: private */
        public List<CountryFilterModel> suggestions;
        /* access modifiers changed from: private */
        public List<CountryFilterModel> tempItems;
        private int textViewResourceId;

        public AutoCompleteTVAdapter(@NonNull Context context2, int i, int i2, @NonNull List<CountryFilterModel> list) {
            super(context2, i, i2, list);
            this.context = context2;
            this.resource = i;
            this.textViewResourceId = i2;
            this.items = list;
            this.tempItems = new ArrayList();
            this.tempItems.addAll(this.items);
            this.suggestions = new ArrayList();
        }

        public View getView(int i, View view, ViewGroup viewGroup) {
            if (view == null) {
                view = ((LayoutInflater) this.context.getSystemService("layout_inflater")).inflate(this.resource, viewGroup, false);
            }
            CountryFilterModel countryFilterModel = (CountryFilterModel) this.items.get(i);
            if (countryFilterModel != null) {
                TextView textView = (TextView) view.findViewById(this.textViewResourceId);
                if (textView != null) {
                    textView.setText(countryFilterModel.getCountryName());
                }
            }
            return view;
        }

        public Filter getFilter() {
            return this.nameFilter;
        }
    }

    private class RecyclerScrollHandler extends OnScrollListener {
        int pastVisibleItems;
        int totalItemCount;
        int visibleItemCount;

        private RecyclerScrollHandler() {
        }

        public void onScrolled(RecyclerView recyclerView, int i, int i2) {
            super.onScrolled(recyclerView, i, i2);
            if (MoreVideosActivity.this.mLayoutManager != null && i2 > 0) {
                this.visibleItemCount = MoreVideosActivity.this.mLayoutManager.getChildCount();
                this.totalItemCount = MoreVideosActivity.this.mLayoutManager.getItemCount();
                this.pastVisibleItems = MoreVideosActivity.this.mLayoutManager.findFirstVisibleItemPosition();
                if (!MoreVideosActivity.this.swipeRefreshLayout.isRefreshing() && this.visibleItemCount + this.pastVisibleItems >= this.totalItemCount - 10 && MoreVideosActivity.this.currentPage < MoreVideosActivity.this.totalPages) {
                    if (!RecorderApplication.getInstance().isNetworkAvailable()) {
                        Toast.makeText(MoreVideosActivity.this.getApplicationContext(), C0793R.string.no_internet_connection, 1).show();
                        MoreVideosActivity.this.setRefreshing(false);
                        return;
                    }
                    MoreVideosActivity.this.currentPage = MoreVideosActivity.this.currentPage + 1;
                    MoreVideosActivity.this.getMoreVideos(true);
                }
            }
        }
    }

    /* access modifiers changed from: protected */
    public void onCreate(@Nullable Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) C0793R.layout.activity_more_videos);
        this.toolbar = (Toolbar) findViewById(C0793R.C0795id.toolbar);
        setSupportActionBar(this.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle((int) C0793R.string.id_all_recordings_txt);
        this.dimBg = (LinearLayout) findViewById(C0793R.C0795id.id_more_videos_background_dim_view);
        this.bottomSheetLayout = (LinearLayout) findViewById(C0793R.C0795id.id_more_videos_bottom_sheet_container);
        this.bottomSheetBehavior = BottomSheetBehavior.from(this.bottomSheetLayout);
        this.swipeRefreshLayout = (SwipeRefreshLayout) findViewById(C0793R.C0795id.id_swipe_refresh);
        this.autoCompleteTextView = (AutoCompleteTextView) findViewById(C0793R.C0795id.id_more_videos_filter_auto_complete_tv);
        this.recyclerView = (RecyclerView) findViewById(C0793R.C0795id.id_recycler_view);
        this.noInternetError = (AppCompatTextView) findViewById(C0793R.C0795id.id_empty_error_text_view);
        this.filterApplyButton = (AppCompatButton) findViewById(C0793R.C0795id.id_more_videos_filter_apply_button);
        this.filterApplyButton.setVisibility(8);
        this.sortLayout = (LinearLayout) findViewById(C0793R.C0795id.id_more_videos_sort_grid_layout);
        this.filterApplyButton.setOnClickListener(this);
        this.mLayoutManager = new LinearLayoutManager(this, 1, false);
        this.recyclerView.addOnScrollListener(new RecyclerScrollHandler());
        this.bottomSheetBehavior.setBottomSheetCallback(new BottomSheetCallback() {
            public void onSlide(@NonNull View view, float f) {
            }

            public void onStateChanged(@NonNull View view, int i) {
                if (i == 5) {
                    MoreVideosActivity.this.dimBg.setVisibility(8);
                } else {
                    MoreVideosActivity.this.dimBg.setVisibility(0);
                }
            }
        });
        this.bottomSheetBehavior.setState(5);
        this.swipeRefreshLayout.setOnRefreshListener(this);
        this.swipeRefreshLayout.setColorSchemeResources(C0793R.color.colorPrimary);
        getCountryData();
        for (int i = 0; i < this.sortLayout.getChildCount(); i++) {
            View childAt = this.sortLayout.getChildAt(i);
            if (childAt != null) {
                childAt.setOnClickListener(this);
                if (childAt.getId() == C0793R.C0795id.id_more_videos_sort_type_date) {
                    childAt.performClick();
                }
            }
        }
    }

    private void getCountryData() {
        Single.create(new SingleOnSubscribe<List<CountryFilterModel>>() {
            /* JADX WARNING: Removed duplicated region for block: B:19:0x0057  */
            /* JADX WARNING: Removed duplicated region for block: B:23:? A[RETURN, SYNTHETIC] */
            /* Code decompiled incorrectly, please refer to instructions dump. */
            public void subscribe(p009io.reactivex.SingleEmitter<List<com.ezscreenrecorder.model.CountryFilterModel>> r7) throws Exception {
                /*
                    r6 = this;
                    r0 = 0
                    java.util.ArrayList r1 = new java.util.ArrayList     // Catch:{ Exception -> 0x0048, all -> 0x0045 }
                    r1.<init>()     // Catch:{ Exception -> 0x0048, all -> 0x0045 }
                    com.ezscreenrecorder.activities.MoreVideosActivity r2 = com.ezscreenrecorder.activities.MoreVideosActivity.this     // Catch:{ Exception -> 0x0048, all -> 0x0045 }
                    android.content.res.AssetManager r2 = r2.getAssets()     // Catch:{ Exception -> 0x0048, all -> 0x0045 }
                    java.lang.String r3 = "country_codes.csv"
                    java.io.InputStream r2 = r2.open(r3)     // Catch:{ Exception -> 0x0048, all -> 0x0045 }
                    java.io.BufferedReader r3 = new java.io.BufferedReader     // Catch:{ Exception -> 0x0048, all -> 0x0045 }
                    java.io.InputStreamReader r4 = new java.io.InputStreamReader     // Catch:{ Exception -> 0x0048, all -> 0x0045 }
                    r4.<init>(r2)     // Catch:{ Exception -> 0x0048, all -> 0x0045 }
                    r3.<init>(r4)     // Catch:{ Exception -> 0x0048, all -> 0x0045 }
                L_0x001c:
                    java.lang.String r0 = r3.readLine()     // Catch:{ Exception -> 0x0043 }
                    if (r0 == 0) goto L_0x003f
                    java.lang.String r2 = ","
                    java.lang.String[] r0 = r0.split(r2)     // Catch:{ Exception -> 0x0043 }
                    com.ezscreenrecorder.model.CountryFilterModel r2 = new com.ezscreenrecorder.model.CountryFilterModel     // Catch:{ Exception -> 0x0043 }
                    r4 = 1
                    r4 = r0[r4]     // Catch:{ Exception -> 0x0043 }
                    java.lang.String r4 = r4.trim()     // Catch:{ Exception -> 0x0043 }
                    r5 = 0
                    r0 = r0[r5]     // Catch:{ Exception -> 0x0043 }
                    java.lang.String r0 = r0.trim()     // Catch:{ Exception -> 0x0043 }
                    r2.<init>(r4, r0)     // Catch:{ Exception -> 0x0043 }
                    r1.add(r2)     // Catch:{ Exception -> 0x0043 }
                    goto L_0x001c
                L_0x003f:
                    r7.onSuccess(r1)     // Catch:{ Exception -> 0x0043 }
                    goto L_0x0050
                L_0x0043:
                    r0 = move-exception
                    goto L_0x004b
                L_0x0045:
                    r7 = move-exception
                    r3 = r0
                    goto L_0x0055
                L_0x0048:
                    r1 = move-exception
                    r3 = r0
                    r0 = r1
                L_0x004b:
                    r7.onError(r0)     // Catch:{ all -> 0x0054 }
                    if (r3 == 0) goto L_0x0053
                L_0x0050:
                    r3.close()
                L_0x0053:
                    return
                L_0x0054:
                    r7 = move-exception
                L_0x0055:
                    if (r3 == 0) goto L_0x005a
                    r3.close()
                L_0x005a:
                    throw r7
                */
                throw new UnsupportedOperationException("Method not decompiled: com.ezscreenrecorder.activities.MoreVideosActivity.C08513.subscribe(io.reactivex.SingleEmitter):void");
            }
        }).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread()).subscribe((SingleObserver<? super T>) new SingleObserver<List<CountryFilterModel>>() {
            public void onError(Throwable th) {
            }

            public void onSubscribe(Disposable disposable) {
            }

            public void onSuccess(List<CountryFilterModel> list) {
                MoreVideosActivity.this.autoCompleteTextView.setOnItemClickListener(new OnItemClickListener() {
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
                        MoreVideosActivity.this.countryFilterName = ((CountryFilterModel) adapterView.getItemAtPosition(i)).getCountryCode();
                        MoreVideosActivity.this.filterApplyButton.performClick();
                    }
                });
                MoreVideosActivity.this.autoCompleteTextView.addTextChangedListener(new TextWatcher() {
                    public void afterTextChanged(Editable editable) {
                    }

                    public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                    }

                    public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                        MoreVideosActivity.this.countryFilterName = "";
                        try {
                            if (charSequence.length() > 0) {
                                MoreVideosActivity.this.autoCompleteTextView.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, C0793R.C0794drawable.ic_close_circle_grey600_24dp, 0);
                            } else {
                                MoreVideosActivity.this.autoCompleteTextView.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, 0, 0);
                            }
                        } catch (Exception unused) {
                        }
                    }
                });
                MoreVideosActivity.this.autoCompleteTextView.setOnTouchListener(new OnTouchListener() {
                    public boolean onTouch(View view, MotionEvent motionEvent) {
                        try {
                            if (motionEvent.getAction() == 1 && motionEvent.getRawX() >= ((float) (MoreVideosActivity.this.autoCompleteTextView.getRight() - MoreVideosActivity.this.autoCompleteTextView.getCompoundDrawables()[2].getBounds().width()))) {
                                boolean z = !TextUtils.isEmpty(MoreVideosActivity.this.countryFilterName);
                                MoreVideosActivity.this.autoCompleteTextView.setText("");
                                MoreVideosActivity.this.autoCompleteTextView.clearFocus();
                                if (z) {
                                    MoreVideosActivity.this.setBottomSheetState(5);
                                    MoreVideosActivity.this.getMoreVideos(false);
                                }
                            }
                        } catch (Exception unused) {
                        }
                        return false;
                    }
                });
                AutoCompleteTextView access$400 = MoreVideosActivity.this.autoCompleteTextView;
                MoreVideosActivity moreVideosActivity = MoreVideosActivity.this;
                AutoCompleteTVAdapter autoCompleteTVAdapter = new AutoCompleteTVAdapter(moreVideosActivity, C0793R.layout.custom_country_name_view, C0793R.C0795id.id_single_item_text_view, list);
                access$400.setAdapter(autoCompleteTVAdapter);
            }
        });
    }

    public void onBackPressed() {
        BottomSheetBehavior bottomSheetBehavior2 = this.bottomSheetBehavior;
        if (bottomSheetBehavior2 == null || bottomSheetBehavior2.getState() != 3) {
            super.onBackPressed();
        } else {
            setBottomSheetState(5);
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(C0793R.C0797menu.menu_more_videos_activity, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem menuItem) {
        int itemId = menuItem.getItemId();
        if (itemId == 16908332) {
            onBackPressed();
        } else if (itemId == C0793R.C0795id.id_list_filter) {
            if (this.bottomSheetBehavior.getState() == 5) {
                setBottomSheetState(3);
            } else {
                setBottomSheetState(5);
            }
        }
        return super.onOptionsItemSelected(menuItem);
    }

    public void setRefreshing(boolean z) {
        SwipeRefreshLayout swipeRefreshLayout2 = this.swipeRefreshLayout;
        if (swipeRefreshLayout2 != null) {
            swipeRefreshLayout2.setRefreshing(z);
        }
    }

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
    public void getMoreVideos(boolean z) {
        if (!isFinishing()) {
            this.noInternetError.setVisibility(8);
            if (!RecorderApplication.getInstance().isNetworkAvailable()) {
                if (z || !isListEmpty()) {
                    Toast.makeText(getApplicationContext(), C0793R.string.no_internet_connection, 1).show();
                } else {
                    this.noInternetError.setText(C0793R.string.id_no_internet_error_list_message);
                    this.noInternetError.setVisibility(0);
                }
                setRefreshing(false);
                return;
            }
            if (this.mAdapter == null) {
                this.mAdapter = new RecordingsRemoteSubListAdapter(this, VideosRemoteDataModel.KEY_VIDEO_LIST_TYPE_OTHER_VIDEOS);
                this.recyclerView.setLayoutManager(this.mLayoutManager);
                this.recyclerView.setAdapter(this.mAdapter);
            }
            if (!z) {
                this.currentPage = 1;
                this.totalPages = 1;
                if (this.mAdapter.getItemCount() > 0) {
                    this.mAdapter.clearList();
                }
            }
            setRefreshing(true);
            ServerAPI.getInstance().getMoreVideo(String.valueOf(this.currentPage), this.currentSortType, this.countryFilterName).flatMapPublisher(new Function<MoreVideosDataModel, Publisher<VideosData>>() {
                public Publisher<VideosData> apply(MoreVideosDataModel moreVideosDataModel) throws Exception {
                    if (moreVideosDataModel.getData() != null) {
                        MoreVideosActivity.this.totalPages = moreVideosDataModel.getData().getTotalpages().intValue();
                        if (moreVideosDataModel.getData().getVideosData() != null && moreVideosDataModel.getData().getVideosData().size() > 0) {
                            return Flowable.fromIterable(moreVideosDataModel.getData().getVideosData());
                        }
                    }
                    return null;
                }
            }).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread()).subscribe((FlowableSubscriber<? super T>) new DisposableSubscriber<VideosData>() {
                public void onNext(VideosData videosData) {
                    if (MoreVideosActivity.this.mAdapter != null) {
                        MoreVideosActivity.this.mAdapter.addItem(videosData);
                    }
                }

                public void onError(Throwable th) {
                    MoreVideosActivity.this.setRefreshing(false);
                    if (MoreVideosActivity.this.isListEmpty()) {
                        MoreVideosActivity.this.noInternetError.setText(C0793R.string.id_error_api_list_txt);
                        MoreVideosActivity.this.noInternetError.setVisibility(0);
                    }
                }

                public void onComplete() {
                    MoreVideosActivity.this.setRefreshing(false);
                    if (MoreVideosActivity.this.isListEmpty()) {
                        MoreVideosActivity.this.noInternetError.setText(C0793R.string.id_error_api_list_txt);
                        MoreVideosActivity.this.noInternetError.setVisibility(0);
                    }
                }
            });
        }
    }

    public void onRefresh() {
        getMoreVideos(false);
    }

    public static void hideKeyboard(Activity activity) {
        try {
            InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService("input_method");
            View currentFocus = activity.getCurrentFocus();
            if (currentFocus == null) {
                currentFocus = new View(activity);
            }
            inputMethodManager.hideSoftInputFromWindow(currentFocus.getWindowToken(), 0);
        } catch (Exception unused) {
        }
    }

    public void onClick(View view) {
        if (!isFinishing()) {
            switch (view.getId()) {
                case C0793R.C0795id.id_more_videos_sort_type_alphabetic /*2131296624*/:
                    this.currentSortType = "name";
                    resetSortSelection();
                    setSortSelection(view);
                    break;
                case C0793R.C0795id.id_more_videos_sort_type_date /*2131296625*/:
                    this.currentSortType = SORT_TYPE_EXTRA_DATE;
                    resetSortSelection();
                    setSortSelection(view);
                    break;
                case C0793R.C0795id.id_more_videos_sort_type_views /*2131296626*/:
                    this.currentSortType = SORT_TYPE_EXTRA_VIEWS;
                    resetSortSelection();
                    setSortSelection(view);
                    break;
            }
            setBottomSheetState(5);
            getMoreVideos(false);
        }
    }

    public void setSortSelection(View view) {
        try {
            if (view instanceof LinearLayout) {
                LinearLayout linearLayout = (LinearLayout) view;
                if (linearLayout.getChildCount() > 0) {
                    for (int i = 0; i < linearLayout.getChildCount(); i++) {
                        View childAt = linearLayout.getChildAt(i);
                        if (childAt instanceof AppCompatImageView) {
                            ((AppCompatImageView) childAt).setColorFilter(ContextCompat.getColor(getApplicationContext(), C0793R.color.colorPrimary));
                        } else if (childAt instanceof AppCompatTextView) {
                            ((AppCompatTextView) childAt).setTextColor(ContextCompat.getColor(getApplicationContext(), C0793R.color.colorPrimary));
                        }
                    }
                }
            }
        } catch (Exception unused) {
        }
    }

    private void resetSortSelection() {
        int i = 0;
        while (i < this.sortLayout.getChildCount()) {
            try {
                View childAt = this.sortLayout.getChildAt(i);
                if (childAt instanceof LinearLayout) {
                    LinearLayout linearLayout = (LinearLayout) childAt;
                    if (linearLayout.getChildCount() > 0) {
                        for (int i2 = 0; i2 < linearLayout.getChildCount(); i2++) {
                            View childAt2 = linearLayout.getChildAt(i2);
                            if (childAt2 instanceof AppCompatImageView) {
                                ((AppCompatImageView) childAt2).setColorFilter(ContextCompat.getColor(getApplicationContext(), C0793R.color.id_sort_type_default_color));
                            } else if (childAt2 instanceof AppCompatTextView) {
                                ((AppCompatTextView) childAt2).setTextColor(ContextCompat.getColor(getApplicationContext(), C0793R.color.id_sort_type_default_color));
                            }
                        }
                    }
                }
                i++;
            } catch (Exception unused) {
                return;
            }
        }
    }

    /* access modifiers changed from: private */
    public void setBottomSheetState(int i) {
        if (this.bottomSheetBehavior != null) {
            if (i == 5) {
                hideKeyboard(this);
            }
            this.bottomSheetBehavior.setState(i);
        }
    }
}
