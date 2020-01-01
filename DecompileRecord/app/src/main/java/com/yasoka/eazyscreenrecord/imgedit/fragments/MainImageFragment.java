package com.yasoka.eazyscreenrecord.imgedit.fragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.support.p000v4.app.Fragment;
import android.support.p000v4.app.FragmentManager;
import android.support.p000v4.content.ContextCompat;
import android.support.p003v7.widget.LinearLayoutManager;
import android.support.p003v7.widget.RecyclerView;
import android.support.p003v7.widget.RecyclerView.Adapter;
import android.support.p003v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.ezscreenrecorder.C0793R;
import com.ezscreenrecorder.R;
import com.ezscreenrecorder.fragments.ShowConfirmationForRewardAdDialog;
import com.ezscreenrecorder.fragments.ShowConfirmationForRewardAdDialog.OnConfirmationResponseCallback;
import com.ezscreenrecorder.fragments.ShowRewardAdLoadingDialog;
import com.ezscreenrecorder.fragments.ShowRewardAdLoadingDialog.OnAdRewardedCallback;
import com.ezscreenrecorder.imgedit.ImageEditActivity;
import com.ezscreenrecorder.utils.TouchImageView;
import com.ezscreenrecorder.utils.TouchImageView.OnTouchImageViewListener;
import java.util.ArrayList;
import java.util.List;

public class MainImageFragment extends Fragment implements OnConfirmationResponseCallback {
    public static final String CROP_IMAGE_EXTRA_SOURCE = "ImageExtra";
    private static final int REQUEST_CODE_DRAW_OPT_REWARD_AD = 1412;
    private static final int REQUEST_CODE_DRAW_OPT_REWARD_AD_CONFERMATION = 1411;
    /* access modifiers changed from: private */
    public static boolean isRewardedAdAlreadyShowed = false;
    /* access modifiers changed from: private */
    public TouchImageView imgShow;

    class FilterListAdapter extends Adapter<FilterListViewHolder> {
        /* access modifiers changed from: private */
        public Context context;
        /* access modifiers changed from: private */
        public List<Filters> filtersList = new ArrayList();
        MainImageFragment fragment;
        /* access modifiers changed from: private */
        public FragmentManager fragmentManager;

        class FilterListViewHolder extends ViewHolder {
            ImageView mFilterImage;
            TextView mFilterName;

            FilterListViewHolder(View view) {
                super(view);
                this.mFilterName = (TextView) view.findViewById(C0793R.C0795id.txt_filter_name);
                this.mFilterImage = (ImageView) view.findViewById(C0793R.C0795id.img_filter);
                view.setOnClickListener(new OnClickListener(FilterListAdapter.this) {
                    public void onClick(View view) {
                        if (MainImageFragment.this.isAdded()) {
                            Filters filters = (Filters) FilterListAdapter.this.filtersList.get(FilterListViewHolder.this.getAdapterPosition());
                            Bundle bundle = new Bundle();
                            bundle.putString(MainImageFragment.CROP_IMAGE_EXTRA_SOURCE, ((ImageEditActivity) FilterListAdapter.this.context).getCurrentImage());
                            switch (filters.filterImage) {
                                case C0793R.C0794drawable.ic_crop /*2131230917*/:
                                    bundle.putBoolean("isCrop", true);
                                    ((ImageEditActivity) FilterListAdapter.this.context).replaceFragment(CropFragment.class, null, bundle);
                                    break;
                                case C0793R.C0794drawable.ic_draw /*2131230931*/:
                                    if (MainImageFragment.isRewardedAdAlreadyShowed) {
                                        ((ImageEditActivity) FilterListAdapter.this.context).replaceFragment(DrawFragment.class, null, bundle);
                                        break;
                                    } else {
                                        ShowConfirmationForRewardAdDialog showConfirmationForRewardAdDialog = new ShowConfirmationForRewardAdDialog();
                                        showConfirmationForRewardAdDialog.setConfirmationData(ShowConfirmationForRewardAdDialog.EXTRA_CONFIRMATION_TYPE_DRAW_FEATURE, new OnConfirmationResponseCallback() {
                                            public void onUserResponse(boolean z) {
                                                if (z) {
                                                    ShowRewardAdLoadingDialog showRewardAdLoadingDialog = new ShowRewardAdLoadingDialog();
                                                    showRewardAdLoadingDialog.setOnAdRewardListener(new OnAdRewardedCallback() {
                                                        public void onAdRewarded(boolean z) {
                                                            if (z) {
                                                                MainImageFragment.isRewardedAdAlreadyShowed = true;
                                                                Toast.makeText(MainImageFragment.this.getContext(), C0793R.string.reward_ad_success_message_to_user, 1).show();
                                                                new Handler().postDelayed(new Runnable() {
                                                                    public void run() {
                                                                        Bundle bundle = new Bundle();
                                                                        bundle.putString(MainImageFragment.CROP_IMAGE_EXTRA_SOURCE, ((ImageEditActivity) MainImageFragment.this.getActivity()).getCurrentImage());
                                                                        ((ImageEditActivity) MainImageFragment.this.getActivity()).replaceFragment(DrawFragment.class, null, bundle);
                                                                    }
                                                                }, 200);
                                                            }
                                                        }
                                                    });
                                                    showRewardAdLoadingDialog.show(MainImageFragment.this.getActivity().getSupportFragmentManager(), "DRAW_LOAD_AD");
                                                }
                                            }
                                        });
                                        showConfirmationForRewardAdDialog.show(FilterListAdapter.this.fragmentManager, "DRAW_CONF_DIALOG");
                                        break;
                                    }
                                case R.drawable.ic_filters /*2131230948*/:
                                    ((ImageEditActivity) FilterListAdapter.this.context).replaceFragment(FiltersFragment.class, null, bundle);
                                    break;
                                case R.drawable.ic_flip /*2131230949*/:
                                    bundle.putBoolean("isFlip", true);
                                    ((ImageEditActivity) FilterListAdapter.this.context).replaceFragment(CropFragment.class, null, bundle);
                                    break;
                                case C0793R.C0794drawable.ic_rotate /*2131231025*/:
                                    bundle.putBoolean("isRotate", true);
                                    ((ImageEditActivity) FilterListAdapter.this.context).replaceFragment(CropFragment.class, null, bundle);
                                    break;
                                case C0793R.C0794drawable.ic_text /*2131231056*/:
                                    ((ImageEditActivity) FilterListAdapter.this.context).replaceFragment(TextFragment.class, null, bundle);
                                    break;
                            }
                        }
                    }
                });
            }
        }


