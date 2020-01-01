package com.yasoka.eazyscreenrecord.imgedit.fragments;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.p000v4.app.Fragment;
import android.support.p000v4.view.ViewCompat;
import android.support.p003v7.widget.AppCompatSeekBar;
import android.support.p003v7.widget.LinearLayoutManager;
import android.support.p003v7.widget.RecyclerView;
import android.support.p003v7.widget.RecyclerView.Adapter;
import android.support.p003v7.widget.RecyclerView.ViewHolder;
import android.support.p003v7.widget.helper.ItemTouchHelper.Callback;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.ezscreenrecorder.C0793R;
import com.ezscreenrecorder.R;
import com.ezscreenrecorder.imgedit.ImageEditActivity;
import com.ezscreenrecorder.imgedit.ProgressWheel;
import com.ezscreenrecorder.imgedit.colorseekbar.ColorSeekBar;
import com.ezscreenrecorder.imgedit.colorseekbar.ColorSeekBar.OnColorChangeListener;
import com.ezscreenrecorder.imgedit.filters.BlurTransformation;
import com.ezscreenrecorder.imgedit.filters.GrayscaleTransformation;
import com.ezscreenrecorder.imgedit.filters.MaskTransformation;
import com.ezscreenrecorder.imgedit.filters.gpu.BrightnessFilterTransformation;
import com.ezscreenrecorder.imgedit.filters.gpu.ContrastFilterTransformation;
import com.ezscreenrecorder.imgedit.filters.gpu.InvertFilterTransformation;
import com.ezscreenrecorder.imgedit.filters.gpu.KuwaharaFilterTransformation;
import com.ezscreenrecorder.imgedit.filters.gpu.PixelationFilterTransformation;
import com.ezscreenrecorder.imgedit.filters.gpu.SepiaFilterTransformation;
import com.ezscreenrecorder.imgedit.filters.gpu.SketchFilterTransformation;
import com.ezscreenrecorder.imgedit.filters.gpu.ToonFilterTransformation;
import com.ezscreenrecorder.imgedit.filters.gpu.VignetteFilterTransformation;
import com.ezscreenrecorder.imgedit.filters.latest.BludgeFilterTransformation;
import com.ezscreenrecorder.imgedit.filters.latest.EmbossFilterTransformation;
import com.ezscreenrecorder.imgedit.filters.latest.GPUExplosureFilterTransformation;
import com.ezscreenrecorder.imgedit.filters.latest.GPUMonochromeFilterTransformation;
import com.ezscreenrecorder.imgedit.filters.latest.GammaFilterTransformation;
import com.ezscreenrecorder.imgedit.filters.latest.HueFilterTransformation;
import com.ezscreenrecorder.imgedit.filters.latest.NoFilterTransformation;
import com.ezscreenrecorder.imgedit.filters.latest.PosterFilterTransformation;
import com.ezscreenrecorder.imgedit.filters.latest.SaturationFilterTransformation;
import com.ezscreenrecorder.model.FirebaseDataDevice;
import com.ezscreenrecorder.server.ServerAPI;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import p009io.reactivex.SingleObserver;
import p009io.reactivex.observers.DisposableSingleObserver;

public class FiltersFragment extends Fragment {
    /* access modifiers changed from: private */
    public AppCompatSeekBar appCompatSeekBar;
    /* access modifiers changed from: private */
    public AppCompatSeekBar appCompatSeekBar2;
    /* access modifiers changed from: private */
    public ColorSeekBar colorSeekBar;
    /* access modifiers changed from: private */
    public Transformation currentTransformation;
    /* access modifiers changed from: private */
    public FrameLayout drawLayout;
    private ImageView imgView;
    private RecyclerView mFilterList;
    /* access modifiers changed from: private */
    public String mImagePath;
    /* access modifiers changed from: private */
    public RecyclerView mMaskList;
    /* access modifiers changed from: private */
    public ProgressWheel progressWheel;

    class FilterListAdapter extends Adapter<FilterListViewHolder> {
        /* access modifiers changed from: private */
        public FilterListener filterListener;
        private Context mContext;
        /* access modifiers changed from: private */
        public List<Transformation> mDataSet = new ArrayList();
        /* access modifiers changed from: private */
        public int selectedPosition;

        class FilterListViewHolder extends ViewHolder {
            /* access modifiers changed from: private */
            public final ImageView imgFilter;
            /* access modifiers changed from: private */
            public final FrameLayout selectedFrameLayout;
            /* access modifiers changed from: private */
            public final TextView txtFilter;

            FilterListViewHolder(View view) {
                super(view);
                this.imgFilter = (ImageView) view.findViewById(C0793R.C0795id.img_filter);
                this.txtFilter = (TextView) view.findViewById(C0793R.C0795id.txt_filter_name);
                this.selectedFrameLayout = (FrameLayout) view.findViewById(C0793R.C0795id.view_selected);
                view.setOnClickListener(new OnClickListener(FilterListAdapter.this) {
                    public void onClick(View view) {
                        FilterListAdapter.this.selectedPosition = FilterListViewHolder.this.getAdapterPosition();
                        if (FilterListAdapter.this.selectedPosition != -1) {
                            Transformation transformation = (Transformation) FilterListAdapter.this.mDataSet.get(FilterListAdapter.this.selectedPosition);
                            if (FilterListAdapter.this.filterListener != null) {
                                FilterListAdapter.this.filterListener.getListener(transformation);
                            }
                            FilterListAdapter.this.notifyDataSetChanged();
                        }
                    }
                });
            }
        }

        FilterListAdapter(Context context) {
            this.mContext = context;
            this.mDataSet.add(new NoFilterTransformation());
            this.mDataSet.add(new BlurTransformation(this.mContext, 2));
            this.mDataSet.add(new GrayscaleTransformation(this.mContext));
            this.mDataSet.add(new BrightnessFilterTransformation(this.mContext));
            this.mDataSet.add(new ContrastFilterTransformation(this.mContext));
            this.mDataSet.add(new InvertFilterTransformation(this.mContext));
            this.mDataSet.add(new KuwaharaFilterTransformation(this.mContext));
            this.mDataSet.add(new PixelationFilterTransformation(this.mContext));
            this.mDataSet.add(new SepiaFilterTransformation(this.mContext));
            this.mDataSet.add(new SketchFilterTransformation(this.mContext));
            this.mDataSet.add(new ToonFilterTransformation(this.mContext));
            this.mDataSet.add(new VignetteFilterTransformation(this.mContext));
            this.mDataSet.add(new GammaFilterTransformation(this.mContext));
            this.mDataSet.add(new PosterFilterTransformation(this.mContext));
            this.mDataSet.add(new SaturationFilterTransformation(this.mContext));
            this.mDataSet.add(new EmbossFilterTransformation(this.mContext));
            this.mDataSet.add(new HueFilterTransformation(this.mContext));
            this.mDataSet.add(new GPUExplosureFilterTransformation(this.mContext));
            this.mDataSet.add(new GPUMonochromeFilterTransformation(this.mContext));
            this.mDataSet.add(new BludgeFilterTransformation(this.mContext));
        }