        FilterListAdapter(Context context2) {
            this.context = context2;
        }

        FilterListAdapter(Context context2, MainImageFragment mainImageFragment) {
            this.context = context2;
            this.fragment = mainImageFragment;
            this.fragmentManager = mainImageFragment.getActivity().getSupportFragmentManager();
        }

        /* access modifiers changed from: 0000 */
        public void addFilter(Filters filters) {
            this.filtersList.add(filters);
            notifyDataSetChanged();
        }

        public FilterListViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            return new FilterListViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.filter_item, viewGroup, false));
        }

        public void onBindViewHolder(FilterListViewHolder filterListViewHolder, int i) {
            Filters filters = (Filters) this.filtersList.get(i);
            filterListViewHolder.mFilterName.setText(filters.filterName);
            Glide.with(this.context).load(Integer.valueOf(filters.filterImage)).into(filterListViewHolder.mFilterImage);
        }

        public int getItemCount() {
            return this.filtersList.size();
        }
    }

    class Filters {
        @DrawableRes
        int filterImage;
        String filterName;

        Filters(String str, int i) {
            this.filterName = str;
            this.filterImage = i;
        }
    }

    public void onCreate(@Nullable Bundle bundle) {
        super.onCreate(bundle);
        setHasOptionsMenu(true);
    }

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        return layoutInflater.inflate(R.layout.fragment_main_image, viewGroup, false);
    }

    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {
        super.onCreateOptionsMenu(menu, menuInflater);
        menuInflater.inflate(C0793R.C0797menu.menu_main, menu);
    }

    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        MenuItem findItem = menu.findItem(C0793R.C0795id.action_undo);
        if (findItem != null) {
            Drawable icon = findItem.getIcon();
            if (((ImageEditActivity) getActivity()).imageHistoryList.size() == 1) {
                findItem.setEnabled(false);
                icon.setColorFilter(new PorterDuffColorFilter(ContextCompat.getColor(getContext(), C0793R.color.background_progress_color), Mode.MULTIPLY));
            } else {
                icon.setColorFilter(new PorterDuffColorFilter(ContextCompat.getColor(getContext(), C0793R.color.colorWhite), Mode.MULTIPLY));
                findItem.setEnabled(true);
            }
            findItem.setIcon(icon);
        }
        MenuItem findItem2 = menu.findItem(C0793R.C0795id.action_save);
        if (findItem2 != null) {
            Drawable icon2 = findItem2.getIcon();
            if (((ImageEditActivity) getActivity()).imageHistoryList.size() == 1) {
                findItem2.setEnabled(false);
                icon2.setColorFilter(new PorterDuffColorFilter(ContextCompat.getColor(getContext(), C0793R.color.background_progress_color), Mode.MULTIPLY));
            } else {
                findItem2.setEnabled(true);
                icon2.setColorFilter(new PorterDuffColorFilter(ContextCompat.getColor(getContext(), C0793R.color.colorWhite), Mode.MULTIPLY));
            }
            findItem2.setIcon(icon2);
        }
    }

    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == C0793R.C0795id.action_undo) {
            if (((ImageEditActivity) getActivity()).imageHistoryList.size() > 1) {
                ((ImageEditActivity) getActivity()).imageHistoryList.remove(((ImageEditActivity) getActivity()).imageHistoryList.size() - 1);
                refreshImage();
            } else {
                Toast.makeText(getContext(), "No More Undo to Process", 1).show();
            }
        } else if (menuItem.getItemId() == C0793R.C0795id.action_save) {
            ((ImageEditActivity) getActivity()).save();
        }
        return super.onOptionsItemSelected(menuItem);
    }

    public void onViewCreated(View view, @Nullable Bundle bundle) {
        super.onViewCreated(view, bundle);
        this.imgShow = (TouchImageView) view.findViewById(C0793R.C0795id.img_show);
        this.imgShow.setOnTouchImageViewListener(new OnTouchImageViewListener() {
            public void onMove() {
            }
        });
        RecyclerView recyclerView = (RecyclerView) view.findViewById(C0793R.C0795id.rcyl_image_filters);
        FilterListAdapter filterListAdapter = new FilterListAdapter(getContext(), this);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), 0, false));
        recyclerView.setAdapter(filterListAdapter);
        filterListAdapter.addFilter(new Filters(getString(C0793R.string.crop), R.drawable.ic_crop));
        filterListAdapter.addFilter(new Filters(getString(C0793R.string.draw), R.drawable.ic_draw));
        filterListAdapter.addFilter(new Filters(getString(C0793R.string.edit_text), C0793R.C0794drawable.ic_text));
        filterListAdapter.addFilter(new Filters(getString(C0793R.string.flip), C0793R.C0794drawable.ic_flip));
        filterListAdapter.addFilter(new Filters(getString(C0793R.string.rotate), R.drawable.ic_rotate));
        filterListAdapter.addFilter(new Filters("Filters", C0793R.C0794drawable.ic_filters));
    }

    public void onActivityCreated(@Nullable Bundle bundle) {
        super.onActivityCreated(bundle);
        ((ImageEditActivity) getActivity()).setMyTitle(getString(C0793R.string.img_editor));
        ((ImageEditActivity) getActivity()).setTitleStartAligned(true);
    }

    public void onStart() {
        super.onStart();
        refreshImage();
    }

    public void onResume() {
        super.onResume();
    }

    public void refreshImage() {
        final String currentImage = ((ImageEditActivity) getActivity()).getCurrentImage();
        this.imgShow.postDelayed(new Runnable() {
            public void run() {
                try {
                    Glide.with(MainImageFragment.this.getContext()).load(currentImage).asBitmap().into(new SimpleTarget<Bitmap>() {
                        public void onResourceReady(Bitmap bitmap, GlideAnimation<? super Bitmap> glideAnimation) {
                            MainImageFragment.this.imgShow.setImageBitmap(bitmap);
                            MainImageFragment.this.imgShow.post(new Runnable() {
                                public void run() {
                                    MainImageFragment.this.imgShow.setZoom(1.0f);
                                }
                            });
                        }
                    });
                    MainImageFragment.this.getActivity().invalidateOptionsMenu();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, 200);
    }

    public void onDestroy() {
        super.onDestroy();
        isRewardedAdAlreadyShowed = false;
    }

    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        if (isAdded()) {
            getActivity();
            if (i2 == -1) {
                if (i == REQUEST_CODE_DRAW_OPT_REWARD_AD_CONFERMATION) {
                    ShowRewardAdLoadingDialog showRewardAdLoadingDialog = new ShowRewardAdLoadingDialog();
                    showRewardAdLoadingDialog.setTargetFragment(this, REQUEST_CODE_DRAW_OPT_REWARD_AD);
                    showRewardAdLoadingDialog.show(getActivity().getSupportFragmentManager(), "DRAW_LOAD_AD");
                } else if (i == REQUEST_CODE_DRAW_OPT_REWARD_AD) {
                    isRewardedAdAlreadyShowed = true;
                    Toast.makeText(getContext(), C0793R.string.reward_ad_success_message_to_user, 1).show();
                    new Handler().postDelayed(new Runnable() {
                        public void run() {
                            Bundle bundle = new Bundle();
                            bundle.putString(MainImageFragment.CROP_IMAGE_EXTRA_SOURCE, ((ImageEditActivity) MainImageFragment.this.getActivity()).getCurrentImage());
                            ((ImageEditActivity) MainImageFragment.this.getActivity()).replaceFragment(DrawFragment.class, null, bundle);
                        }
                    }, 200);
                }
            } else if (i == REQUEST_CODE_DRAW_OPT_REWARD_AD) {
                Toast.makeText(getContext(), C0793R.string.reward_ad_error_message_to_user, 1).show();
            }
        }
    }

    public void onUserResponse(boolean z) {
        ShowRewardAdLoadingDialog showRewardAdLoadingDialog = new ShowRewardAdLoadingDialog();
        showRewardAdLoadingDialog.setTargetFragment(this, REQUEST_CODE_DRAW_OPT_REWARD_AD);
        showRewardAdLoadingDialog.show(getActivity().getSupportFragmentManager(), "DRAW_LOAD_AD");
    }
}