        public FilterListViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            return new FilterListViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.filter_list_item, viewGroup, false));
        }

        public void onBindViewHolder(FilterListViewHolder filterListViewHolder, int i) {
            Transformation transformation = (Transformation) this.mDataSet.get(i);
            if (transformation instanceof NoFilterTransformation) {
                filterListViewHolder.txtFilter.setText(C0793R.string.id_original_txt);
            } else if (transformation instanceof BlurTransformation) {
                filterListViewHolder.txtFilter.setText(C0793R.string.id_blur_txt);
            } else if (transformation instanceof GrayscaleTransformation) {
                filterListViewHolder.txtFilter.setText(C0793R.string.id_greyscale_txt);
            } else if (transformation instanceof BrightnessFilterTransformation) {
                filterListViewHolder.txtFilter.setText(C0793R.string.id_brightness_txt);
            } else if (transformation instanceof ContrastFilterTransformation) {
                filterListViewHolder.txtFilter.setText(C0793R.string.id_contrast_txt);
            } else if (transformation instanceof InvertFilterTransformation) {
                filterListViewHolder.txtFilter.setText(C0793R.string.id_invert_txt);
            } else if (transformation instanceof KuwaharaFilterTransformation) {
                filterListViewHolder.txtFilter.setText(C0793R.string.id_kuwahara_txt);
            } else if (transformation instanceof PixelationFilterTransformation) {
                filterListViewHolder.txtFilter.setText(C0793R.string.id_pixels_txt);
            } else if (transformation instanceof SepiaFilterTransformation) {
                filterListViewHolder.txtFilter.setText(C0793R.string.id_sepia_txt);
            } else if (transformation instanceof SketchFilterTransformation) {
                filterListViewHolder.txtFilter.setText(C0793R.string.id_sketch_txt);
            } else if (transformation instanceof ToonFilterTransformation) {
                filterListViewHolder.txtFilter.setText(C0793R.string.id_toon_txt);
            } else if (transformation instanceof MaskTransformation) {
                filterListViewHolder.txtFilter.setText(C0793R.string.id_mask_txt);
            } else if (transformation instanceof VignetteFilterTransformation) {
                filterListViewHolder.txtFilter.setText(C0793R.string.id_vignette_txt);
            } else if (transformation instanceof GammaFilterTransformation) {
                filterListViewHolder.txtFilter.setText(C0793R.string.id_gamma_txt);
            } else if (transformation instanceof PosterFilterTransformation) {
                filterListViewHolder.txtFilter.setText(C0793R.string.id_poster_txt);
            } else if (transformation instanceof SaturationFilterTransformation) {
                filterListViewHolder.txtFilter.setText(C0793R.string.id_saturation_txt);
            } else if (transformation instanceof EmbossFilterTransformation) {
                filterListViewHolder.txtFilter.setText(C0793R.string.id_emboss_txt);
            } else if (transformation instanceof HueFilterTransformation) {
                filterListViewHolder.txtFilter.setText(C0793R.string.id_hue_txt);
            } else if (transformation instanceof GPUExplosureFilterTransformation) {
                filterListViewHolder.txtFilter.setText(C0793R.string.id_explosure_txt);
            } else if (transformation instanceof GPUMonochromeFilterTransformation) {
                filterListViewHolder.txtFilter.setText(C0793R.string.id_mono_txt);
            } else if (transformation instanceof BludgeFilterTransformation) {
                filterListViewHolder.txtFilter.setText(C0793R.string.id_bludge_txt);
            }
            Glide.with(this.mContext).load(FiltersFragment.this.mImagePath).bitmapTransform(transformation).into(filterListViewHolder.imgFilter);
            if (this.selectedPosition == i) {
                filterListViewHolder.selectedFrameLayout.setVisibility(0);
                filterListViewHolder.txtFilter.setTextColor(ViewCompat.MEASURED_STATE_MASK);
                filterListViewHolder.txtFilter.setBackgroundColor(-1);
                return;
            }
            filterListViewHolder.selectedFrameLayout.setVisibility(4);
            filterListViewHolder.txtFilter.setTextColor(-1);
            filterListViewHolder.txtFilter.setBackgroundColor(ViewCompat.MEASURED_STATE_MASK);
        }

        public int getItemCount() {
            return this.mDataSet.size();
        }

        /* access modifiers changed from: 0000 */
        public void setFilterListener(FilterListener filterListener2) {
            this.filterListener = filterListener2;
        }
    }

    interface FilterListener {
        void getListener(Transformation transformation);
    }

    public void onCreate(@Nullable Bundle bundle) {
        super.onCreate(bundle);
        if (getArguments() != null) {
            this.mImagePath = getArguments().getString(MainImageFragment.CROP_IMAGE_EXTRA_SOURCE);
        }
        setHasOptionsMenu(true);
    }

    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {
        super.onCreateOptionsMenu(menu, menuInflater);
        menuInflater.inflate(C0793R.C0797menu.menu_sub_img_edit, menu);
    }

    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == C0793R.C0795id.action_done) {
            saveImage();
        }
        return super.onOptionsItemSelected(menuItem);
    }

    public void saveImage() {
        this.appCompatSeekBar.setVisibility(4);
        this.appCompatSeekBar2.setVisibility(4);
        this.mMaskList.setVisibility(4);
        ((ImageEditActivity) getActivity()).showLoading();
        new Handler().postDelayed(new Runnable() {
            public void run() {
                FiltersFragment.this.drawLayout.buildDrawingCache();
                Bitmap createBitmap = Bitmap.createBitmap(FiltersFragment.this.drawLayout.getDrawingCache());
                FiltersFragment.this.drawLayout.destroyDrawingCache();
                try {
                    String newOutputPath = ((ImageEditActivity) FiltersFragment.this.getActivity()).getNewOutputPath(FiltersFragment.this.mImagePath);
                    createBitmap.compress(CompressFormat.JPEG, 100, new FileOutputStream(newOutputPath));
                    ((ImageEditActivity) FiltersFragment.this.getActivity()).addImage(newOutputPath);
                    ((ImageEditActivity) FiltersFragment.this.getActivity()).hideLoading();
                    if (FiltersFragment.this.getActivity().getSupportFragmentManager().getBackStackEntryCount() > 1) {
                        FiltersFragment.this.getActivity().getSupportFragmentManager().popBackStack();
                    }
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }, 500);
    }

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        return layoutInflater.inflate(R.layout.fragment_filters, viewGroup, false);
    }

    public void onViewCreated(View view, @Nullable Bundle bundle) {
        super.onViewCreated(view, bundle);
        this.imgView = (ImageView) view.findViewById(C0793R.C0795id.img_draw);
        this.drawLayout = (FrameLayout) view.findViewById(C0793R.C0795id.lay_draw);
        this.mFilterList = (RecyclerView) view.findViewById(C0793R.C0795id.rcyl_filters_list);
        this.appCompatSeekBar = (AppCompatSeekBar) view.findViewById(C0793R.C0795id.seek_bar);
        this.appCompatSeekBar2 = (AppCompatSeekBar) view.findViewById(C0793R.C0795id.seek_bar2);
        this.progressWheel = (ProgressWheel) view.findViewById(C0793R.C0795id.progress_view);
        this.colorSeekBar = (ColorSeekBar) view.findViewById(C0793R.C0795id.color_seek_bar);
        this.colorSeekBar.setOnColorChangeListener(new OnColorChangeListener() {
            public void onColorChangeListener(int i, int i2, int i3) {
                FiltersFragment.this.changeTransformation();
            }
        });
        this.appCompatSeekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
            }

            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
                int progress = seekBar.getProgress();
                PrintStream printStream = System.out;
                StringBuilder sb = new StringBuilder();
                sb.append("PRO-->");
                sb.append(progress);
                printStream.println(sb.toString());
                if (FiltersFragment.this.currentTransformation != null) {
                    if (FiltersFragment.this.currentTransformation instanceof BlurTransformation) {
                        ((BlurTransformation) FiltersFragment.this.currentTransformation).setmRadius(progress);
                    } else if (FiltersFragment.this.currentTransformation instanceof KuwaharaFilterTransformation) {
                        ((KuwaharaFilterTransformation) FiltersFragment.this.currentTransformation).setmRadius(progress);
                    } else if (FiltersFragment.this.currentTransformation instanceof PixelationFilterTransformation) {
                        ((PixelationFilterTransformation) FiltersFragment.this.currentTransformation).setmPixel(((float) progress) / 10.0f);
                    } else {
                        String str = "PRL-->";
                        if (FiltersFragment.this.currentTransformation instanceof BrightnessFilterTransformation) {
                            float f = progress < 10 ? -(((float) progress) / 10.0f) : (((float) progress) - 10.0f) / 10.0f;
                            PrintStream printStream2 = System.out;
                            StringBuilder sb2 = new StringBuilder();
                            sb2.append(str);
                            sb2.append(f);
                            printStream2.println(sb2.toString());
                            ((BrightnessFilterTransformation) FiltersFragment.this.currentTransformation).setmBrightness(f);
                        } else if (FiltersFragment.this.currentTransformation instanceof ContrastFilterTransformation) {
                            ((ContrastFilterTransformation) FiltersFragment.this.currentTransformation).setmContrast(((float) progress) / 10.0f);
                        } else if (FiltersFragment.this.currentTransformation instanceof SepiaFilterTransformation) {
                            ((SepiaFilterTransformation) FiltersFragment.this.currentTransformation).setmIntensity(((float) progress) / 10.0f);
                        } else if (FiltersFragment.this.currentTransformation instanceof ToonFilterTransformation) {
                            ((ToonFilterTransformation) FiltersFragment.this.currentTransformation).setmThreshold(((float) progress) / 10.0f);
                        } else if (FiltersFragment.this.currentTransformation instanceof GammaFilterTransformation) {
                            ((GammaFilterTransformation) FiltersFragment.this.currentTransformation).setmIntensity(((float) progress) / 10.0f);
                        } else if (FiltersFragment.this.currentTransformation instanceof SaturationFilterTransformation) {
                            ((SaturationFilterTransformation) FiltersFragment.this.currentTransformation).setmfloatensity(((float) progress) / 10.0f);
                        } else if (FiltersFragment.this.currentTransformation instanceof PosterFilterTransformation) {
                            ((PosterFilterTransformation) FiltersFragment.this.currentTransformation).setmIntensity(progress);
                        } else if (FiltersFragment.this.currentTransformation instanceof EmbossFilterTransformation) {
                            ((EmbossFilterTransformation) FiltersFragment.this.currentTransformation).setmfloatensity(((float) progress) / 10.0f);
                        } else if (FiltersFragment.this.currentTransformation instanceof HueFilterTransformation) {
                            ((HueFilterTransformation) FiltersFragment.this.currentTransformation).setmfloatensity((float) progress);
                        } else if (FiltersFragment.this.currentTransformation instanceof GPUMonochromeFilterTransformation) {
                            ((GPUMonochromeFilterTransformation) FiltersFragment.this.currentTransformation).setmfloatensity(((float) progress) / 10.0f);
                        } else if (FiltersFragment.this.currentTransformation instanceof GPUExplosureFilterTransformation) {
                            float f2 = progress < 10 ? -(((float) progress) / 10.0f) : (((float) progress) - 10.0f) / 10.0f;
                            PrintStream printStream3 = System.out;
                            StringBuilder sb3 = new StringBuilder();
                            sb3.append(str);
                            sb3.append(f2);
                            printStream3.println(sb3.toString());
                            ((GPUExplosureFilterTransformation) FiltersFragment.this.currentTransformation).setmfloatensity(f2);
                        }
                    }
                }
                FiltersFragment.this.changeTransformation();
            }
        });
        this.appCompatSeekBar2.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
            }

            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
                int progress = seekBar.getProgress();
                PrintStream printStream = System.out;
                StringBuilder sb = new StringBuilder();
                sb.append("PRO2-->");
                sb.append(progress);
                printStream.println(sb.toString());
                if (FiltersFragment.this.currentTransformation != null && (FiltersFragment.this.currentTransformation instanceof ToonFilterTransformation)) {
                    ((ToonFilterTransformation) FiltersFragment.this.currentTransformation).setmQuantizationLevels((float) (progress / 10));
                }
                FiltersFragment.this.changeTransformation();
            }
        });
        this.mFilterList.setLayoutManager(new LinearLayoutManager(getContext(), 0, false));
        this.mMaskList = (RecyclerView) view.findViewById(C0793R.C0795id.rcyl_mask_list);
        this.mMaskList.setLayoutManager(new LinearLayoutManager(getContext(), 0, false));
        FilterListAdapter filterListAdapter = new FilterListAdapter(getContext());
        filterListAdapter.setFilterListener(new FilterListener() {
            public void getListener(Transformation transformation) {
                FiltersFragment.this.currentTransformation = transformation;
                FiltersFragment.this.appCompatSeekBar.setVisibility(8);
                FiltersFragment.this.appCompatSeekBar2.setVisibility(8);
                FiltersFragment.this.mMaskList.setVisibility(8);
                FiltersFragment.this.colorSeekBar.setVisibility(8);
                if (FiltersFragment.this.currentTransformation instanceof BlurTransformation) {
                    FiltersFragment.this.appCompatSeekBar.setVisibility(0);
                    FiltersFragment.this.appCompatSeekBar.setMax(25);
                    FiltersFragment.this.appCompatSeekBar.setProgress(((BlurTransformation) FiltersFragment.this.currentTransformation).getmRadius());
                } else if ((FiltersFragment.this.currentTransformation instanceof GrayscaleTransformation) || (FiltersFragment.this.currentTransformation instanceof InvertFilterTransformation) || (FiltersFragment.this.currentTransformation instanceof SketchFilterTransformation)) {
                    FiltersFragment.this.appCompatSeekBar.setVisibility(4);
                } else if (FiltersFragment.this.currentTransformation instanceof BrightnessFilterTransformation) {
                    FiltersFragment.this.appCompatSeekBar.setVisibility(0);
                    FiltersFragment.this.appCompatSeekBar.setMax(20);
                    FiltersFragment.this.appCompatSeekBar.setProgress(10);
                } else if (FiltersFragment.this.currentTransformation instanceof ContrastFilterTransformation) {
                    FiltersFragment.this.appCompatSeekBar.setVisibility(0);
                    FiltersFragment.this.appCompatSeekBar.setMax(40);
                    FiltersFragment.this.appCompatSeekBar.setProgress(10);
                } else if (FiltersFragment.this.currentTransformation instanceof KuwaharaFilterTransformation) {
                    FiltersFragment.this.appCompatSeekBar.setVisibility(0);
                    FiltersFragment.this.appCompatSeekBar.setMax(25);
                    FiltersFragment.this.appCompatSeekBar.setProgress(5);
                } else if (FiltersFragment.this.currentTransformation instanceof PixelationFilterTransformation) {
                    FiltersFragment.this.appCompatSeekBar.setVisibility(0);
                    FiltersFragment.this.appCompatSeekBar.setMax(Callback.DEFAULT_SWIPE_ANIMATION_DURATION);
                    FiltersFragment.this.appCompatSeekBar.setProgress(100);
                } else if (FiltersFragment.this.currentTransformation instanceof SepiaFilterTransformation) {
                    FiltersFragment.this.appCompatSeekBar.setVisibility(0);
                    FiltersFragment.this.appCompatSeekBar.setMax(25);
                    FiltersFragment.this.appCompatSeekBar.setProgress(10);
                } else if (FiltersFragment.this.currentTransformation instanceof GammaFilterTransformation) {
                    FiltersFragment.this.appCompatSeekBar.setVisibility(0);
                    FiltersFragment.this.appCompatSeekBar.setMax(30);
                    FiltersFragment.this.appCompatSeekBar.setProgress(10);
                } else if (FiltersFragment.this.currentTransformation instanceof PosterFilterTransformation) {
                    FiltersFragment.this.appCompatSeekBar.setVisibility(0);
                    FiltersFragment.this.appCompatSeekBar.setMax(256);
                    FiltersFragment.this.appCompatSeekBar.setProgress(10);
                } else if (FiltersFragment.this.currentTransformation instanceof SaturationFilterTransformation) {
                    FiltersFragment.this.appCompatSeekBar.setVisibility(0);
                    FiltersFragment.this.appCompatSeekBar.setMax(20);
                    FiltersFragment.this.appCompatSeekBar.setProgress(10);
                } else if (FiltersFragment.this.currentTransformation instanceof EmbossFilterTransformation) {
                    FiltersFragment.this.appCompatSeekBar.setVisibility(0);
                    FiltersFragment.this.appCompatSeekBar.setMax(40);
                    FiltersFragment.this.appCompatSeekBar.setProgress(10);
                } else if (FiltersFragment.this.currentTransformation instanceof HueFilterTransformation) {
                    FiltersFragment.this.appCompatSeekBar.setVisibility(0);
                    FiltersFragment.this.appCompatSeekBar.setMax(360);
                    FiltersFragment.this.appCompatSeekBar.setProgress(90);
                } else if (FiltersFragment.this.currentTransformation instanceof ToonFilterTransformation) {
                    FiltersFragment.this.appCompatSeekBar.setVisibility(0);
                    FiltersFragment.this.appCompatSeekBar2.setVisibility(0);
                    FiltersFragment.this.appCompatSeekBar.setMax(10);
                    FiltersFragment.this.appCompatSeekBar.setProgress(1);
                    FiltersFragment.this.appCompatSeekBar2.setMax(100);
                    FiltersFragment.this.appCompatSeekBar2.setProgress(10);
                } else if (FiltersFragment.this.currentTransformation instanceof MaskTransformation) {
                    FiltersFragment.this.mMaskList.setVisibility(0);
                } else if (FiltersFragment.this.currentTransformation instanceof GPUExplosureFilterTransformation) {
                    FiltersFragment.this.appCompatSeekBar.setVisibility(0);
                    FiltersFragment.this.appCompatSeekBar.setMax(20);
                    FiltersFragment.this.appCompatSeekBar.setProgress(10);
                }
                FiltersFragment.this.changeTransformation();
            }
        });
        this.mFilterList.setAdapter(filterListAdapter);
    }

    /* access modifiers changed from: private */
    public void changeTransformation() {
        if (this.currentTransformation != null) {
            this.progressWheel.setVisibility(0);
            if (this.currentTransformation instanceof MaskTransformation) {
                Glide.with(getContext()).load(this.mImagePath).bitmapTransform(new CenterCrop(getContext()), this.currentTransformation).listener((RequestListener<? super ModelType, GlideDrawable>) new RequestListener() {
                    public boolean onException(Exception exc, Object obj, Target target, boolean z) {
                        FiltersFragment.this.progressWheel.setVisibility(8);
                        return false;
                    }

                    public boolean onResourceReady(Object obj, Object obj2, Target target, boolean z, boolean z2) {
                        FiltersFragment.this.progressWheel.setVisibility(8);
                        return false;
                    }
                }).into(this.imgView);
                return;
            }
            Glide.with(getContext()).load(this.mImagePath).bitmapTransform(this.currentTransformation).listener((RequestListener<? super ModelType, GlideDrawable>) new RequestListener() {
                public boolean onException(Exception exc, Object obj, Target target, boolean z) {
                    FiltersFragment.this.progressWheel.setVisibility(8);
                    return false;
                }

                public boolean onResourceReady(Object obj, Object obj2, Target target, boolean z, boolean z2) {
                    FiltersFragment.this.progressWheel.setVisibility(8);
                    return false;
                }
            }).into(this.imgView);
        }
    }

    public void onActivityCreated(@Nullable Bundle bundle) {
        super.onActivityCreated(bundle);
        Glide.with((Fragment) this).load(this.mImagePath).into(this.imgView);
        ((ImageEditActivity) getActivity()).setMyTitle(getString(C0793R.string.id_filters_txt));
        ((ImageEditActivity) getActivity()).setTitleStartAligned(false);
        ServerAPI.getInstance().addToFireBase(getContext(), "Filters").subscribe((SingleObserver<? super T>) new DisposableSingleObserver<FirebaseDataDevice>() {
            public void onSuccess(FirebaseDataDevice firebaseDataDevice) {
            }

            public void onError(Throwable th) {
                th.printStackTrace();
            }
        });
    }
}
